package com.hbasetmp.hadvisor.advisors;

import java.util.Collection;

import com.hbasetmp.hadvisor.advisor.Advice;
import com.hbasetmp.hadvisor.advisor.AdvisorProperty;
import com.hbasetmp.hadvisor.advisor.OK;
import com.hbasetmp.hadvisor.context.AdviceContext;
import com.hbasetmp.hadvisor.context.InitializationContext;

/**
 * Returns OK
 */
public class TestAdvisorOk extends BaseTestAdvisor {

    @Override
    public void initialize(InitializationContext context) {
    }

    @Override
    public Advice getAdvice(AdviceContext context) {
        return new OK();
    }

    @Override
    public Collection<AdvisorProperty<?>> getConfigProperties() {
        return null;
    }

}
