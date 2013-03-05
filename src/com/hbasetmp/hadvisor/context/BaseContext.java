package com.hbasetmp.hadvisor.context;

import com.hbasetmp.hadvisor.advisor.Advisor;
import com.hbasetmp.hadvisor.advisor.AdvisorPropertyValue;

public interface BaseContext {

	/**
	 * Gets the value of the specified property. The calling advisor must have advertised the fact that it uses
	 * this property by returning it from its {@link Advisor#getConfigProperties()} method,
	 * otherwise an {@link IllegalStateException} is thrown.
	 */
	public <T> AdvisorPropertyValue<T> getPropertyValue(AdvisorPropertyName<T> advisorProperty);

	/**
	 * Logs a warning, which is associated with the current advisor
	 */
	public void logWarning(String warningMessage);
	
}
