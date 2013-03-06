/*******************************************************************************
 * Copyright SemanticBits, Northwestern University and Akaza Research
 * 
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caaers/LICENSE.txt for details.
 ******************************************************************************/
package gov.nih.nci.cabig.caaers.web.admin;

import gov.nih.nci.cabig.caaers.domain.Agent;
import gov.nih.nci.cabig.caaers.domain.AgentSpecificTerm;
import gov.nih.nci.cabig.ctms.web.tabs.Flow;
import gov.nih.nci.cabig.ctms.web.tabs.FlowFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/*
* @author Ion C. Olaru
*
* */

public class AgentCreateController extends AgentController {

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        super.formBackingObject(request);
        AgentCommand c = new AgentCommand();
        c.setAgentSpecificTerms(new ArrayList<AgentSpecificTerm>());
        c.setAgent(new Agent());
        return c;
    }

    @Override
	public FlowFactory<AgentCommand> getFlowFactory() {
		return new FlowFactory<AgentCommand>() {
			public Flow<AgentCommand> createFlow(AgentCommand cmd) {
				Flow<AgentCommand> flow = new Flow<AgentCommand>("Agents");
				flow.addTab(new AgentTab<AgentCommand>("Agent ", "Agent", "admin/agentCreateForm"));
				return flow;
			}
		};
	}

}
