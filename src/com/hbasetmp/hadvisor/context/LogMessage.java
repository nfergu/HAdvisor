package com.hbasetmp.hadvisor.context;

import org.apache.hadoop.hbase.ServerName;

public class LogMessage {

	private ServerName serverName;
	private final String message;

	public LogMessage(ServerName serverName, String message) {
		this.serverName = serverName;
		this.message = message;
	}

	public ServerName getServerName() {
		return serverName;
	}

	public String getMessage() {
		return message;
	}
	
}
