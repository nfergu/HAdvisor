package com.hbasetmp.hadvisor.advisor;

import com.hbasetmp.hadvisor.context.HBaseService;

public class ProblemEvent {

	private final String message;
	private final HBaseService affectsServer;

	public ProblemEvent(String message, HBaseService affectsServer) {
		this.message = message;
		this.affectsServer = affectsServer;
		
	}

	public String getMessage() {
		return message;
	}

	public HBaseService getAffectsServer() {
		return affectsServer;
	}
	
}
