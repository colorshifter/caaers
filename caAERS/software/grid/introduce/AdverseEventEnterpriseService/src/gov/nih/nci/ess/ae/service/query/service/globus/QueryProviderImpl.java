package gov.nih.nci.ess.ae.service.query.service.globus;

import gov.nih.nci.ess.ae.service.query.service.QueryImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the AdverseEventEnterpriseServiceImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.3
 * 
 */
public class QueryProviderImpl{
	
	QueryImpl impl;
	
	public QueryProviderImpl() throws RemoteException {
		impl = new QueryImpl();
	}
	

    public gov.nih.nci.ess.ae.service.query.stubs.FindAdverseEventsResponse findAdverseEvents(gov.nih.nci.ess.ae.service.query.stubs.FindAdverseEventsRequest params) throws RemoteException, gov.nih.nci.ess.ae.service.management.stubs.types.AdverseEventServiceException {
    gov.nih.nci.ess.ae.service.query.stubs.FindAdverseEventsResponse boxedResult = new gov.nih.nci.ess.ae.service.query.stubs.FindAdverseEventsResponse();
    boxedResult.setAdverseEvent(impl.findAdverseEvents(params.getAdverseEvent().getAdverseEvent()));
    return boxedResult;
  }

    public gov.nih.nci.ess.ae.service.query.stubs.GetAdverseEventDataResponse getAdverseEventData(gov.nih.nci.ess.ae.service.query.stubs.GetAdverseEventDataRequest params) throws RemoteException, gov.nih.nci.ess.ae.service.management.stubs.types.AdverseEventServiceException {
    gov.nih.nci.ess.ae.service.query.stubs.GetAdverseEventDataResponse boxedResult = new gov.nih.nci.ess.ae.service.query.stubs.GetAdverseEventDataResponse();
    boxedResult.setAdverseEvent(impl.getAdverseEventData(params.getAdverseEventIdentifier().getId()));
    return boxedResult;
  }

}