package com.hbasetmp.hadvisor.contextimpl;

import static com.google.common.collect.Maps.*;
import static com.hbasetmp.hadvisor.util.Util.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Subscriptions {
    
    private final Collection<JmxAttribute> subscribedJmxAttributes;
    private final Map<String, List<JmxAttribute>> subscribedJmxAttributeMap = newHashMap();

    public Subscriptions(Collection<JmxAttribute> subscribedJmxAttributes) {
        this.subscribedJmxAttributes = subscribedJmxAttributes;
        for (JmxAttribute jmxAttribute : subscribedJmxAttributes) {
            putArrayListItem(subscribedJmxAttributeMap, jmxAttribute.getMBeanNameSubString(), jmxAttribute);
        }
    }

    public Collection<JmxAttribute> getSubcribedJmxAttributes() {
        return subscribedJmxAttributes;
    }
    
    public Collection<JmxAttribute> getSubcribedJmxAttributeMap() {
        return subscribedJmxAttributes;
    }
    
}
