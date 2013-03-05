package com.hbasetmp.hadvisor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.ServerName;

import com.hbasetmp.hadvisor.context.HBaseService;


public class Util {

    private static final String REPLACEMENT_TOKEN = "{}";
    
	public static String message(String originalMessage, String... replacements) {
	    StringBuilder stringBuilder = new StringBuilder(originalMessage);
	    int searchFrom = 0;
	    for (String replacement : replacements) {
	        String replacementText = "'" + replacement + "'";
	        int nextIndex = stringBuilder.indexOf(REPLACEMENT_TOKEN, searchFrom);
	        if (nextIndex != -1) {
	            stringBuilder.replace(nextIndex, nextIndex + REPLACEMENT_TOKEN.length(), replacementText);
	        }
	        searchFrom = nextIndex + replacementText.length();
	        if (searchFrom >= stringBuilder.length()) {
	            break;
	        }
	    }
	    return stringBuilder.toString();
	}
	
	public static ServerName getServerName(HBaseService hBaseService, ClusterStatus clusterStatus) {
	    if (hBaseService.isMaster()) {
	        return clusterStatus.getMaster();
	    }
	    else {
	        return hBaseService.getRegionServerName();
	    }
	}

    public static <K,V> void putArrayListItem(Map<K, List<V>> map, K key, V value) {
        List<V> list = map.get(key);
        if (list == null) {
            list = new ArrayList<V>();
            map.put(key, list);
        }
        list.add(value);
    }

    public static String csvList(String... values) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);
            if (i < values.length - 1) {
                builder.append(",");
            }
        }
        return builder.toString();
    }
	
}
