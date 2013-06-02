package com.hbasetmp.hadvisor.advisor;

import java.util.Collection;

import com.hbasetmp.hadvisor.context.AdviceContext;
import com.hbasetmp.hadvisor.context.AdvisorPropertyName;
import com.hbasetmp.hadvisor.context.BaseContext;
import com.hbasetmp.hadvisor.context.HAdvisorVersion;
import com.hbasetmp.hadvisor.context.InitializationContext;
import com.hbasetmp.hadvisor.context.Version;

/**
 * <p>Provides advice about a particular aspect of an HBase cluster. Implementors should implement the 
 * {@link #getAdvice(AdviceContext)} method to provide advice. This method is called periodically, when 
 * some advice is required. When an advisor is initialized, the {@link #initialize(InitializationContext)}
 * method is called.
 */
public interface Advisor {

	/**
	 * Implementors should implement this method to initialize the advisor, and add new subscriptions for data, 
	 * which will be available the next time the {@link #getAdvice(AdviceContext)} method is called.
	 */
	public void initialize(InitializationContext context);
	
	/**
	 * <p>Provides advice on a specific aspect of an HBase cluster. Implementors should return a specific
	 * subclass of {@link Advice}, depending on the type of advice they are giving, as follows:
     * 
     * <ul>
     *  
     *  <li>{@link OK}: No problems were detected with this aspect of the HBase cluster.
     *  
     *  <li>{@link ProblemState}: One or more services in the cluster are in a problem <i>state</i>. 
     *  This means that there is a persistent problem with the cluster. For example, if the number of regions on one 
     *  of the region servers increases above an acceptable threshold, then the cluster has entered a problem state. 
     *  The cluster will remain in this state until the advisor returns {@link OK} in the future.
     *  
     *  <li>{@link ProblemEvents}: One or more services in the cluster have had problem <i>event</i>s. 
     *  This means that individual events have occurred that represent a problem for the cluster, but these problems 
     *  are not necessarily long-lived. For example, if a particular type of unexpected error has occurred in the 
     *  cluster then the advisor may determine that a problematic event has occurred.
 	 *  
 	 *  <li>{@link Unknown}: The advisor was unable to provide any advice. This may be because some information is not 
 	 *  available from the HBase cluster (possibly due to configuration), or because one or more required services are 
 	 *  down.
 	 *  
     * </ul>
	 */
	public Advice getAdvice(AdviceContext context);
	
	/**
	 * Returns a collection of all of the properties that this {@link Advisor} uses. These properties will be
	 * available from {@link BaseContext#getPropertyValue(AdvisorPropertyName)} when the 
	 * {@link #initialize(InitializationContext)} or {@link #getAdvice(AdviceContext)} methods are called. 
	 */
	public Collection<AdvisorProperty<?>> getConfigProperties();

	/**
	 * Gets the display name of the advisor. This is the name that will be displayed to the user.
	 */
	public String getDisplayName();
	
	/**
	 * Gets a description of the purpose of this advisor, including the advice that it provides and the data
	 * that it bases this advice on.
	 */
	public String getDescription();
	
	/**
	 * Returns the version of this advisor.
	 */
	public Version getVersion();
	
	/**
	 * A URL containing further information about this advisor. Can be <tt>null</tt>.
	 */
	public String url();

	/**
	 * <p>Returns the earliest known version of HAdvisor that this advisor is compatible (this is typically the
	 * version that this advisor was compiled against, but could be any earlier version if compatibility against
	 * that version is known).
	 * 
	 * <p>The easiest way to find the appropriate version is to run the main method of {@link HAdvisorVersion}
	 */
	public HAdvisorVersion getEarliestKnownCompatibleVersion();
	
}
