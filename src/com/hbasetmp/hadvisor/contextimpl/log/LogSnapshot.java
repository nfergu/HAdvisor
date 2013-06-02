package com.hbasetmp.hadvisor.contextimpl.log;

import java.util.List;
import java.util.Map;

import com.hbasetmp.hadvisor.context.LogMessage;

/**
 * A snapshot of log messages
 */
public class LogSnapshot {

    public List<LogMessage> getHBaseLogMessages(Class<?> advisor) {
        return null;
    }
    
    public Map<String, List<LogMessage>> getNonHBaseLogMessages(Class<?> advisor) {
        return null;
    }
    
}
