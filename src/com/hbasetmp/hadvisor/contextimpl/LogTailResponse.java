package com.hbasetmp.hadvisor.contextimpl;

import java.util.List;
import java.util.Map;

public class LogTailResponse {

    private final Map<String, List<String>> containingMatchesLogLines;
    private final Map<String, List<String>> regexMatchesLogLines;

    public LogTailResponse(Map<String, List<String>> containingMatchesLogLines, Map<String, List<String>> regexMatchesLogLines) {
        this.containingMatchesLogLines = containingMatchesLogLines;
        this.regexMatchesLogLines = regexMatchesLogLines;
    }

    public Map<String, List<String>> getContainingMatchesLogLines() {
        return containingMatchesLogLines;
    }

    public Map<String, List<String>> getRegexMatchesLogLines() {
        return regexMatchesLogLines;
    }
    
}
