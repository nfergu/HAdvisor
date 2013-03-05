package com.hbasetmp.hadvisor.context;

public class ConfigProperty {

	private final String name;
	private final String value;
	private final String source;

	public ConfigProperty(String name, String value, String source) {
		this.name = name;
		this.value = value;
		this.source = source;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getSource() {
		return source;
	}
	
}
