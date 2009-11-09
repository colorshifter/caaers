<%@ include file="/WEB-INF/views/taglibs.jsp"%>

<html>
<head>
<title>Search Organization</title>

<style type="text/css">
    div.row div.label { width: 9em; } 
    div.row div.value { margin-left: 10em; }
    div.content { padding: 5px 15px; }
</style>

<title>${tab.longTitle}</title>
<tags:dwrJavascriptLink objects="search"/>

<script>

function buildTable(form) {
	$('indicator').className=''
	var type = "";
	var text = "";

	for(var x=0; x < 2; x++) {
	
		if ( $('prop'+x).value.length > 0 ){
			text = text +  $('prop'+x).value + ",";
			type = type +  $('prop'+x).name +',';
		}
	}
	
	$('prop').value=type
	$('value').value=text
	
	var parameterMap = getParameterMap(form);		
	search.getOrganizationTable(parameterMap,type,text,showTable);
    $('bigSearch').show();
}


</script>
</head>
<body>

<div class="tabpane">
<div class="workflow-tabs2">
 <ul id="" class="tabs autoclear">
    <li id="thirdlevelnav" class="tab"><div>
        <a href="createOrganization">Create Organization</a>
    </div></li>
    <li id="thirdlevelnav" class="tab selected"><div>
        <a href="searchOrganization">Search Organization</a>
    </div></li>
 </ul>
	<tags:pageHelp propertyKey="searchOrganization" />
 </div>
 <div class="content">
  <form:form name="searchForm" id="searchForm" method="post">
<p>
<tags:instructions code="organizationsearch" />
</p>
   <chrome:box title="Search Criteria" cssClass="mpaired" autopad="false">
		    <div class="row">
		    	<div class="label"> Name :&nbsp; </div>
		    	<div class="value"><input id="prop0" name="name" type="text"/></div>
		    </div>
		    
		    <div class="row">
		    	<div class="label"> CTEP Identifier :&nbsp; </div>
		    	<div class="value"><input id="prop1" type="text" name="nciInstituteCode"/></div>
		    </div>

           <div class="row">
        	<div class="value">
        		<tags:button type="button" value="Search" color="blue" icon="search" onclick="buildTable('assembler');" size="small"/>
	            <tags:indicator id="indicator" />
        	</div>
           </div>
   </chrome:box>

	<div class="endpanes" />
	<div class="endpanes" />


   </form:form>
     <div id="bigSearch" style="display:none;">
         <br>
         <form:form id="assembler">
             <div>
                 <input type="hidden" name="_prop" id="prop">
                 <input type="hidden" name="_value" id="value">
             </div>
             <chrome:box title="Search Results">
                 <chrome:division id="single-fields">
                     <div id="tableDiv">
                         <c:out value="${assembler}" escapeXml="false"/>
                     </div>
                 </chrome:division>
             </chrome:box>
         </form:form>
     </div>
 </div>
 
</div>
</body>
</html>