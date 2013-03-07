package com.hbasetmp.hadvisor.contextimpl;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Maps.*;
import static com.hbasetmp.hadvisor.util.Util.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HServerLoad;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.regionserver.compactions.CompactionRequest.CompactionState;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbasetmp.hadvisor.Config;
import com.hbasetmp.hadvisor.HAdvisorProperties;
import com.hbasetmp.hadvisor.context.FileSystemInfo;
import com.hbasetmp.hadvisor.context.HBaseMaster;
import com.hbasetmp.hadvisor.context.HBaseService;
import com.hbasetmp.hadvisor.context.HLogInfo;
import com.hbasetmp.hadvisor.context.MBeanAttributeValue;
import com.hbasetmp.hadvisor.context.MBeanAttributeValues;
import com.hbasetmp.hadvisor.context.RegionDetails;
import com.hbasetmp.hadvisor.context.SnapshotData;
import com.hbasetmp.hadvisor.context.TableName;
import com.hbasetmp.hadvisor.exception.FatalInitializationException;
import com.hbasetmp.hadvisor.exception.JmxConnectionException;
import com.hbasetmp.hadvisor.util.Util;

public class SnapshotDataImpl implements SnapshotData {

    private static final Logger LOG = LoggerFactory.getLogger(SnapshotDataImpl.class);
    
    private final DateTime snapshotTime;
    private final HBaseAdmin hBaseAdmin;
    private final Subscriptions subscriptions;
    private final ClusterStatus clusterStatus;
    private final Collection<ServerName> liveServers;
    private final Map<ServerName, HServerLoad> regionServerLoadDetails;
    private final Map<TableName, List<RegionDetails>> regionDetails;
    private final Map<HBaseService, MBeanAttributeValues> subscribedJmxAttributeValues;

    public SnapshotDataImpl(DateTime snapshotTime, HBaseAdmin hBaseAdmin, Subscriptions subscriptions) throws FatalInitializationException {
        this.snapshotTime = snapshotTime;
        this.hBaseAdmin = hBaseAdmin;
        this.subscriptions = subscriptions;
        this.clusterStatus = obtainClusterStatus(this.hBaseAdmin);
        this.liveServers = this.clusterStatus.getServers();
        this.regionServerLoadDetails = obtainRegionServerLoadDetails(this.clusterStatus, liveServers);
        this.regionDetails = obtainRegionDetails(this.hBaseAdmin);
        this.subscribedJmxAttributeValues = obtainSubscribedJmxAttributeValues(this.subscriptions, clusterStatus);
    }

    @Override
    public DateTime getSnapshotTime() {
        return this.snapshotTime;
    }

    @Override
    public ClusterStatus getClusterStatus() {
        return this.clusterStatus;
    }

    @Override
    public Collection<ServerName> getLiveServers() {
        return this.liveServers;            
    }

    @Override
    public Collection<ServerName> getDeadServers() {
        return this.clusterStatus.getDeadServerNames();
    }

    @Override
    public Map<ServerName, HServerLoad> getRegionServerLoadDetails() {
        return this.regionServerLoadDetails;
    }

    @Override
    public Map<TableName, List<RegionDetails>> getRegionDetails() {
        return this.regionDetails;
    }

    @Override
    public Map<ServerName, HLogInfo> getSubscribedHLogInfo() {
        // TODO: Get log info from the coprocessor
        throw new UnsupportedOperationException("This method is not supported yet");
    }

    @Override
    public List<FileSystemInfo> getSubscribedFileSystemInfo() {
        // TODO: Get filesystem info from the coprocessor
        throw new UnsupportedOperationException("This method is not supported yet");
    }

    @Override
    public Map<HBaseService, MBeanAttributeValues> getSubscribedJMXAttributeValues() {
        return subscribedJmxAttributeValues;
    }

    private static ClusterStatus obtainClusterStatus(HBaseAdmin hBaseAdmin) throws FatalInitializationException {
        try {
            return hBaseAdmin.getClusterStatus();
        } catch (IOException e) {
            // Throw this, since the cluster is presumably in a fairly bad state if this happens
            throw new FatalInitializationException(message("Could not obtain the status of the HBase cluster. " +
            		"The message was {}", e.getMessage()), e);
        }
    }
    
    private static Map<ServerName, HServerLoad> obtainRegionServerLoadDetails(ClusterStatus clusterStatus, Collection<ServerName> liveServers) {
        Map<ServerName, HServerLoad> returnMap = newHashMap();
        for (ServerName liveServer : liveServers) {
            try {
                HServerLoad load = clusterStatus.getLoad(liveServer);
                returnMap.put(liveServer, load);
            }
            catch (Exception e) {
                // Catch everything here (not sure what exception we'll get from HBase if the load data is unavailable)
                LOG.warn(message("Could not get server load details for server {}. The message from HBase admin was {}.", 
                        liveServer.toString(), e.getMessage()), e);
            }
        }
        return returnMap;
    }
    
    private static Map<TableName, List<RegionDetails>> obtainRegionDetails(HBaseAdmin hBaseAdmin) throws FatalInitializationException {
        Map<TableName, List<RegionDetails>> returnMap = newHashMap();
        try {
            HTableDescriptor[] tableList = hBaseAdmin.listTables();
            for (HTableDescriptor tableDescriptor : tableList) {
                List<RegionDetails> regionDetailsList = getRegionsListForTable(hBaseAdmin, tableDescriptor);
                returnMap.put(new TableName(tableDescriptor.getName()), regionDetailsList);
            }
        } catch (Exception e) {
            // Catch everything here (not sure what exception we'll get from HBase if metadata is unavailable)
            LOG.warn(message("Could not get the list of tables. The message from HBase admin was {}.", e.getMessage()), e);
        }
        return returnMap;
    }

    private static List<RegionDetails> getRegionsListForTable(HBaseAdmin hBaseAdmin, HTableDescriptor tableDescriptor) throws IOException {
        try {
            List<HRegionInfo> tableRegions = hBaseAdmin.getTableRegions(tableDescriptor.getName());
            List<RegionDetails> regionDetailsList = newArrayList();
            for (HRegionInfo hRegionInfo : tableRegions) {
                CompactionState compactionState = getCompactionState(hBaseAdmin, hRegionInfo);
                // TODO: Get additional region details from the coprocessor
                regionDetailsList.add(new RegionDetails(hRegionInfo, compactionState, null));
            }
            return regionDetailsList;
        }
        catch (Exception e) {
            // Catch everything here (not sure what exception we'll get from HBase if metadata is unavailable)
            LOG.warn(message("Could not get the table regions for table {}. The message from HBase admin was {}.", 
                    tableDescriptor.getNameAsString(), e.getMessage()), e);
            return null;
        }
    }

    private static CompactionState getCompactionState(HBaseAdmin hBaseAdmin, HRegionInfo hRegionInfo) {
        try {
            return hBaseAdmin.getCompactionState(hRegionInfo.getRegionName());
        } catch (Exception e) {
            // Catch everything here (not sure what exception we'll get from HBase if metadata is unavailable)
            LOG.warn(message("Could not get the compaction state for region {}. The message from HBase admin was {}.", 
                    hRegionInfo.getRegionNameAsString(), e.getMessage()), e);
            return null;
        }
    }
    
    private static Map<HBaseService, MBeanAttributeValues> obtainSubscribedJmxAttributeValues(
            Subscriptions subscriptions, ClusterStatus clusterStatus) {
        Map<HBaseService, MBeanAttributeValues> returnMap = newHashMap();
        HBaseMaster hBaseMaster = new HBaseMaster();
        getSubscribedAttributeValues(hBaseMaster, subscriptions, clusterStatus);
        return returnMap;
    }

    private static MBeanAttributeValues getSubscribedAttributeValues(
            HBaseService hBaseService, Subscriptions subscriptions, ClusterStatus clusterStatus) {

        MBeanServerConnection mBeanServerConnection;
        try {
            mBeanServerConnection = getMBeanServerConnection(hBaseService, clusterStatus);
        } catch (JmxConnectionException e) {
            // Return this exception instead of the attribute values. Log it here so we have the stack trace logged.
            // TODO: Look at more specific exceptions that we get here (authentication etc) so we can provide better
            // error messages.
            LOG.warn(e.getMessage(), e);
            return new MBeanAttributeValues(e);
        }
        
        Map<ObjectName, List<MBeanAttributeValue>> returnMap = newHashMap();
        
        Set<ObjectInstance> mBeans;
        try {
            mBeans = mBeanServerConnection.queryMBeans(null, null);
        } catch (IOException e) {
            // Not sure why this would happen, but return this exception instead of the attribute values
            LOG.warn(e.getMessage(), e);
            return new MBeanAttributeValues(e);
        }
        for (ObjectInstance objectInstance : mBeans) {

            ObjectName objectName = objectInstance.getObjectName();
            
            LOG.trace("Found object name {} with canonical name {}", objectName, objectName.getCanonicalName());
            
            String canonicalName = objectName.getCanonicalName();
            
            MBeanAttributeInfo[] attributes = null;
            
            Collection<JmxAttribute> subcribedJmxAttributeMap = subscriptions.getSubcribedJmxAttributeMap();
            
            for (JmxAttribute jmxAttribute : subcribedJmxAttributeMap) {
                String mBeanNameSubString = jmxAttribute.getMBeanNameSubString();
                if (canonicalName.contains(mBeanNameSubString)) {
                    // Only do this once
                    if (attributes == null) {
                        MBeanInfo mBeanInfo = getMBeanInfo(mBeanServerConnection, objectInstance);
                        if (mBeanInfo != null) {
                            attributes = mBeanInfo.getAttributes();
                        }
                    }
                    addAttributeValuesToMap(mBeanServerConnection, returnMap, objectInstance, objectName, attributes, jmxAttribute);
                }
            }
            
        }
        
        return new MBeanAttributeValues(returnMap);
        
    }

    private static MBeanInfo getMBeanInfo(MBeanServerConnection mBeanServerConnection, ObjectInstance objectInstance) {
        try {
            return mBeanServerConnection.getMBeanInfo(objectInstance.getObjectName());
        } catch (Exception e) {
            // It doesn't really matter what sort of exception we get here -- just log it as a warning and continue
            LOG.warn(message("Could not get info from MBean {}. The message from the MBean server was {}.", 
                    objectInstance.getObjectName().toString(), e.getMessage()), e);
            return null;
        }
    }

    private static MBeanServerConnection getMBeanServerConnection(HBaseService hBaseService, ClusterStatus clusterStatus) 
            throws JmxConnectionException {
        ServerName serverName = Util.getServerName(hBaseService, clusterStatus);
        String hostname = serverName.getHostname();
        int port;
        if (hBaseService.isMaster()) {
            port = Config.getInt(HAdvisorProperties.MASTER_JMX_PORT);
        }
        else {
            port = Config.getInt(HAdvisorProperties.REGION_SERVER_JMX_PORT);
        }
        return connectToMBeanServer(hostname, port);
    }

    private static void addAttributeValuesToMap(MBeanServerConnection mBeanServerConnection, Map<ObjectName, List<MBeanAttributeValue>> returnMap,
            ObjectInstance objectInstance, ObjectName objectName, MBeanAttributeInfo[] attributes, JmxAttribute jmxAttribute) {
        for (MBeanAttributeInfo attributeInfo : attributes) {
            if (attributeInfo.getName().equals(jmxAttribute)) {
                Object attributeValue = getMBeanAttributeValue(mBeanServerConnection, objectInstance, attributeInfo);
                // This can be null if we didn't manage to get the attribute value for some reason
                if (attributeValue != null) {
                    putArrayListItem(returnMap, objectName, new MBeanAttributeValue(attributeInfo, attributeValue));
                }
            }
        }
    }

    private static Object getMBeanAttributeValue(MBeanServerConnection mBeanServerConnection, ObjectInstance objectInstance,
            MBeanAttributeInfo attributeInfo) {
        // We return null if there is an exception here, so that we carry on trying to get other attribute values
        try {
            return mBeanServerConnection.getAttribute(objectInstance.getObjectName(), attributeInfo.getName());
        } catch (Exception e) {
           // It doesn't really matter what sort of exception we get here -- just log it as a warning and continue
           LOG.warn(message("Could not get attribute {} from MBean {}. The message from the MBean server was {}.", 
                   attributeInfo.getName(), objectInstance.getObjectName().toString(), e.getMessage()), e);
        }
        return null;
    }
    
    private static MBeanServerConnection connectToMBeanServer(String hostname, int port) throws JmxConnectionException {
        Map<String, String[]> env = new HashMap<String, String[]>();
        String[] creds = {"monitorRole", "monitor"};
        env.put(JMXConnector.CREDENTIALS, creds);
        try {
            LOG.trace("Connecting to MBean server at [{}:{}]", hostname, port);
            String url = "service:jmx:rmi:///jndi/rmi://" + hostname + ":" + port + "/jmxrmi";
            JMXServiceURL serviceUrl = new JMXServiceURL(url);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceUrl, env);
            MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
            mBeanServerConnection.getMBeanCount(); // Don't believe we're really connected until we do something
            LOG.debug("Connected successfully!");
            return mBeanServerConnection;
        } catch (MalformedURLException e) {
            throw new JmxConnectionException(hostname, port, e);
        } catch (IOException e) {
            throw new JmxConnectionException(hostname, port, e);
        } catch (RuntimeException e) {
            throw new JmxConnectionException(hostname, port, e);
        }
    }

}
