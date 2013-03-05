package com.hbasetmp.hadvisor.advisor;

import java.util.Collections;

public class Unknown extends Advice {

	public Unknown(String message) {
		super(AdviceType.UNKNOWN, Collections.singletonList(new AdviceMessage(message)));
	}

}
