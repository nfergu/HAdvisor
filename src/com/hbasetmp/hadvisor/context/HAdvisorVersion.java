/**
 * Copyright (C) 2010 Causata
 */
package com.hbasetmp.hadvisor.context;

public class HAdvisorVersion {

    public static void main(String[] args) {
        System.out.println(CURRENT_VERSION);
    }
    
    private static final HAdvisorVersion CURRENT_VERSION = new HAdvisorVersion(new Version(0, 1, 1), new Version(0, 94, 2));
    
    private final Version version;
    private final Version compiledAgainstHBaseVersion;

    public HAdvisorVersion(Version version, Version compiledAgainstHBaseVersion) {
        this.version = version;
        this.compiledAgainstHBaseVersion = compiledAgainstHBaseVersion;
    }
    
    public HAdvisorVersion(String versionString) {
        // TODO: Parse this
        this.version = null;
        this.compiledAgainstHBaseVersion = null;
    }

    @Override
    public String toString() {
        return version + "-hbase" + compiledAgainstHBaseVersion;
    }
    
}
