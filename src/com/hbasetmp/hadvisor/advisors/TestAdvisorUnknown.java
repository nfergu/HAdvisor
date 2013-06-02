package com.hbasetmp.hadvisor.advisors;

import java.util.Collection;

import com.hbasetmp.hadvisor.advisor.Advice;
import com.hbasetmp.hadvisor.advisor.AdvisorProperty;
import com.hbasetmp.hadvisor.advisor.Unknown;
import com.hbasetmp.hadvisor.context.AdviceContext;
import com.hbasetmp.hadvisor.context.InitializationContext;

/**
 * Returns an "unknown" state
 */
public class TestAdvisorUnknown extends BaseTestAdvisor {

    public void initialize(InitializationContext context) {
    }

    @Override
    public Advice getAdvice(AdviceContext context) {
        return new Unknown("Unknown advice");
    }

    @Override
    public Collection<AdvisorProperty<?>> getConfigProperties() {
        return null;
    }

}
