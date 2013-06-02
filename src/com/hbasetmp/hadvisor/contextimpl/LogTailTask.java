package com.hbasetmp.hadvisor.contextimpl;

import static com.google.common.collect.Maps.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbasetmp.hadvisor.util.Util;

/**
 * Gets the tail of the content from a URL (that matches certain filters) and returns the results
 */
public class LogTailTask implements Callable<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(LogTailTask.class);
    
    private static final int INITIAL_SIZE = 1024 * 100;
    
    private final String url;
    private final long previousLength;
    private final int maximumToGet;

    private final List<String> containingMatches;
    private final List<String> regexMatches;

    public LogTailTask(String url, long previousLength, int maximumToGet, 
            List<String> containingMatches, List<String> regexMatches) throws MalformedURLException {
        this.url = url;
        this.maximumToGet = maximumToGet;
        this.previousLength = previousLength;
        this.containingMatches = containingMatches;
        this.regexMatches = regexMatches;
    }
    
    @Override
    public Object call() throws ClientProtocolException, IOException {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        
        HttpHead httpHead = new HttpHead(url);
        HttpResponse response = httpClient.execute(httpHead);
        HttpEntity entity = response.getEntity();
        
        long currentLength = entity.getContentLength();
        if (currentLength == -1) {
            LOG.warn(Util.message("Could not tail log file at URL {} as the content length was unknown.", url));
            return null;
        }
        if (currentLength == 0) {
            // No data, so nothing to do
            return null;
        }
        // We always end at the end of the file, but the start position is more complex
        long endAt = currentLength;
        long startAt;
        // If we don't know the previous length, or it is greater than the current length 
        // then just get INITIAL_SIZE worth of data
        if (previousLength <= 0 || previousLength > currentLength) {
            startAt = Math.max(endAt - INITIAL_SIZE, 0);
        }
        else {
            startAt = Math.max(previousLength, endAt - maximumToGet);
        }
        
        if (startAt < endAt) {
            return getContentBody(httpClient, endAt, startAt);
        }
        else {
            return null;
        }

    }

    private LogTailResponse getContentBody(DefaultHttpClient httpClient, long endAt, long startAt) throws IOException, ClientProtocolException {
        
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Range", "bytes=" + startAt + "-" + endAt);
        HttpResponse bodyResponse = httpClient.execute(httpGet);
        HttpEntity bodyEntity = bodyResponse.getEntity();

        ContentType contentType = ContentType.get(bodyEntity);
        Charset charSet;
        if (contentType != null) {
            charSet = contentType.getCharset();
        }
        else {
            charSet = Charset.forName("utf-8");
        }
        
        InputStream content = bodyEntity.getContent();
        
        BufferedReader input = new BufferedReader(new InputStreamReader(content, charSet));

        Map<String, List<String>> containingMatchesLogLines = newHashMap();
        Map<String, List<String>> regexMatchesLogLines = newHashMap();
        
        String currentLine;
        while ((currentLine = input.readLine()) != null) {
            for (String containingMatch : containingMatches) {
                if (currentLine.contains(containingMatch)) {
                    Util.putArrayListItem(containingMatchesLogLines, containingMatch, currentLine);
                }
            }
            for (String regexMatch : regexMatches) {
                if (currentLine.matches(regexMatch)) {
                    Util.putArrayListItem(regexMatchesLogLines, regexMatch, currentLine);
                }
            }
        }
        
        return new LogTailResponse(containingMatchesLogLines, regexMatchesLogLines);
        
    }
    
}
