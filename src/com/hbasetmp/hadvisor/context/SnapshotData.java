package com.hbasetmp.hadvisor.context;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.management.ObjectName;

import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HServerLoad;
import org.apache.hadoop.hbase.ServerName;
import org.joda.time.DateTime;

/**
 * Provides access to data relating to the HBase cluster at a particular point in time.
 */
public interface SnapshotData {
	
	/**
	 * Gets the time of this snapshot.
	 */
	public DateTime getSnapshotTime();

	/**
	 * Gets the status of the cluster at the time of the snapshot.
	 */
	public ClusterStatus getClusterStatus();

	/**
	 * Gets a list of the live region servers in the cluster at the time of the snapshot.
	 */
	public Collection<ServerName> getLiveServers();

	/**
	 * Gets a list of the dead region servers in the cluster at the time of the snapshot.
	 */
	public Collection<ServerName> getDeadServers();

	/**
	 * Gets load details of the region servers in the HBase cluster.
	 * 
	 * @return A {@link Map}, where the key is the region server, and the value is the load details for that region
	 * server
	 */
	public Map<ServerName, HServerLoad> getRegionServerLoadDetails();

	/**
	 * Gets details about the regions in the HBase cluster
	 * 
	 * @return A {@link Map}, where the key is the table name, and the value is the region information for that table
	 */
	public Map<TableName, List<RegionDetails>> getRegionDetails();
	
	/**
	 * <p>Gets information about the HLog on all available region servers. 
	 * 
	 * <p>Note that the advisor must have subscribed to this data (by passing {@link SubscriptionItem#HLOG_INFO} to the 
     * {@link InitializationContext#subscribe(SubscriptionItem)} method), and the HAdvisor coprocessors must
	 * be installed in HBase for this method to return data (an empty map is returned otherwise).
	 * 
	 * @return A {@link Map}, where the key is the server name, and the value is the information about the HLog
	 * on that server
	 */
	public Map<ServerName, HLogInfo> getSubscribedHLogInfo();

	/**
	 * <p>Gets data about all of the filesystems that are used by HBase. 
	 * 
	 * <p><p>Note that the advisor must have subscribed to this data (by passing {@link SubscriptionItem#FILESYSTEM_INFO} to the 
     * {@link InitializationContext#subscribe(SubscriptionItem)} method), and the HAdvisor coprocessors must 
     * be installed in HBase for this method to return data (an empty list is returned otherwise).
	 */
	public List<FileSystemInfo> getSubscribedFileSystemInfo();

	/**
	 * Gets the JMX attribute values that the calling advisor has subscribed to 
	 * (using the {@link InitializationContext}).
	 * 
	 * @return A {@link Map}, where the key is the service, and the value is a map contains attribute values for that MBean.
	 */
	public Map<HBaseService, MBeanAttributeValues> getSubscribedJMXAttributeValues();
	
}
