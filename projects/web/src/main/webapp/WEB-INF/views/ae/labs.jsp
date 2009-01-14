<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>
<script type="text/javascript" src="/caaers/js/extremecomponents.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value="/css/extremecomponents.css"/>">
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${tab.longTitle}</title>
    <tags:stylesheetLink name="ae"/>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="createAE"/>
    
    <!-- <tags:labs labs="${command.assignment.labLoads}"/> -->
    
    <script type="text/javascript">
        var aeReportId = ${empty command.aeReport.id ? 'null' : command.aeReport.id}
		var EnterLab = Class.create();
		Object.extend(EnterLab.prototype, {
		  initialize: function(index, termId, categoryId) {
		  	this.index = index;
		  	this.baseName = 'aeReport.labs[' + index + ']'; 
		  	this.testName = $(this.baseName + '.labTerm');
		  	this.categoryInput = $(this.baseName + '.lab-category');
		  	this.categoryId = (categoryId == null || categoryId.length) == 0 ? null : categoryId ;
		  	this.termId = (termId == null || termId.length) == 0 ? null : termId ;
		  	this.other = $(this.baseName + '.other');
		  	this.otherProperty = this.baseName + '.other';
		  	this.categoryProperty = $(this.baseName + '.lab-category');
		  	this.isTriggerOnLoad ;
		  	
		  	
		  	if(this.categoryId){
		  		for (i=0; i < this.categoryInput.options.length; i++){
		  			if (this.categoryInput.options[i].value == this.categoryId){
		  				this.categoryInput.options[i].selected=true;
		  				
		  				if (this.categoryInput.options[i].value == "105"){
		  						AE.slideAndHide($('not-microbiology-'+index))
		  						AE.slideAndShow($('microbiology-'+index))
		  						
		  					}else{		  						
		  						
		  					}		
		  				break
		  			}	
		  		}
		  	}
		  	
		  	
		  	$(this.testName).observe("change",this.updateLabOther.bind(this))
		  	$(this.categoryInput).observe("change",this.updateTermDropDown.bind(this))
		  	
		  	AE.registerCalendarPopups("lab-" + this.index)
		  	this.updateTermDropDown(true)
		  	
		  
		  },
		  
		  populateTermDropDown: function(values) {
		  	var opt1 = new Option("Please select", " ")
		  	var opt2 = new Option("Other, specify", "")
		  	// nullify drop down contents by setting length to 0
		  	this.testName.options.length = 0
		  	this.testName.options[0] = opt1;
		  	this.testName.options[1] = opt2;
		  	values.each(function(value,index){
		  		var opt = new Option(value.term,value.id)
		  		this.testName.options[index+2] = opt
		  		if(this.testName.options[index+2].value == this.termId && this.isTriggerOnLoad){
		  			this.testName.options[index+2].selected=true
		  		}
		  	
		  	}.bind(this))
		  	this.initializeLabOrOther()
		  	
		  },
		  
		  updateTermDropDown: function(onload) {
		    var kk = onload
		  	this.isTriggerOnLoad = (onload == true) ? true : false;
		  	catId= $F(this.categoryProperty)
		   	createAE.getLabTermsByCategory( catId ,this.populateTermDropDown.bind(this));
		  },
		  
		  updateLabOther: function() {
		  	catId= $F(this.categoryProperty)
		   	var isNOS = $(this.testName).options[1].selected 
		   	if(isNOS){
					$(this.testName).options[1].selected=true
					AE.slideAndShow(this.otherProperty + "-row")
		   	}else{
					$(this.otherProperty).value="";
					AE.slideAndHide(this.otherProperty + "-row")
		   	}
		   	if (catId == "105"){
				AE.slideAndHide($('not-microbiology-'+this.index))
				AE.slideAndShow($('microbiology-'+this.index))
				$(this.baseName + '.units').options[0].selected=true
				$(this.baseName + '.baseline.value').value=""
				$(this.baseName + '.baseline.date').value=""
				$(this.baseName + '.nadir.value').value=""
				$(this.baseName + '.nadir.date').value=""
				$(this.baseName + '.recovery.value').value=""
				$(this.baseName + '.recovery.date').value=""
			}else{
				if ($('not-microbiology-'+this.index).style.display != ""){
				$(this.baseName + '.site').value=""
				$(this.baseName + '.labDate').value=""
				$(this.baseName + '.infectiousAgent').value=""
				AE.slideAndHide($('microbiology-'+this.index))
				AE.slideAndShow($('not-microbiology-'+this.index))
				}
			}
		  },
		  
		  initializeLabOrOther: function() {
               var isNOS = ( $(this.other).value.length >0 )
               if(isNOS){
               		this.testName.options[1].selected=true
               		AE.slideAndShow(this.otherProperty + "-row")
               }
          }		 
		});
		
		
        Element.observe(window, "load", function() {
            new ListEditor("lab", createAE, "Lab", {
                addParameters: [aeReportId],
                addFirstAfter: "single-fields",
                addCallback: function(index) {
                    new EnterLab(index,null);
                },
                deletable: true,
                reorderable: true
            }, 'aeReport.labs')
            <c:forEach items="${command.aeReport.labs}" varStatus="status" var="lab">
            	new EnterLab(${status.index},"${lab.labTerm.id}","${lab.labTerm.category.id}");
            </c:forEach>
            
        })
    </script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" pageHelpAnchor="section15labs">
    <jsp:attribute name="instructions">
    	<tags:instructions code="instruction_ae_labs" />
    	<c:if test="${not empty configuration.map.labViewerBaseUrl}">
			<p>View this person's details in the <a href="${configuration.map.labViewerBaseUrl}/studysubject.do?studySubjectGridId=${command.assignment.gridId}" class="sso" target="labviewer">lab viewer</a>.</p>
		</c:if>
    </jsp:attribute>
    <jsp:attribute name="repeatingFields">
        <c:forEach items="${command.aeReport.labs}" varStatus="status">
            <ae:oneLab index="${status.index}"/>
        </c:forEach>
    </jsp:attribute>
    <jsp:attribute name="localButtons">
        <tags:listEditorAddButton divisionClass="lab" label="Add a lab" buttonCssClass="ae-list-editor-button"/>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>