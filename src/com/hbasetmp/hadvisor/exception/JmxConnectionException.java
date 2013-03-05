package com.hbasetmp.hadvisor.exception;

import static com.hbasetmp.hadvisor.util.Util.*;

import com.hbasetmp.hadvisor.HAdvisorProperties;
import com.hbasetmp.hadvisor.Urls;

public class JmxConnectionException extends Exception {

	private static final long serialVersionUID = 4323386618786436461L;

	public JmxConnectionException(String hostname, int port, Exception underlyingException) {
		super(message("Cannot connect to the JMX server using the host name {} and the port {}. " +
				"The message from the JMX client is {}. Please make sure you have followed the instructions for using JMX with HBase " +
				"(available at {}), and set the JMX config properties ({}) to the correct values.",
				hostname, String.valueOf(port), underlyingException.getMessage(),
				Urls.HBASE_JMX_SETUP, HAdvisorProperties.getJmxPropertiesString()), 
				underlyingException);
	}

}
