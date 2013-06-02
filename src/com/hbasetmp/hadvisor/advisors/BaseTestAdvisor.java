package com.hbasetmp.hadvisor.advisors;

import com.hbasetmp.hadvisor.advisor.Advisor;
import com.hbasetmp.hadvisor.context.HAdvisorVersion;
import com.hbasetmp.hadvisor.context.Version;

public abstract class BaseTestAdvisor implements Advisor {

    @Override
    public String getDisplayName() {
        return getClass().getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Test advisor";
    }

    @Override
    public Version getVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public String url() {
        return "www.test.com/test";
    }

    @Override
    public HAdvisorVersion getEarliestKnownCompatibleVersion() {
        return HAdvisorVersion.V0_1_1;
    }

}
