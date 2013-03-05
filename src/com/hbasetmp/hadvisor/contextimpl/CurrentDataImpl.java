package com.hbasetmp.hadvisor.contextimpl;

import static com.google.common.collect.Maps.*;
import static com.hbasetmp.hadvisor.util.Util.*;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HServerLoad;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.log4j.Level;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.hbasetmp.hadvisor.Config;
import com.hbasetmp.hadvisor.HAdvisorProperties;
import com.hbasetmp.hadvisor.context.CanaryOutput;
import com.hbasetmp.hadvisor.context.ConfigProperty;
import com.hbasetmp.hadvisor.context.CurrentData;
import com.hbasetmp.hadvisor.context.FileSystemInfo;
import com.hbasetmp.hadvisor.context.HBaseService;
import com.hbasetmp.hadvisor.context.HLogInfo;
import com.hbasetmp.hadvisor.context.LogMessage;
import com.hbasetmp.hadvisor.context.MBeanAttributeValues;
import com.hbasetmp.hadvisor.context.RegionDetails;
import com.hbasetmp.hadvisor.context.SnapshotData;
import com.hbasetmp.hadvisor.context.TableName;
import com.hbasetmp.hadvisor.exception.FatalInitializationException;
import com.hbasetmp.hadvisor.util.Util;

public class CurrentDataImpl implements CurrentData {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentDataImpl.class);
 
    private final HBaseAdmin hBaseAdmin;
    private final Subscriptions subscriptions;
    private final SnapshotData snapshotData;
    private final List<HTableDescriptor> tableDescriptors;

    private final Map<HBaseService, Map<String, ConfigProperty>> configPropertyCache = newConcurrentMap();
    
    public CurrentDataImpl(HBaseAdmin hBaseAdmin, Subscriptions subscriptions, SnapshotData snapshotData) throws FatalInitializationException {
        this.hBaseAdmin = hBaseAdmin;
        this.subscriptions = subscriptions;
        this.snapshotData = snapshotData;
        this.tableDescriptors = obtainTableDescriptors(hBaseAdmin);
    }

    @Override
    public DateTime getSnapshotTime() {
        return this.snapshotData.getSnapshotTime();
    }

    @Override
    public ClusterStatus getClusterStatus() {
        return this.snapshotData.getClusterStatus();
    }

    @Override
    public Collection<ServerName> getLiveServers() {
        return this.snapshotData.getLiveServers();
    }

    @Override
    public Collection<ServerName> getDeadServers() {
        return this.snapshotData.getDeadServers();
    }

    @Override
    public Map<ServerName, HServerLoad> getRegionServerLoadDetails() {
        return this.snapshotData.getRegionServerLoadDetails();
    }

    @Override
    public Map<TableName, List<RegionDetails>> getRegionDetails() {
        return this.snapshotData.getRegionDetails();
    }

    @Override
    public Map<ServerName, HLogInfo> getSubscribedHLogInfo() {
        return this.snapshotData.getSubscribedHLogInfo();
    }

    @Override
    public List<FileSystemInfo> getSubscribedFileSystemInfo() {
        return this.snapshotData.getSubscribedFileSystemInfo();
    }

    @Override
    public Map<HBaseService, MBeanAttributeValues> getSubscribedJMXAttributeValues() {
        return this.snapshotData.getSubscribedJMXAttributeValues();
    }

    @Override
    public List<HTableDescriptor> getTableDescriptors() {
        return this.tableDescriptors;
    }

    @Override
    public Map<String, ConfigProperty> getConfigProperties(HBaseService hbaseService) {
        Map<String, ConfigProperty> configProperties = configPropertyCache.get(hbaseService);
        if (configProperties == null) {
            configProperties = obtainConfigProperties(hbaseService);
            configPropertyCache.put(hbaseService, configProperties);
        }
        return configProperties;
    }

    @Override
    public List<LogMessage> getHBaseSubscribedLogMessages() {
        // TODO DO THIS NEXT!
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getNonHBaseSubscribedLogMessages()
     */
    @Override
    public Map<String, List<LogMessage>> getNonHBaseSubscribedLogMessages() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getCurrentLoggingLevel(com.hbasetmp.hadvisor.context.HBaseService, java.lang.String)
     */
    @Override
    public Level getCurrentLoggingLevel(HBaseService serverName, String loggerName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getRawDebugLog(com.hbasetmp.hadvisor.context.HBaseService)
     */
    @Override
    public Reader getRawDebugLog(HBaseService serverName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getRawThreadDump(com.hbasetmp.hadvisor.context.HBaseService)
     */
    @Override
    public Reader getRawThreadDump(HBaseService serverName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getRawWebUI(org.apache.hadoop.hbase.ServerName)
     */
    @Override
    public Document getRawWebUI(ServerName serverName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getMasterRawWebUI()
     */
    @Override
    public Document getMasterRawWebUI() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getTableDetailsRawWebUI(java.lang.String)
     */
    @Override
    public Document getTableDetailsRawWebUI(String tableName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getZooKeeperDump(java.lang.String)
     */
    @Override
    public Reader getZooKeeperDump(String tableName) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.hbasetmp.hadvisor.context.CurrentData#getCanaryOutput()
     */
    @Override
    public Collection<CanaryOutput> getCanaryOutput() {
        // TODO Auto-generated method stub
        return null;
    }
    
    private Map<String, ConfigProperty> obtainConfigProperties(HBaseService hbaseService) {
        Map<String, ConfigProperty> configProperties = newHashMap();
        ServerName serverName = Util.getServerName(hbaseService, snapshotData.getClusterStatus());
        String hostname = serverName.getHostname();
        int port;
        if (hbaseService.isMaster()) {
            port = Config.getInt(HAdvisorProperties.MASTER_WEB_UI_PORT);
        }
        else {
            port = Config.getInt(HAdvisorProperties.REGION_SERVER_WEB_UI_PORT);
        }
        String path = Config.getString(HAdvisorProperties.WEB_UI_CONFIG_PATH);
        String url = "http://" + hostname + ":" + port + "/" + path;
        Connection connection;
        try {
            connection = Jsoup.connect(url);
        } catch (Exception e) {
            // Catch everything here (not sure what exception we'll get if the properties are unavailable)
            // This is just a warning, since its expected if things aren't configured right.
            LOG.warn(message("Could not connect to the web UI at {} to get config properties. The error message was {}", url, e.getMessage()), e);
            return configProperties;
        }
        Document document;
        try {
            document = connection.parser(Parser.xmlParser()).get();
        } catch (Exception e) {
            // Catch everything here (not sure what exception we'll get if we cannot parse)
            // This is just a warning, since its expected if things aren't configured right.
            LOG.warn(message("Could not parse the document at {} as XML, to get the config properties. " +
                    "The error message was {}", url, e.getMessage()), e);
            return configProperties;
        }
        
        addPropertiesFromXml(configProperties, document);
        return configProperties;
    }
    
    private void addPropertiesFromXml(Map<String, ConfigProperty> configProperties, Document document) {
        Elements properties = document.getElementsMatchingText("property");
        for (Element propertyElement : properties) {
            String name = null;
            String value = null;
            String source = null;
            Elements children = propertyElement.children();
            for (Element childElement : children) {
                if (childElement.tagName().equals("name")) {
                    name = childElement.text();
                }
                else if (childElement.tagName().equals("value")) {
                    value = childElement.text();
                }
                else if (childElement.tagName().equals("source")) {
                    source = childElement.text();
                }
                // Ignore other elements
            }
            if (name != null) {
                configProperties.put(name, new ConfigProperty(name, value, source));
            }
            else {
                LOG.info(message("Unnamed property found: {}", propertyElement.toString()));
            }
        }
    }
    
    private static List<HTableDescriptor> obtainTableDescriptors(HBaseAdmin hBaseAdmin) throws FatalInitializationException {
        try {
            HTableDescriptor[] tableDescriptors = hBaseAdmin.getTableDescriptors(Collections.<String>emptyList());
            return Lists.newArrayList(tableDescriptors);
        } catch (IOException e) {
            // Throw this, since the cluster is presumably in a fairly bad state if this happens
            throw new FatalInitializationException(message("Could not get the table descriptors from the HBase cluster. " +
                    "The message was {}", e.getMessage()), e);
        }
    }

}
