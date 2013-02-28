/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.ccts.grid.service;

import gov.nih.nci.ccts.grid.common.LabConsumerServiceI;

import java.rmi.RemoteException;

import org.globus.wsrf.config.ContainerConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class LabConsumerServiceImpl extends LabConsumerServiceImplBase {

	
	private static final String SPRING_CLASSPATH_EXPRESSION = "springClasspathExpression";

    private static final String DEFAULT_SPRING_CLASSPATH_EXPRESSION = "classpath:applicationContext-lab.xml";

	private static final String LAB_CONSUMER_BEAN_NAME = "labConsumerBeanName";

	private static final String DEFAULT_LAB_CONSUMER_BEAN_NAME = "labConsumer";
	
	private LabConsumerServiceI consumer;
	    
	public LabConsumerServiceImpl() throws RemoteException {
		super();
        String exp = ContainerConfig.getConfig().getOption(SPRING_CLASSPATH_EXPRESSION,DEFAULT_SPRING_CLASSPATH_EXPRESSION);
        String bean = ContainerConfig.getConfig().getOption(LAB_CONSUMER_BEAN_NAME,DEFAULT_LAB_CONSUMER_BEAN_NAME);
        ApplicationContext ctx = new ClassPathXmlApplicationContext(exp);
        consumer = (LabConsumerServiceI) ctx.getBean(bean);
	}

  public gov.nih.nci.cabig.ccts.domain.loadlabs.Acknowledgement loadLabs(gov.nih.nci.cabig.ccts.domain.loadlabs.LoadLabsRequest loadLabsRequest) throws RemoteException {
    //TODO: Implement this autogenerated method
    //throw new RemoteException("Not yet implemented");
	  return consumer.loadLabs(loadLabsRequest);
  }

}

