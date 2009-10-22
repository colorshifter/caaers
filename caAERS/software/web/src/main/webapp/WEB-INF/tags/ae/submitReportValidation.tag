<%-- 
	This page renders the report validation on the submit page in the aeReport flow.
	It was moved to this tag to support refreshing this part on the page once the physician signs-off on the
	submit page. 
	Author : Sameer Sawant.
--%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div id="report-validation-section">
	<c:forEach items="${command.aeReport.reports}" varStatus="status" var="report">
		<c:if test="${report.status ne 'WITHDRAWN' and report.status ne 'REPLACED' and report.status ne 'AMENDED' and report.status ne 'COMPLETED'}">
			<chrome:division collapsable="true" title="${report.reportDefinition.label}" id="division-${report.id}">
				<div class="row">
					<div class="leftpanel">
                        <div class="row">
                            <div class="label">
                                Status
                            </div>
                            <div class="value">
                                <c:if test="${report.lastVersion.reportStatus == 'PENDING'}">
                                    <span class="dueOn">
                                        <c:if test="${not empty report.lastVersion.dueOn}">
                                            <i>Due on</i>
                                            <b><tags:formatDate value="${report.lastVersion.dueOn}" /></b>
                                        </c:if>
                                        <c:if test="${ empty report.lastVersion.dueOn}">
                                            <i>Amendment Due</i>
                                        </c:if>
                                    </span>
                                </c:if>
                                <c:if test="${report.lastVersion.reportStatus == 'WITHDRAWN'}">
                                    <span class="submittedOn"><i>Withdrawn</i>
                                        <br>
                                        <b><tags:formatDate value="${report.lastVersion.withdrawnOn}" /></b>
                                    </span>
                                </c:if>
                                <c:if test="${report.lastVersion.reportStatus == 'COMPLETED'}">
                                    <span class="submittedOn"><i>Submitted on </i>
                                        <br>
                                        <b><tags:formatDate value="${report.lastVersion.submittedOn}" /></b>
                                    </span>
                                </c:if>
                                <c:if test="${report.lastVersion.reportStatus == 'FAILED'}">
                                    <span class="dueOn"><i>Submission to AdEERS failed </i></span>
                                </c:if>
                                <c:if test="${report.lastVersion.reportStatus == 'INPROCESS'}">
                                    <span class="dueOn"><i>Submission to AdEERS in process</i></span>
                                </c:if>
                            </div>
                        </div>
					</div>
					<div class="rightpanel">
						<c:if test="${report.reportDefinition.amendable == true}">
							<div class="row">
								<div class="label">
									Amendment #
								</div>
								<div class="value">
									${report.lastVersion.amendmentNumber}
								</div>
							</div>
						</c:if>
					</div>
				</div>
                <c:choose>
                    <c:when test="${reportMessages[report.id].submittable}">
                        <div class="row" style="margin-left:102px; background-color:#C8FFBF; padding:10px; width:500px; font-weight:bolder;">
                                <img src="<chrome:imageUrl name="../buttons/button_icons/check_icon.png"/>" alt="" style="vertical-align:middle; margin-right:10px;" /> Ready to submit!
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row" style="margin-left:102px; background-color:#FFDFDF; padding:10px; width:500px;">
                            <h3>
                                Information remaining to complete
                            </h3>
                            
                                <ul>
                                    <c:forEach items="${reportMessages[report.id].messages}" var="sectionEntry">
                                        <li>
                                            ${sectionEntry.key.displayName} section
                                        </li>
                                    </c:forEach>
                                </ul>
                            
                        </div>
                    </c:otherwise>
                </c:choose>
			</chrome:division>
		</c:if>
	</c:forEach>
	
	
	
	
    	<table class="tablecontent">
    			<tr>
    				<th scope="col" align="left"><b>Report</b> </th>
    				<th scope="col" align="left"><b>Amendment #</b> </th>
    				<th scope="col" align="left"><b>Ready to submit?</b> </th>
    				<th scope="col" align="left"><b>Status</b> </th>
    				<th scope="col" align="left"><b>Actions</b> </th>
    			</tr>
    			<c:forEach items="${command.aeReport.reports}" varStatus="status" var="report">
    			<c:if test="${report.status ne 'WITHDRAWN' and report.status ne 'REPLACED' and report.status ne 'AMENDED' and report.status ne 'COMPLETED'}">
    			<tr>    				
            		<td><div class="label">${report.reportDefinition.label}</div></td>
            		<c:if test="${report.reportDefinition.amendable == true}">
	            		<td align="center"><div class="label">${report.lastVersion.amendmentNumber}</div></td>
	            	</c:if>
	            	<c:if test="${report.reportDefinition.amendable == false}">
	            		<td/>
	            	</c:if>
            		<td class="completion-messages">
                        <c:choose>
                            <c:when test="${reportMessages[report.id].submittable}" >
                                Yes
                            </c:when>
                            <c:otherwise>
								<c:if test="${report.status ne 'COMPLETED'}">
                                <p>Not yet.  Remaining to complete:</p>
                                <c:forEach items="${reportMessages[report.id].messages}" var="sectionEntry">
                                    <h4>${sectionEntry.key.displayName} section</h4>
                                    <c:forEach items="${sectionEntry.value}" var="msg">
                                        <ul>
                                            <li>${msg.text} <c:if test="${not empty msg.property}"><!-- (${msg.property}) --></c:if></li>
                                        </ul>
                                    </c:forEach>
                                </c:forEach>
								</c:if>
                            </c:otherwise>
                        </c:choose>
						
                    </td>
            		<td id="report-status-${report.id}">
            			<c:if test="${report.lastVersion.reportStatus == 'PENDING'}" >
							<span class="dueOn" >
								<c:if test="${not empty report.lastVersion.dueOn}" >
            						<i>Due on</i> <br> <b><tags:formatDate value="${report.lastVersion.dueOn}" /></b>
            					</c:if>
            					<c:if test="${ empty report.lastVersion.dueOn}" >
            						<i>Amendment Due</i>
            					</c:if>
            				</span>
            			</c:if>
            			<c:if test="${report.lastVersion.reportStatus == 'WITHDRAWN'}" >
							<span class="submittedOn" >
            						<i>Withdrawn</i><br> <b><tags:formatDate value="${report.lastVersion.withdrawnOn}" /></b>
            				</span>
            			</c:if>
            			<c:if test="${report.lastVersion.reportStatus == 'COMPLETED'}" >
            				<span class="submittedOn" >
            					<i>Submitted on </i><br> <b><tags:formatDate value="${report.lastVersion.submittedOn}" /></b>
            				</span>
            			</c:if>	
            			<c:if test="${report.lastVersion.reportStatus == 'FAILED'}" >
             				<span class="dueOn" >
            					<i>Submission to AdEERS failed </i>
            				</span>           			
            			</c:if>
             			<c:if test="${report.lastVersion.reportStatus == 'INPROCESS'}" >
             				<span class="dueOn" >
            					<i>Submission to AdEERS in process</i>
            				</span>           			
            			</c:if>           		
            		</td>
            		<td id="action${report.id}">
						<SELECT style="width:100px;" id="actions-${report.id}" name="actions" onChange="executeAction(${report.id}, '<c:url value='/pages/ae/generateExpeditedfPdf?aeReport=${report.aeReport.id}&reportId=${report.id}'/>', '${report.aeReport.id}', '${lastVersion.submissionUrl}')">
		     				<OPTION selected value="none">Please select</OPTION>
		     				<c:if test="${command.study.caaersXMLType}">
		     					<OPTION value="xml" title="export caAERS XML">Export caAERS XML</OPTION>
		     				</c:if>
		     				<c:if test="${command.study.adeersPDFType}">
		     					<OPTION value="pdf" title="export AdEERS PDF">Export AdEERS PDF</OPTION>
		     				</c:if>
		     				<c:if test="${command.study.medwatchPDFType}">
		     					<OPTION value="medwatchpdf" title="export MedWatch 3500A PDF">Export MedWatch 3500A PDF</OPTION>
		     				</c:if>
		     				<c:if test="${command.study.dcpSAEPDFType}">
		     					<OPTION value="dcp" title="export DCP SAE PDF">Export DCP SAE PDF</OPTION>
		     				</c:if>
                			<c:if test="${command.study.ciomsPDFType}">
                    			<OPTION value="cioms" title="export CIOMS PDF">Export CIOMS PDF</OPTION>
                			</c:if>
		     				<c:if test="${command.study.ciomsSaePDFType}">
		     					<OPTION value="ciomssae" title="export DCP Safety Report PDF">Export DCP Safety Report PDF</OPTION>
		     				</c:if>
		     				<c:if test="${(report.lastVersion.reportStatus == 'PENDING') or (report.lastVersion.reportStatus == 'FAILED')}" >
		     					<OPTION value="withdraw" title="withdraw">Withdraw</OPTION>
		     				</c:if>
		     				<c:if test="${reportMessages[report.id].submittable}" >
									<c:if test="${(report.lastVersion.reportStatus == 'PENDING') or (report.lastVersion.reportStatus == 'FAILED')}" >
										<c:if test="${!command.workflowEnabled || isSuperUser}">
											<OPTION value="submit" title="submit">Submit</OPTION>
										</c:if>
									</c:if>
									<c:if test="${report.reportDefinition.amendable and (report.lastVersion.reportStatus == 'COMPLETED') }" >
										<OPTION value="amend" title="amend">Amend</OPTION>
									</c:if>
							</c:if>
		     			</SELECT>
						
            		</td>
    			</tr>
    			</c:if>
    			</c:forEach>    						
    	</table>		
</div>