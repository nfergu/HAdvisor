package com.hbasetmp.hadvisor.context;

import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileSystem.Statistics;

public interface FileSystemInfo {

	public long getUsed();
	
	public Statistics getStatistics();

	public ContentSummary getContentSummary();
	
}
