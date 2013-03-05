package com.hbasetmp.hadvisor;

import org.slf4j.Logger;

public class AdvisorWarnings {

	public static void warn(String message) {
		warn(message, null);
	}

	public static void warn(String message, Logger logger) {
		if (logger != null) {
			logger.warn(message);
		}
	}
	
}
