package com.hbasetmp.hadvisor.context;

public class Version {

	private final int majorVersion;
	private final int minorVersion;
	private final int release;
	
	public Version(int majorVersion, int minorVersion, int release) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.release = release;
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public int getRelease() {
		return release;
	}
	
	@Override
	public String toString() {
	    return majorVersion + "." + minorVersion + "." + release;
	}
	
}
