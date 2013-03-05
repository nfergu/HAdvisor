package com.hbasetmp.hadvisor.advisor;

import java.util.Collections;
import java.util.List;

import com.hbasetmp.hadvisor.context.HBaseService;

public class AdviceMessage {

	private final String message;
	private final List<HBaseService> affectsServers;
	
	public AdviceMessage(String message) {
		this(message, Collections.<HBaseService>emptyList());
	}
	
	public AdviceMessage(String message, List<HBaseService> affectsServers) {
		this.message = message;
		this.affectsServers = affectsServers;
	}

	public String getMessage() {
		return message;
	}

	public List<HBaseService> getAffectsServers() {
		return affectsServers;
	}

}
