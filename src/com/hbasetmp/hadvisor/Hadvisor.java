package com.hbasetmp.hadvisor;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.hbasetmp.hadvisor.advisor.Advisor;

public class Hadvisor {

	private final Advisor[] advisors = new Advisor[] {
	};
	
	public static void main(String[] args) {
		new Hadvisor().go(args);
	}

	private void go(String[] args) {

		String configPath = args[0];
		
		Configuration conf = HBaseConfiguration.create();
	    File hbaseConfigPath = new File(configPath);
		if(hbaseConfigPath.exists()) {
			conf.addResource(new Path(hbaseConfigPath.getAbsolutePath()));
		}
		
	}
	
}
