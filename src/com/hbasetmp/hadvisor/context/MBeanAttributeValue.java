package com.hbasetmp.hadvisor.context;

import javax.management.MBeanAttributeInfo;

public class MBeanAttributeValue {

	private final MBeanAttributeInfo attributeInfo;
	private final Object attributeValue;

	public MBeanAttributeValue(MBeanAttributeInfo attributeInfo, Object attributeValue) {
		this.attributeInfo = attributeInfo;
		this.attributeValue = attributeValue;
	}

	public MBeanAttributeInfo getAttributeInfo() {
		return attributeInfo;
	}

	public Object getAttributeValue() {
		return attributeValue;
	}
	
}
