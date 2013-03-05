/**
 * Copyright (C) 2010 Causata
 */
package com.hbasetmp.hadvisor.context;

import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.regionserver.compactions.CompactionRequest.CompactionState;

/**
 * Contains information about a region in HBase
 */
public class RegionDetails {

    private final HRegionInfo regionInfo;
    private final CompactionState compactionState;
    private final AdditionalRegionInfo additionalRegionInfo;

    public RegionDetails(HRegionInfo regionInfo, CompactionState compactionState, AdditionalRegionInfo additionalRegionInfo) {
        this.regionInfo = regionInfo;
        this.compactionState = compactionState;
        this.additionalRegionInfo = additionalRegionInfo;
    }

    /**
     * Gets the region info. This will always be available.
     */
    public HRegionInfo getRegionInfo() {
        return regionInfo;
    }

    /**
     * Gets the compaction state. This may be <tt>null</tt> if, for example, the region is not currently available
     */
    public CompactionState getCompactionState() {
        return compactionState;
    }

    /**
     * <p>Gets additional information about HBase Regions (other than that returned by {@link #getRegionInfo()}). 
     * 
     * <p>Note that the advisor must have subscribed to this data (by passing {@link SubscriptionItem#ADDITIONAL_REGION_INFO} to the 
     * {@link InitializationContext#subscribe(SubscriptionItem)} method), and the HAdvisor coprocessors must be installed in HBase for this method 
     * to return data (<tt>null</tt> is returned otherwise).
     */
    public AdditionalRegionInfo getSubscribedAdditionalRegionInfo() {
        return additionalRegionInfo;
    }
    
}
