<%@ include file="/WEB-INF/views/taglibs.jsp" %>

<html>
<head>
    <title>${tab.longTitle}</title>
    <tags:dwrJavascriptLink objects="createAE"/>
    <script type="text/javascript">
    	var reportIndex = ${empty command.reportIndex ? 'null' : command.reportIndex}
    </script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" pageHelpAnchor="section11reporterinformation" saveButtonLabel="Submit">
    
    
    <jsp:attribute name="singleFields">
    	
    	<p> The <b>${command.aeReport.reports[command.reportIndex].reportDefinition.name} </b> will be sent to the following preconfigured recipient(s) .<br /></p>
    	
    		<table class="tablecontent">
    		
    			<tr>
    			
    				<th scope="col" align="left"><b>Recipient(s)</b></th>
    			</tr>
    	
    	
		    	<c:forEach items="${command.reportDeliveries}" varStatus="status" var="report">
    		
    				<tr>
    				
    					<td><div class="label">
    						${report.reportDeliveryDefinition.entityType eq 1 ? report.reportDeliveryDefinition.entityName : report.endPoint }
    						 <c:if test="${report.reportDeliveryDefinition.entityType ne 1}">
   					 			 (${report.reportDeliveryDefinition.entityName})
    						 </c:if>
    						 </div></td>
    			
    				</tr>
		   	 	</c:forEach>

    		    	
    	    	
    		</table>

    		<br>
    	    	
    
    		<chrome:division title="Cc details">
    		
    			<p>To send this report to others, enter the email addresses in the field below.<br />
    		
    				Multiple email addresses can be entered separated by a comma.</p>

            
    	
		    	<c:forEach items="${fieldGroups['ccReport'].fields}" var="field">
                
    				<tags:renderRow field="${field}"/>
            
    			</c:forEach>
        
   			 </chrome:division>
    
    </jsp:attribute>
		
	<jsp:attribute name="tabControls">
		<div class="content buttons autoclear">
			<div class="local-buttons">
			</div>
	    	<div class="flow-buttons">
				<span class="prev">       
	            	<input type="image" alt="« Save &amp; Back" value="« saveback" class="tab0" id="flow-prev" src="/caaers/images/blue/saveback_btn.png"/>
	        	</span>	        
				<span class="next">
					<input type="image" alt="submit report »" value="Submit Report " id="flow-next" src="<c:url value="/images/blue/submit_btn.png" />"/>
	       		</span>
			</div>
		</div>
	</jsp:attribute>

</tags:tabForm>
</body>
</html>
