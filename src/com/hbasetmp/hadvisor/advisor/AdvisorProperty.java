package com.hbasetmp.hadvisor.advisor;

import com.hbasetmp.hadvisor.context.AdvisorPropertyName;

public class AdvisorProperty<T> implements AdvisorPropertyName<T> {

	private final String propertyName;
	private final T defaultValue;
	private final String description;
    private final Class<T> type;
	
	public AdvisorProperty(String propertyName, Class<T> type, T defaultValue, String description) {
		this.propertyName = propertyName;
		this.type = type;
		this.defaultValue = defaultValue;
		this.description = description;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class<T> getType() {
	    return type;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public String getDescription() {
		return description;
	}

    @Override
    public String toString() {
        return "AdvisorProperty [propertyName=" + propertyName + ", defaultValue=" + defaultValue + ", description=" + description + ", type=" + type
                + "]";
    }

}
