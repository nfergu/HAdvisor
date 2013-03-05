package com.hbasetmp.hadvisor.advisor;

import java.util.Collections;

public class OK extends Advice {

	public OK(String message) {
		super(AdviceType.OK, Collections.singletonList(new AdviceMessage(message)));
	}

}
