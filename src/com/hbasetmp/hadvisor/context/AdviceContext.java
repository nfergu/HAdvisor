package com.hbasetmp.hadvisor.context;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Range;
import com.hbasetmp.hadvisor.advisor.Advisor;

/**
 * <p>Provides context when an advisor is giving advice, using the {@link Advisor#getAdvice(AdviceContext)} method.
 *  
 * <p>Data can obtained about the current state of the HBase cluster using the {@link #getCurrentData()} method, 
 * and snapshots of some of this data over time can be obtained using the {@link #getSnapshotData()} method. 
 * 
 * However, the snapshots do not contain all of the data that it is possible to obtain from the 
 * {@link #getCurrentData()} method, as it is not appropriate or efficient to create snapshots of certain types of data. 
 */
public interface AdviceContext extends BaseContext {
	
	/**
	 * Gets data relating to the current state of the HBase cluster. For snapshots of past data, use the
	 * {@link #getSnapshotData()} method.
	 */
	public CurrentData getCurrentData();
	
	/**
	 * Gets a time-ordered list of snapshots, which contain data relating to the HBase cluster over time. The 
	 * time range of the snapshots can be obtained using the {@link #getSnapshotTimeRange()} method.
	 */
	public List<SnapshotData> getSnapshotData();

	/**
	 * Gets the time range of the snapshots returned by the {@link #getSnapshotData()} method. 
	 */
	public Range<DateTime> getSnapshotTimeRange();

}
