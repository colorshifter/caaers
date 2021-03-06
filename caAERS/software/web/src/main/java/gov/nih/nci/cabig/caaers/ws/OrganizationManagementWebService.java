/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.ws;



import gov.nih.nci.cabig.caaers.integration.schema.organization.Organizations;
import gov.nih.nci.cabig.caaers.ws.faults.SecurityExceptionFaultMessage;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Organization Creation and Update.
 * @author Ramakrishna
 *
 */

@WebService(name="OrganizationServiceInterface",targetNamespace="http://schema.integration.caaers.cabig.nci.nih.gov/common")
public interface OrganizationManagementWebService {
	
	/**
	 * This operation will accept jaxb Organizations and either saves or updates it.
	 * @TODO
	 * This operation should allow for response.
	 * Need to modify schema for Response
	 * @param xmlOrganizations
	 */
	@WebMethod
	public gov.nih.nci.cabig.caaers.integration.schema.common.CaaersServiceResponse createOrUpdateOrganization(@WebParam(name="Organizations",
            targetNamespace="http://schema.integration.caaers.cabig.nci.nih.gov/common") Organizations xmlOrganizations) throws SecurityExceptionFaultMessage;
	
	
	/**
	 * This operation will accept jaxb Organizations and a merged organization.
	 * @TODO
	 * This operation should allow for response.
	 * Need to modify schema for Response
	 * @param xmlOrganizations
	 */
	@WebMethod
	public gov.nih.nci.cabig.caaers.integration.schema.common.CaaersServiceResponse mergeOrganization(@WebParam(name="Organizations",
            targetNamespace="http://schema.integration.caaers.cabig.nci.nih.gov/common") Organizations xmlOrganizations) throws SecurityExceptionFaultMessage;


}
