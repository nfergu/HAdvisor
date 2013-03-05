package com.hbasetmp.hadvisor.advisor;

import java.util.Collections;
import java.util.List;


public class ProblemState extends Advice {

	public ProblemState(AdviceMessage message) {
		super(AdviceType.PROBLEM, Collections.singletonList(message));
	}
	
	public ProblemState(List<AdviceMessage> messages) {
	    super(AdviceType.PROBLEM, messages);
	}

}
