<%@ include file="/WEB-INF/views/taglibs.jsp" %>

<html>
	<head>
		<style type="text/css">
        	input.autocomplete {
            	width: 450px;
            	font-style: normal;
            	background-color: #CCE6FF;
        	}

        	input.pending-search {
            	width: 450px;
            	color: gray;
            	font-style: italic;
            	background-color: #CCE6FF;
        	}
	    </style>
	
    	<tags:dwrJavascriptLink objects="authorRule"/>
    	<title>Select Rule Level</title>

	    <script type="text/javascript">
	    	
			
			var sponsorNameAutocompleterProps = {
            	basename: "sponsorName",
            	populator: function(autocompleter, text) {
                	authorRule.matchOrganization(text, function(values) {
							autocompleter.setChoices(values)
					})
            	},
            	valueSelector: function(organization) {
            		var nciInstituteCode = organization.nciInstituteCode == null ? "" : " ( " + organization.nciInstituteCode + " ) ";
            		
                	return organization.name + nciInstituteCode;
            	}
        	}
        	
        	var institutionNameAutocompleterProps = {
        		basename: "institutionName",
        		populator: function(autocompleter, text) {
        				authorRule.matchSites(text, function(values) {
							autocompleter.setChoices(values)
        			})
        		},
        		valueSelector: function(organization) {
	        		var nciInstituteCode = organization.nciInstituteCode == null ? "" : " ( " + organization.nciInstituteCode + " ) ";
        			return organization.name + nciInstituteCode;
        		}
        	}
        	
        	var categoryIdentifierAutocompleterProps = {
        		basename: "categoryIdentifier",
        		populator: function(autocompleter, text){
        			var institutionNameInput =  $('institutionName-input').value;
					if(institutionNameInput != '') {
						authorRule.matchStudiesByInstitution(text, $('institutionName').value, function(values) {
							autocompleter.setChoices(values)
						})
					}
					var sponsorNameInput =  $('sponsorName-input').value;
					if(sponsorNameInput != '') {
						authorRule.matchStudies(text, $('sponsorName').value, function(values) {
							autocompleter.setChoices(values)
						})
					}
        		},
        		valueSelector: function(study){
        			if(study.primaryIdentifierValue) return "(" + study.primaryIdentifierValue + ") "+study.shortTitle;
					return study.shortTitle;
        		}
        	}

 	        function acPostSelect(mode, selectedChoice) {
            	Element.removeClassName($(mode.basename + "-input"), "required");
            	Element.removeClassName($(mode.basename + "-input"), "valueOK");
            	Element.addClassName($(mode.basename + "-input"), "validField");
            	Element.update(mode.basename + "-selected-name", mode.valueSelector(selectedChoice))
            	$(mode.basename).value = selectedChoice.id;
            	$(mode.basename + '-selected').show()
            	new Effect.Highlight(mode.basename + "-selected")
            	$(mode.basename + "-input").onblur = function() {
                	if ($(mode.basename + "-input").hasClassName('validField')) {
                    	ValidationManager.setNormalState($(mode.basename + "-input"));
                	} else {
                    	ValidationManager.setInvalidState($(mode.basename + "-input"));
                	}
            	}
            	$(mode.basename + "-input").onchange = function() {
                	if (!$(mode.basename + "-input").hasClassName('validField')) {
                    	ValidationManager.setInvalidState($(mode.basename + "-input"));
                	}
            	}
        	 }
        
        	 function updateSelectedDisplay(mode) {
             	if ($(mode.basename).value) {
                	Element.update(mode.basename + "-selected-name", $(mode.basename + "-input").value)
                	$(mode.basename + '-selected').show()
            	}
	         }

     	     function acCreate(mode) {
             	new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices", mode.populator, {
                	valueSelector: mode.valueSelector,
                	afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                    	acPostSelect(mode, selectedChoice)
                    	if(mode.basename == 'categoryIdentifier')
                    		setCategoryIdentifier(selectedChoice);
                    	if(mode.basename == 'sponsorName')
                    		setSponsor(selectedChoice);
                    	if(mode.basename == 'institutionName')
                    		setInstitution(selectedChoice);
                	},
                	indicator: mode.basename + "-indicator"
            	})
            	Event.observe(mode.basename + "-clear", "click", function() {
                	Element.addClassName($(mode.basename + "-input"), "required");
                	Element.removeClassName($(mode.basename + "-input"), "validField");
                	Element.removeClassName($(mode.basename + "-input"), "valueOK");
                	$(mode.basename + "-selected").hide()
                	$(mode.basename).value = ""
                	$(mode.basename + "-input").value = ""
	            })
        	 }
        	 
        	function setCategoryIdentifier(selectedChoice) 
			{
				$("categoryIdentifier").value = selectedChoice.shortTitle;
			}


			function setSponsor(selectedChoice) 
			{
				$("sponsorName").value = selectedChoice.name;
			}
			
			function setInstitution(selectedChoice) 
			{
				$("institutionName").value = selectedChoice.name;
			}
			
			function setRuleSetName(ruleSetElement)
    		{
	    		$("hiddenRuleSetName").value=ruleSetElement.options[ruleSetElement.selectedIndex].value;    		
    		}
    		
    		function displayRuleTypeInput(level)
			{
				//$('sponsorName-input').value = '';
				//$('institutionName-input').value = '';
				//$('categoryIdentifier-input').value	 = '';
				$("level").value = level.value;
				
				if(level.value == 'Please select a Rule level')
				{
					$("level").value = '';
					Effect.Fade("sponsorName-details");
					Effect.Fade("institutionName-details");
					Effect.Fade("categoryIdentifier-details");
				}
					 
				if (level.value == 'Sponsor')
				{
					Effect.Appear("sponsorName-details");
					Effect.Fade("categoryIdentifier-details");
					Effect.Fade("institutionName-details");
				}
				
				if (level.value == 'SponsorDefinedStudy')
				{
					Effect.Appear("sponsorName-details");
					Effect.Appear("categoryIdentifier-details");
					Effect.Fade("institutionName-details");
				}

				if (level.value == 'Institution')
				{
					Effect.Appear("institutionName-details");
					Effect.Fade("categoryIdentifier-details");
					Effect.Fade("sponsorName-details");
				}

				if (level.value == 'InstitutionDefinedStudy')
				{
					Effect.Appear("institutionName-details");
					Effect.Appear("categoryIdentifier-details");
					Effect.Fade("sponsorName-details");
				}	 
			}
			
			function initializeSelectElements(){
				if(${initializeLevelSelect}){
					var sel = $('ruleLevel');
					for(var i = 0; i < sel.options.length; i++){
						if(sel.options[i].value == '${initialLevel}'){
							sel.selectedIndex = i;
						}
					}
				}
				if(${initializeRuleSetNameSelect}){
					var sel = $('ruleSetName');
					for(var i = 0; i < sel.options.length; i++){
						if(sel.options[i].value == '${initialRuleSet}')
							sel.selectedIndex = i;
					}
				}
			}
			
			Event.observe(window, "load", function(){
	    		acCreate(sponsorNameAutocompleterProps);
	            updateSelectedDisplay(sponsorNameAutocompleterProps);
	            acCreate(institutionNameAutocompleterProps);
	            updateSelectedDisplay(institutionNameAutocompleterProps);
	            acCreate(categoryIdentifierAutocompleterProps);
	            updateSelectedDisplay(categoryIdentifierAutocompleterProps);
				initSearchField();
				initializeSelectElements();
				displayRuleTypeInput($('ruleLevel'));
				
				//remove the query string from form url
	    		removeQueryStringFromForm('command');	            				
			});
    		
    	</script>
	</head>
	<body onLoad="nextTab();">
	<chrome:box title="Create Rules">
        	<tags:instructions code="ruletype" />  
    	<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" hideBox="true">
    		<jsp:attribute name="singleFields">
    			<div class="row"  id="ruleSetDiv">
            			<div class="label"><tags:requiredIndicator/>&nbsp;<label for="ruleSetName">Type</label></div>
            			<div class="value">
                			<select id="ruleSetName" onChange="setRuleSetName(this)">
                    			<option value="">Please select</option>
				                <option value="SAE Reporting Rules">SAE Reporting Rules </option>
                				<option value="Mandatory Sections Rules">Mandatory Sections Rules</option>
				            </select>
				            <tags:errors path="ruleSetName"/>
        				</div>
        			</div>
				
    				<div class="row"  id="ruleLevelDiv">
            			<div class="label"><tags:requiredIndicator/>&nbsp;<label for="ruleLevelName">Level</label></div>
            			<div class="value">
            				<form:hidden path="level"/>
                			<select id="ruleLevel" onChange="displayRuleTypeInput(this)" value="${level }">
                    			<option value="Please select a Rule level">Please select</option>
				                <option value="Sponsor">Sponsor rules </option>
                				<option value="Institution">Institution rules</option>
                				<option value="SponsorDefinedStudy">Sponsor defined rules for a study</option>
                				<option value="InstitutionDefinedStudy">Institution defined rules for a study</option>
				            </select>
				            <tags:errors path="level"/>
        				</div>
        			</div>
        			
        			
        			<div class="autoclear" id="criteria-div">		
						
						<div title="Select sponsor" id="sponsorName-details" style="display:none">
                            <ui:row path="sponsorName">
                                <jsp:attribute name="label">
                                    <ui:label required="true" text="Sponsor" path="sponsorName"/>
                                </jsp:attribute>
                                <jsp:attribute name="value">
                                    <ui:autocompleter path="sponsorName" enableClearButton="true" required="true" title="Sponsor"/>
                                </jsp:attribute>
                            </ui:row>
						</div>
						
						<div title="Select institution" id="institutionName-details" style="display:none" class="pane">
							<ui:row path="institutionName">
                                <jsp:attribute name="label">
                                    <ui:label required="true" text="Institution" path="institutionName"/>
                                </jsp:attribute>
                                <jsp:attribute name="value">
                                    <ui:autocompleter path="institutionName" enableClearButton="true" required="true" title="Institution"/>
                                </jsp:attribute>
                            </ui:row>
						</div>

						<div title="Select study" id="categoryIdentifier-details" style="display:none" class="pane">
							<ui:row path="categoryIdentifier">
                                <jsp:attribute name="label">
                                    <ui:label required="true" text="Study" path="categoryIdentifier"/>
                                </jsp:attribute>
                                <jsp:attribute name="value">
                                    <ui:autocompleter path="categoryIdentifier" enableClearButton="true" required="true" title="Study"/>
                                </jsp:attribute>
                            </ui:row>
						</div>
					</div>
					
					
    			<form:hidden id="hiddenRuleSetName" path="ruleSetName"/>
    		</jsp:attribute>
    	</tags:tabForm>
		</chrome:box>
	</body>
</html>