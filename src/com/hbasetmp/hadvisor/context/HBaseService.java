package com.hbasetmp.hadvisor.context;

import org.apache.hadoop.hbase.ServerName;

/**
 * Represents a service in HBase, which can either be a master or a region server.
 */
public abstract class HBaseService {

	private final boolean isMaster;
	private final boolean isRegionServer;
	private final ServerName regionServerName;

	public HBaseService(boolean isMaster, boolean isRegionServer, ServerName regionServerName) {
		this.isMaster = isMaster;
		this.isRegionServer = isRegionServer;
		this.regionServerName = regionServerName;
	}

	/**
	 * Whether the service is a master
	 */
	public boolean isMaster() {
		return isMaster;
	}

	/**
	 * Whether the service is a region server
	 */
	public boolean isRegionServer() {
		return isRegionServer;
	}

	/**
	 * The {@link ServerName} of the region server, is this is one, or <tt>null</tt> otherwise. 
	 */
	public ServerName getRegionServerName() {
		return regionServerName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isMaster ? 1231 : 1237);
		result = prime * result + (isRegionServer ? 1231 : 1237);
		result = prime
				* result
				+ ((regionServerName == null) ? 0 : regionServerName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HBaseService other = (HBaseService) obj;
		if (isMaster != other.isMaster)
			return false;
		if (isRegionServer != other.isRegionServer)
			return false;
		if (regionServerName == null) {
			if (other.regionServerName != null)
				return false;
		} else if (!regionServerName.equals(other.regionServerName))
			return false;
		return true;
	}
	
}
