package com.hbasetmp.hadvisor.context;

import org.apache.hadoop.hbase.ServerName;

public class HBaseRegionServer extends HBaseService {
	
	public HBaseRegionServer(ServerName serverName) {
		super(false, true, serverName);
	}

}
