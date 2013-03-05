package com.hbasetmp.hadvisor.context;

import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.tool.Canary;
import org.apache.log4j.Level;
import org.jsoup.nodes.Document;

import com.hbasetmp.hadvisor.advisor.Advisor;

/**
 * Provides access to data relating to the current state of the HBase cluster.
 */
public interface CurrentData extends SnapshotData {

	/**
	 * Gets a list of descriptors for the tables that are present in the HBase cluster. Note that modifying
	 * the returned {@link HTableDescriptor}s has no effect on the underlying HBase cluster.
	 */
	public List<HTableDescriptor> getTableDescriptors();

	/**
	 * Gets the configuration properties for the specified service. This includes all properties, including
	 * those where the default value has not been overridden. Note that the list may be empty (or may not
	 * contain all properties) if the HBase web UI cannot be accessed (connection details are misconfigured,
	 * the web UI has been disabled in HBase).
	 * 
	 * @return A {@link Map}, where the key is the name of the config property, and the value is a
	 * {@link ConfigProperty}
	 */
	public Map<String, ConfigProperty> getConfigProperties(HBaseService serverName);

	/**
	 * Gets a time-ordered list of the HBase log messages that the calling advisor has subscribed to 
	 * (using the {@link InitializationContext}), since the last time the {@link Advisor#getAdvice(AdviceContext)} 
	 * method was called. The logs must be accessible via the HBase Web UI.
	 */
	public List<LogMessage> getHBaseSubscribedLogMessages();

	/**
	 * Gets a time-ordered list of the non-HBase log messages that the calling advisor has subscribed to 
	 * (using the {@link InitializationContext}) since the last time the {@link Advisor#getAdvice(AdviceContext)} 
	 * method was called. The logs must be accessible via the HBase Web UI, which means that they must be in the same 
	 * directory as the HBase logs (typically <tt>/var/log/hbase</tt>).
	 * 
	 * @return A {@link Map}, where the key is the name of the log file, and the value is a time-ordered list
	 * of log messages
	 */
	public Map<String, List<LogMessage>> getNonHBaseSubscribedLogMessages();
	
	/**
	 * Gets the current logging level in HBase for the specified service and logger name
	 */
	public Level getCurrentLoggingLevel(HBaseService serverName, String loggerName);

	/**
	 * Gets the debug log from the HBase Web UI, for the specified service (this is accessible from <tt>/dump</tt> on
	 * the HBase Web UI). This is a raw, unparsed version of the debug log.
	 */
	public Reader getRawDebugLog(HBaseService serverName);

	/**
	 * Gets the thread dump from the HBase Web UI, for the specified service (this is accessible from <tt>/stacks</tt> on
	 * the HBase Web UI). This is a raw, unparsed version of the thread dump.
	 */
	public Reader getRawThreadDump(HBaseService serverName);

	/**
	 * Gets a parsed version of the main page of the HBase Web UI, for the specified server, as a JSoup {@link Document}.
	 */
	public Document getRawWebUI(ServerName serverName);

	/**
	 * Gets a parsed version of the main page of the HBase Master Web UI, as a JSoup {@link Document}.
	 */
	public Document getMasterRawWebUI();

	/**
	 * Gets a parsed version of the table details page from the HBase Master Web UI, as a JSoup {@link Document}. Note
	 * that most of the details that are available on this page should also be available from the 
	 * {@link #getTableDescriptors()} and {@link #getRegionDetails()} 
	 */
	public Document getTableDetailsRawWebUI(String tableName);

	/**
	 * Gets the ZooKeeper dump from the HBase Master Web UI.
	 */
	public Reader getZooKeeperDump(String tableName);
	
	/**
	 * Gets the output of running the {@link Canary} against the HBase Cluster.
	 */
	public Collection<CanaryOutput> getCanaryOutput();
	
}
