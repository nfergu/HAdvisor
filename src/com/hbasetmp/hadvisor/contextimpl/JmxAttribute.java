/**
 * Copyright (C) 2010 Causata
 */
package com.hbasetmp.hadvisor.contextimpl;

public class JmxAttribute {   
    
    private final String mBeanNameSubstring;
    private final String attributeName;

    public JmxAttribute(String mBeanNameSubstring, String attributeName) {
        this.mBeanNameSubstring = mBeanNameSubstring;
        this.attributeName = attributeName;
    }

    public String getMBeanNameSubString() {
        return mBeanNameSubstring;
    }

    public String getAttributeName() {
        return attributeName;
    }
    
}
