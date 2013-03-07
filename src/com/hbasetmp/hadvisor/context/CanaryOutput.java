package com.hbasetmp.hadvisor.context;

public class CanaryOutput {

    private final byte[] regionName;
    private final byte[] columnFamilyName;
    private final long latency;
    private final Exception exception;

    public CanaryOutput(byte[] regionName, byte[] columnFamilyName, long latency) {
        this.regionName = regionName;
        this.columnFamilyName = columnFamilyName;
        this.latency = latency;
        this.exception = null;
    }
    
    public CanaryOutput(byte[] regionName, Exception exception) {
        this.regionName = regionName;
        this.columnFamilyName = null;
        this.latency = 0;
        this.exception = exception;
    }
    
    public CanaryOutput(byte[] regionName, byte[] columnFamilyName, Exception exception) {
        this.regionName = regionName;
        this.latency = 0;
        this.columnFamilyName = columnFamilyName;
        this.exception = exception;
    }

    public byte[] getColumnFamilyName() {
        return columnFamilyName;
    }

    public byte[] getRegionName() {
        return regionName;
    }

    public long getLatency() {
        return latency;
    }

    public Exception getException() {
        return exception;
    }
    
}
