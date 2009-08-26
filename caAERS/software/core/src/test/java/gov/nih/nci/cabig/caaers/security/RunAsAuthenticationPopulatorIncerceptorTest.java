package gov.nih.nci.cabig.caaers.security;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.acegisecurity.context.SecurityContextHolder;
import org.aopalliance.intercept.MethodInvocation;

import gov.nih.nci.cabig.caaers.CaaersTestCase;

/**
 * 
 * @author Biju Joseph
 *
 */
public class RunAsAuthenticationPopulatorIncerceptorTest extends CaaersTestCase {
	
	RunAsAuthenticationPopulatorIncerceptor interceptor;
	
	protected void setUp() throws Exception {
		super.setUp();
		interceptor = (RunAsAuthenticationPopulatorIncerceptor) getDeployedApplicationContext().getBean("runAsAutenticationProviderInterceptor");
	}

	public void testInvoke() throws Exception{

		MethodInvocation method = new MethodInvocation(){

			public Method getMethod() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object[] getArguments() {
				// TODO Auto-generated method stub
				return null;
			}

			public AccessibleObject getStaticPart() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object getThis() {
				// TODO Auto-generated method stub
				return null;
			}

			public Object proceed() throws Throwable {
				return "SYSTEM".equals(SecurityUtils.getUserLoginName());
			}
			
		};
		
		
		Object retVal = interceptor.invoke(method);
		assertNotNull(retVal);
		assertEquals(Boolean.TRUE, retVal);
		
	}

}
