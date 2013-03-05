package com.hbasetmp.hadvisor.context;

import org.apache.hadoop.hbase.regionserver.wal.HLog.Metric;

public interface HLogInfo {

	public Metric getWriteTime();

	public Metric getWriteSize();

	public Metric getSyncTime();

	public long getSlowAppendCount();

	public Metric getSlowAppendTime();
	
}
