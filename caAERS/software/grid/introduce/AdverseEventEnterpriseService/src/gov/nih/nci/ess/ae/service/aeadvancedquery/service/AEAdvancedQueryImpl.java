package gov.nih.nci.ess.ae.service.aeadvancedquery.service;

import gov.nih.nci.ess.ae.service.aeadvancedquery.common.AEAdvancedQueryI;
import gov.nih.nci.ess.ae.service.common.AdverseEventEnterpriseServiceI;
import gov.nih.nci.ess.ae.service.misc.SpringContextHolder;

import java.rmi.RemoteException;

import org.globus.wsrf.config.ContainerConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO:I am the service side implementation class. IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class AEAdvancedQueryImpl extends AEAdvancedQueryImplBase {

	private static final String BEAN_NAME = "adverseEventAdvancedQueryImpl";

	private AEAdvancedQueryI advancedQueryI;

	public AEAdvancedQueryImpl() throws RemoteException {
		super();
		ApplicationContext ctx = getApplicationContext();
		advancedQueryI = (AEAdvancedQueryI) ctx.getBean(BEAN_NAME);
	}

	/**
	 * @return
	 * @throws BeansException
	 */
	private ApplicationContext getApplicationContext() throws BeansException {
		ApplicationContext ctx = null;
		String exp = ContainerConfig
				.getConfig()
				.getOption(
						AdverseEventEnterpriseServiceI.SPRING_CLASSPATH_EXPRESSION,
						AdverseEventEnterpriseServiceI.DEFAULT_SPRING_CLASSPATH_EXPRESSION);
		if (SpringContextHolder.getApplicationContext() == null) {
			ctx = new ClassPathXmlApplicationContext(exp);
			SpringContextHolder.setApplicationContext(ctx);
		} else {
			ctx = SpringContextHolder.getApplicationContext();
		}
		return ctx;
	}

	public ess.caaers.nci.nih.gov.AdverseEvent[] findAdverseEvents(
			ess.caaers.nci.nih.gov.AdverseEventQuery adverseEventQuery,
			ess.caaers.nci.nih.gov.LimitOffset limitOffset)
			throws RemoteException,
			gov.nih.nci.ess.ae.service.management.stubs.types.AdverseEventServiceException {
		return advancedQueryI
				.findAdverseEvents(adverseEventQuery, limitOffset);
	}

}
