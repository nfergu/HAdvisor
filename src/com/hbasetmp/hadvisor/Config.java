package com.hbasetmp.hadvisor;

import static com.hbasetmp.hadvisor.util.Util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hbasetmp.hadvisor.advisor.AdvisorProperty;


public class Config {

	private static final Logger LOG = LoggerFactory.getLogger(Config.class);
	
	public static String getString(AdvisorProperty<String> property) {
		String value = System.getProperty(property.getPropertyName());
		if (value == null) {
			value = property.getDefaultValue();
		}
		return value;
	}

	public static int getInt(AdvisorProperty<Integer> property) {
		String value = System.getProperty(property.getPropertyName());
		int returnValue;
		if (value == null) {
			returnValue = property.getDefaultValue();
		}
		else {
			try {
				returnValue = Integer.parseInt(value);
			}
			catch (NumberFormatException e) {
				LOG.warn(message("Could not parse value {} for property {} as an integer", value, property.toString()));
				returnValue = property.getDefaultValue();
			}
		}
		return returnValue;
	}
	
}
