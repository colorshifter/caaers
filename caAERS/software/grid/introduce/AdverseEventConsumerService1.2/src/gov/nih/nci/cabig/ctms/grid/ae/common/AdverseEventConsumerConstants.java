/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.ctms.grid.ae.common;

import javax.xml.namespace.QName;


public interface AdverseEventConsumerConstants {
	public static final String SERVICE_NS = "http://ae.grid.ctms.cabig.nci.nih.gov/AdverseEventConsumer";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "AdverseEventConsumerKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "AdverseEventConsumerResourceProperties");

	//Service level metadata (exposed as resouce properties)
	
}
