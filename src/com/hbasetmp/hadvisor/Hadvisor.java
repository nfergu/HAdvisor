package com.hbasetmp.hadvisor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.joda.time.DateTime;

import com.google.common.collect.Range;
import com.hbasetmp.hadvisor.advisor.Advice;
import com.hbasetmp.hadvisor.advisor.Advisor;
import com.hbasetmp.hadvisor.advisor.AdvisorPropertyValue;
import com.hbasetmp.hadvisor.advisors.TestAdvisorOk;
import com.hbasetmp.hadvisor.advisors.TestAdvisorProblemEvent;
import com.hbasetmp.hadvisor.advisors.TestAdvisorProblemState;
import com.hbasetmp.hadvisor.advisors.TestAdvisorUnknown;
import com.hbasetmp.hadvisor.context.AdviceContext;
import com.hbasetmp.hadvisor.context.AdvisorPropertyName;
import com.hbasetmp.hadvisor.context.CurrentData;
import com.hbasetmp.hadvisor.context.SnapshotData;

public class HAdvisor {

	private final Advisor[] advisors = new Advisor[] {
	        new TestAdvisorOk(),
	        new TestAdvisorProblemState(),
	        new TestAdvisorProblemEvent(),
	        new TestAdvisorUnknown()
	};
	
	public static void main(String[] args) {
		new HAdvisor().go(args);
	}

	private void go(String[] args) {

	    if (args.length < 1) {
	        printUsageAndExit();
	    }
	    
		String configPath = args[0];
		
		Configuration conf = HBaseConfiguration.create();
	    File hbaseConfigPath = new File(configPath);
		if(hbaseConfigPath.exists()) {
			conf.addResource(new Path(hbaseConfigPath.getAbsolutePath()));
		}
		else {
		    System.err.println("Could not find HBase configuration file at location [" + hbaseConfigPath + "]");
		    System.exit(-1);
		}
		
		// Always do a check on startup, and then one every checkFrequency minutes
		int checkFrequency = Config.getInt(HAdvisorProperties.CHECK_FREQUENCY);
		ScheduledExecutorService mainExecutor = Executors.newScheduledThreadPool(1);
		mainExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                doCheck();
            }
		}, 0, checkFrequency, TimeUnit.MINUTES);
		
	}

	private void doCheck() {
	    Map<Class<?>, Advice> adviceMap = new HashMap<Class<?>, Advice>();
	    for (Advisor advisor : advisors) {
	        adviceMap.put(advisor.getClass(), advisor.getAdvice(getContext()));
	    }
	    persistAdvice(adviceMap);
    }
	
    private void persistAdvice(Map<Class<?>, Advice> adviceMap) {
        // TODO Next: Implement this
    }

    private AdviceContext getContext() {
        // TODO: Implement this
        return new AdviceContext() {
            @Override
            public void logWarning(String warningMessage) {

            }
            @Override
            public <T> AdvisorPropertyValue<T> getPropertyValue(AdvisorPropertyName<T> advisorProperty) {
                return null;
            }
            @Override
            public Range<DateTime> getSnapshotTimeRange() {
                return null;
            }
            @Override
            public List<SnapshotData> getSnapshotData() {
                return null;
            }
            @Override
            public CurrentData getCurrentData() {
                return null;
            }
        };
    }

    private void printUsageAndExit() {
        System.err.println("Usage: " + HAdvisor.class.getSimpleName() + " <hbasesitexmllocation>");
        System.exit(-1);
    }
	
}
