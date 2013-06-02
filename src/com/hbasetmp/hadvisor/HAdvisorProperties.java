package com.hbasetmp.hadvisor;

import com.hbasetmp.hadvisor.advisor.AdvisorProperty;
import com.hbasetmp.hadvisor.util.Util;

public class HAdvisorProperties {

    private static final String PREFIX = "hadvisor.";
    
    public static final AdvisorProperty<Integer> CHECK_FREQUENCY = new AdvisorProperty<Integer>(
            getProperty("check.frequency"), Integer.class, 5,
            "How often to perform advice checks, in minutes.");

    public static final AdvisorProperty<Integer> MASTER_JMX_PORT = new AdvisorProperty<Integer>(
            getProperty("master.jmx.port"), Integer.class, 10101,
            "The port on which the JMX server listens on the HBase Master. This is typically configured using the " +
            "com.sun.management.jmxremote.port system property on the server.");
    
    public static final AdvisorProperty<Integer> REGION_SERVER_JMX_PORT = new AdvisorProperty<Integer>(
            getProperty("regionserver.jmx.port"), Integer.class, 10102,
            "The port on which the JMX server listens on the HBase Master. This is typically configured using the " +
            "com.sun.management.jmxremote.port system property on the server.");

    public static final AdvisorProperty<Integer> MASTER_WEB_UI_PORT = new AdvisorProperty<Integer>(
            getProperty("master.web.ui.port"), Integer.class, 60010,
            "The port on which the master serves its Web UI. This can be configured using the hbase.master.info.port" +
            "system property on the server.");

    public static final AdvisorProperty<Integer> REGION_SERVER_WEB_UI_PORT = new AdvisorProperty<Integer>(
            getProperty("regionserver.web.ui.port"), Integer.class, 60030,
            "The port on which the region server serves its Web UI. This can be configured using the hbase.regionserver.info.port" +
            "system property on the server.");

    public static final AdvisorProperty<String> WEB_UI_CONFIG_PATH = new AdvisorProperty<String>(
            getProperty("web.ui.config.path"), String.class, "conf",
            "The config path that the web UI serves the HBase configuration on. There should normally be no need to change this.");
    
    // For maximum log tail throughput (on heavily-loaded region server):
    // RS: 68,635 bytes per minute
    // GC: 15,691 bytes per minute

    private static final AdvisorProperty<?>[] allProperties = new AdvisorProperty<?>[] {
        MASTER_JMX_PORT,
        REGION_SERVER_JMX_PORT
    };

    public static AdvisorProperty<?>[] getAllproperties() {
        return allProperties;
    }

    public static String getJmxPropertiesString() {
        return Util.csvList(MASTER_JMX_PORT.getPropertyName(), REGION_SERVER_JMX_PORT.getPropertyName());
    }
    
    private static String getProperty(String suffix) {
        return PREFIX + suffix;
    }
    
}
