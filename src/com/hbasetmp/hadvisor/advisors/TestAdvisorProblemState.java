package com.hbasetmp.hadvisor.advisors;

import java.util.Collection;

import com.hbasetmp.hadvisor.advisor.Advice;
import com.hbasetmp.hadvisor.advisor.AdviceMessage;
import com.hbasetmp.hadvisor.advisor.AdvisorProperty;
import com.hbasetmp.hadvisor.advisor.ProblemState;
import com.hbasetmp.hadvisor.context.AdviceContext;
import com.hbasetmp.hadvisor.context.InitializationContext;

/**
 * Returns a problem state
 */
public class TestAdvisorProblemState extends BaseTestAdvisor {

    public void initialize(InitializationContext context) {
    }

    @Override
    public Advice getAdvice(AdviceContext context) {
        return new ProblemState(new AdviceMessage("This is a problem state"));
    }

    @Override
    public Collection<AdvisorProperty<?>> getConfigProperties() {
        return null;
    }

}
