package com.hbasetmp.hadvisor.advisors;

import java.util.Collection;
import java.util.Collections;

import com.hbasetmp.hadvisor.advisor.Advice;
import com.hbasetmp.hadvisor.advisor.AdvisorProperty;
import com.hbasetmp.hadvisor.advisor.ProblemEvent;
import com.hbasetmp.hadvisor.advisor.ProblemEvents;
import com.hbasetmp.hadvisor.context.AdviceContext;
import com.hbasetmp.hadvisor.context.InitializationContext;

/**
 * Returns a problem event
 */
public class TestAdvisorProblemEvent extends BaseTestAdvisor {

    public void initialize(InitializationContext context) {
    }

    @Override
    public Advice getAdvice(AdviceContext context) {
        return new ProblemEvents(Collections.singletonList(new ProblemEvent("This is a problem event")));
    }

    @Override
    public Collection<AdvisorProperty<?>> getConfigProperties() {
        return null;
    }
    
}
