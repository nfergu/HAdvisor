package com.hbasetmp.hadvisor.context;

public enum SubscriptionItem {

    /**
     * Subscription for the additional region info, as returned by {@link RegionDetails#getAdditionalRegionInfo()}.
     * Note that the HAdvisor coprocessors must be installed in HBase for this method to return data.
     */
    ADDITIONAL_REGION_INFO,
    
    /**
     * Subscription for the HLog info, as returned by the {@link SnapshotData#getHLogInfo()}.
     * Note that the HAdvisor coprocessors must be installed in HBase for this method to return data.
     */
    HLOG_INFO,
    
    /**
     * Subscription for the filesystem info, as returned by {@link SnapshotData#getFileSystemInfo()}.
     * Note that the HAdvisor coprocessors must be installed in HBase for this method to return data.
     */
    FILESYSTEM_INFO,
    
}
