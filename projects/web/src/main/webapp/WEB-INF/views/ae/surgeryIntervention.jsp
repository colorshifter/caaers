<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${tab.longTitle}</title>
    <tags:stylesheetLink name="ae"/>
    <tags:includeScriptaculous/>
     <tags:dwrJavascriptLink objects="createAE"/>
     
     <style type="text/css">
    	div.row div.label { width: 15em; }
    	div.row div.value { margin-left: 16em;}
    	
    	 textarea {
            width: 20em;
            height: 5em;
        }
    	
    </style>
    
    <script type="text/javascript">
    
    	var aeReportId = ${empty command.aeReport.id ? 'null' : command.aeReport.id}
    	
    	
    	 var EnterAnatomicSite = Class.create()
        Object.extend(EnterAnatomicSite.prototype, {
            initialize: function(index, anatomicSiteName) {
                this.index = index
                var cmProperty = "aeReport.surgeryInterventions[" + index + "]";
                this.anatomicSiteProperty = cmProperty + ".anatomicSite"

                if (anatomicSiteName) $(this.anatomicSiteProperty + "-input").value = anatomicSiteName
						
       
                AE.createStandardAutocompleter(
                    this.anatomicSiteProperty, this.termPopulator.bind(this),
                    function(anatomicSiteCondition) { return anatomicSiteCondition.name })

            },

            termPopulator: function(autocompleter, text) {
                createAE.matchAnatomicSite(text, function(values) {
                    autocompleter.setChoices(values)
                })
            }
        })
    	

        Element.observe(window, "load", function() {
        	
        	<c:forEach items="${command.aeReport.surgeryInterventions}" varStatus="status" var="surgeryIntervention">
            new EnterAnatomicSite(${status.index}, '${surgeryIntervention.anatomicSite.name}')
            </c:forEach>
           

			if ( $('surgeryIntervention-0') != null ){
				$('add-surgeryIntervention-button').type="hidden";
			}
			
            new ListEditor("surgeryIntervention", createAE, "SurgeryIntervention", {
                addFirstAfter: "single-fields",
                addParameters: [aeReportId],
                addCallback: function(index) {
                	AE.registerCalendarPopups("surgeryIntervention-" + index)
                	new EnterAnatomicSite(index);
                	$('add-surgeryIntervention-button').type="hidden";
                	
                }
            })
        })
    
    </script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}">
    <jsp:attribute name="instructions">
    <tags:instructions code="instruction_ae_surgery" />   
    </jsp:attribute>
   <jsp:attribute name="repeatingFields">
        <c:forEach items="${command.aeReport.surgeryInterventions}" varStatus="status">
            <ae:oneSurgeryIntervention index="${status.index}"/>
        </c:forEach>
    </jsp:attribute>
    <jsp:attribute name="localButtons">
        <tags:listEditorAddButton divisionClass="surgeryIntervention" label="Add a Surgery intervention"/>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>