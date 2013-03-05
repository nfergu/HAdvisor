package com.hbasetmp.hadvisor.context;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HDFSBlocksDistribution;
import org.apache.hadoop.hbase.util.Pair;

public interface AdditionalRegionInfo {

	public boolean isAvailable();
	
	public boolean isClosed();

	public boolean isClosing();

	public boolean isSplittable();

	public boolean areWritesEnabled();

	public long getLastFlushTime();

	public long getLargestHStoreSize();

	public boolean needsCompaction();

	public int getCompactPriority();

	public List<Pair<Long, Long>> getRecentFlushInfo();

	public HDFSBlocksDistribution getHDFSBlocksDistribution();

	public Map<StoreKey, StoreInfo> getStoreInfo();

}
