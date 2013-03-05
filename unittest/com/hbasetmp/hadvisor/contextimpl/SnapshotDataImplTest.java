package com.hbasetmp.hadvisor.contextimpl;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.MiniHBaseCluster;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hbasetmp.hadvisor.exception.FatalInitializationException;

public class SnapshotDataImplTest {

    private static MiniHBaseCluster miniCluster;
    private static Configuration configuration;
    private static HBaseAdmin hBaseAdmin;
    private static SnapshotDataImpl snapshotDataImpl;

    @BeforeClass
    public static void setupAll() throws Exception {
        HBaseTestingUtility hBaseTestingUtility = new HBaseTestingUtility();
        miniCluster = hBaseTestingUtility.startMiniCluster(2, 4);
        configuration = miniCluster.getConfiguration();
        hBaseAdmin = new HBaseAdmin(configuration);
        snapshotDataImpl = new SnapshotDataImpl(
                DateTime.now(), hBaseAdmin, new Subscriptions(Collections.<JmxAttribute>emptyList()));
    }
    
    @Test
    public void testGetClusterStatus() throws FatalInitializationException {
        ClusterStatus clusterStatus = snapshotDataImpl.getClusterStatus();
        Collection<ServerName> servers = clusterStatus.getServers();
        assertEquals(4, servers.size());
    }

}
