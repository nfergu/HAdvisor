package com.hbasetmp.hadvisor.context;

import java.util.List;

import org.apache.hadoop.hbase.regionserver.compactions.CompactionProgress;

public interface StoreInfo {

	public long getSize();
	
	public int getNumberOfStoreFiles();
	
	public long getLastCompactSize();

	public List<StoreFileInfo> getStoreFileInfo();
	
	public CompactionProgress getCompactionProgress();
	
	public boolean needsCompaction();
	
}
