package com.hbasetmp.hadvisor.advisor;

import java.util.Collection;
import java.util.List;

import com.hbasetmp.hadvisor.context.AdviceContext;

/**
 * <p>Represents advice about a particular aspect of an HBase cluster, as returned by an {@link Advisor}.
 * Advice is always of a particular type (represented by {@link AdviceType}, as follows:
 * 
 * <ul>
 *  <li>OK: No problems were detected with this aspect of the HBase cluster.
 *  <li>PROBLEM: One or more problems were detected with this aspect of the cluster. The cluster can be in a
 *  problematic <i>state</i>, or problematic <i>events</i> can have occurred. See
 *  {@link Advisor#getAdvice(AdviceContext)} for further explanation.
 *  <li>UNKNOWN: The advisor was unable to provide any advice.
 * </ul>
 * 
 * @see Advisor#getAdvice(AdviceContext)
 */
public abstract class Advice {

	private final AdviceType adviceType;
	private final List<AdviceMessage> messages;
	private final List<ProblemEvent> problemEvents;

	protected Advice(AdviceType adviceType, List<AdviceMessage> messages) {
		this.adviceType = adviceType;
		this.messages = messages;
		this.problemEvents = null;
	}
	
	protected Advice(List<ProblemEvent> problemEvents) {
		this.problemEvents = problemEvents;
		this.messages = null;
		this.adviceType = AdviceType.PROBLEM;
	}

	public AdviceType getAdviceType() {
		return adviceType;
	}
	
	public List<AdviceMessage> getMessages() {
        return messages;
    }

    public Collection<ProblemEvent> getProblemEvents() {
		return problemEvents;
	}
	
}
