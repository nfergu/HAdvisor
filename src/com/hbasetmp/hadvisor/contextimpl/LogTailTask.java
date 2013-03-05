package com.hbasetmp.hadvisor.contextimpl;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;

/**
 * Gets the tail of the content from a URL and sends results to a destination
 */
public class LogTailTask implements Runnable {

    private final String url;
    private final int previousLength;
    private final long previousTimestamp;

    public LogTailTask(String url, int previousLength, long previousTimestamp) {
        this.url = url;
        this.previousLength = previousLength;
        this.previousTimestamp = previousTimestamp;
    }
    
    @Override
    public void run() {
        
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        int length = conn.getContentLength();
        
        if (length == -1) {
            System.out.println("Unable to determine length of document with HEAD request.");
            System.out.println("This document and/or web server is not suitable for tailing.");
            System.exit(0);
        }
        
        if (lastSize == -1) {
            lastSize = length - 1024 * 4; // Get 4K at first
        }
        
        conn.disconnect();
        
        if (length > lastSize) {
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setRequestProperty("Range", "bytes=" + lastSize + "-" + length);
            
            BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
            int bytesRead = 0;
            while ((bytesRead = input.read(buffer, 0, buffer.length)) != -1) {
                System.out.write(buffer, 0, bytesRead);
            }
            System.out.flush();
            
            conn.disconnect();
        }
        else if (length < lastSize) {
            System.out.println();
            System.out.println("[Log file shrank in size. Restarting at end.]");
            System.out.println();
        }
        
        lastSize = length;
        
    }

    
    
}
