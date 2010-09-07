<%@ include file="/WEB-INF/views/taglibs.jsp"%>

<html>
<head>

<style type="text/css">
        /* Override default lable length */
       	div.row div.label { width: 9em; } 
        div.row div.value { margin-left: 10em; }
</style>

<style>
    .yui-pg-page { padding: 5pt; }
    .yui-dt-label .yui-dt-sortable { color: white; }
    .yui-dt table { width: 100%; }
    div.yui-dt-liner a {color : black;}
    tr.yui-dt-even { background-color: #FFF; border-bottom: 1px gray solid;}
    tr.yui-dt-odd { background-color: #EDF5FF; border-bottom: 2px blue dotted; padding: 2px; }
    
</style>
    
<title>Search Subjects</title>
<tags:dwrJavascriptLink objects="search"/>

<script>
   
function buildTable(form) {
	$('indicator').className=''
	var type = "";
	var text = "";

	for(var x=0; x < 3; x++) {
	
		if ( $('prop'+x).value.length > 0 ){
			text = text +  $('prop'+x).value + ",";
			type = type +  $('prop'+x).name +',';
		}
	}
	
	$('prop').value=type
	$('value').value=text
	
	var parameterMap = getParameterMap(form);
	search.buildParticipantTable(parameterMap, type, text, test);		
}

function test(jsonResult) {
    $('indicator').className = 'indicator';
    initializeYUITable("tableDiv", jsonResult, myColumnDefs, myFields);
    hideCoppaSearchDisclaimer();
}

var linkFormatter = function(elCell, oRecord, oColumn, oData) {
        var _id = oRecord.getData("id");
        elCell.innerHTML = "<a href='edit?participantId=" + _id + "'>" + oData + "</a>";
};

var myColumnDefs = [
    {key:"firstName", label:"First Name", sortable:true, resizeable:true, formatter: linkFormatter},
    {key:"lastName", label:"Last Name", sortable:true, resizeable:true, formatter: linkFormatter},
    {key:"primaryIdentifierValue", label:"Primary ID", sortable:true, resizeable:true, formatter: linkFormatter},
    {key:"studySubjectIdentifiersCSV", label:"Study Subject Identifiers", sortable:true, resizeable:true, formatter: linkFormatter}
];

var myFields = [
    {key:'id', parser:"string"},
    {key:'firstName', parser:"string"},
    {key:'lastName', parser:"string"},
    {key:'primaryIdentifierValue', parser:"string"},
    {key:'studySubjectIdentifiersCSV', parser:"string"}
];
    
</script>
</head>
<body>

<div class="content">

<form:form name="searchForm" id="searchForm" method="post">
	<p><tags:instructions code="instruction_subject_search"/></p>


    <chrome:box title="Search Criteria" cssClass="mpaired" autopad="false">

        <div class="row">
            <div class="label">Identifier&nbsp; </div>
            <div class="value"><input id="prop0" type="text" name="identifier"/></div>
        </div>


        <div class="row">
            <div class="label">First Name&nbsp; </div>
            <div class="value"><input id="prop1" type="text" name="firstName"/></div>
        </div>

        <div class="row">
            <div class="label">Last Name&nbsp; </div>
            <div class="value"><input id="prop2" type="text" name="lastName"/></div>
        </div>

        <div class="row">
            <div class="value" style="float:left;">
                <tags:button color="blue" type="button" value="Search" size="small" icon="search" onclick="buildTable('assembler'); $('bigSearch').show();"/>
                <tags:indicator id="indicator"/>
            </div>
        </div>
    </chrome:box>



 </form:form>
<br>			
<br>

<form:form id="assembler" >
	<input type="hidden" name="_prop" id="prop" >
	<input type="hidden" name="_value" id="value"  >

    <c:set var="display" value="none" />

    <div id="bigSearch" style="border:0px green dotted; display:${display};">
    <chrome:box title="Results">
     	<chrome:division id="single-fields">
        <div id="tableDiv">
   			<c:out value="${assembler}" escapeXml="false"/>
		</div>
		</chrome:division>
	</chrome:box>
    </div>
    
</form:form>
</div>
</body>
</html>