package com.hbasetmp.hadvisor.context;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.management.MBeanAttributeInfo;
import javax.management.ObjectName;

import org.apache.hadoop.hbase.HTableDescriptor;

import com.hbasetmp.hadvisor.advisor.Advisor;

/**
 * Provides context for the {@link Advisor#initialize(InitializationContext)} method, allowing the advisor to subscribe
 * to various data items.
 */
public interface InitializationContext extends BaseContext {

    /**
     * Subscribes to the item specified by the <tt>subscriptionItem</tt> parameter. The subscribed item will
     * be available from {@link CurrentData} or {@link SnapshotData}.
     */
    public void subscribe(SubscriptionItem subscriptionItem);
    
	/**
	 * Subscribes the advisor to HBase log messages containing the specified text (as a substring). These messages will then be
	 * available from the {@link CurrentData#getHBaseSubscribedLogMessages()}. 
	 * The logs must be accessible via the HBase Web UI.
	 */
	public void subscribeToHBaseLogMessages(String substring);
	
	/**
	 * Has the same functionality as the {@link #subscribeToHBaseLogMessages(String)} method, but the
	 * log messages must match the specified regular expression.
	 */
	public void subscribeToHBaseLogMessages(Pattern regex);

	/**
	 * Subscribes the advisor to non-HBase log messages, where the <tt>logNameSubstring</tt> text is contained 
	 * in the log file name, and the <tt>messageText</tt> text is contained in the log message. These messages will 
	 * then be available from the {@link CurrentData#getHBaseSubscribedLogMessages()}. 
	 * The logs must be accessible via the HBase Web UI, which means that they must be in the same directory as 
	 * the HBase logs (typically <tt>/var/log/hbase</tt>).
	 */
	public void subscribeToNonHBaseLogMessages(String logNameSubstring, String messageText);
	
	/**
	 * Has the same functionality as the {@link #subscribeToNonHBaseLogMessagess(String, String)} method, 
	 * but the log messages must match the specified regular expression.
	 */
	public void subscribeToNonHBaseLogMessages(String logNameSubstring, Pattern messageRegex);
	
	/**
	 * Subscribes the advisor to values from the specified JMX attribute, where the MBean that contains the attribute
	 * is a built-in HBase MBean (see {@link HBaseMBean}), specified by the <tt>hBaseMBean</tt> parameter, and
	 * the name of the attribute is specified by the <tt>attributeName</tt> parameter.
	 */
	public void subscribeToJmxAttribute(HBaseMBean hBaseMBean, String attributeName);
	
	/**
	 * Subscribes the advisor to values from the specified JMX attribute, where the MBean that has the required attribute
	 * is specified by the <tt>mBeanNameSubString</tt> (the value of this parameter is be a substring of the 
	 * MBean name), and the name of the attribute is specified by the <tt>attributeName</tt> parameter. Note that 
	 * this can potentially evaluate to multiple attribute values.
	 */
	public void subscribeToJmxAttribute(String mBeanNameSubString, String attributeName);
	
	/**
	 * Gets all of the JMX attributes that are available for each service in the HBase cluster. This method is
	 * typically used in development, and for debugging.
	 * 
	 * @return A {@link Map}, where the key is an HBase service, and the value is a mapping from the MBean
	 * object name to a list of attributes that are available for that MBean.
	 */
	public Map<HBaseService, Map<ObjectName, List<MBeanAttributeInfo>>> getAvailableJMXAttributes();
	
	/**
	 * Gets a list of descriptors for the tables that are present in the HBase cluster. Note that modifying
	 * the returned {@link HTableDescriptor}s has no effect on the underlying HBase cluster.
	 */
	public List<HTableDescriptor> getTableDescriptors();
	
}
