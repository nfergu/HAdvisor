package com.hbasetmp.hadvisor.advisor;

public class AdvisorPropertyValue<T> {

	private final String propertyName;
	private final T value;
	
	public AdvisorPropertyValue(String propertyName, T value) {
		this.propertyName = propertyName;
		this.value = value;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public T getValue() {
		return value;
	}
	
}
