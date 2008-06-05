<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@attribute name="isMeddra" required="true" type="java.lang.Boolean" description="Will tell whether the autocompleter should look for MedDRA or CTC" %>
<%@attribute name="version" required="true" type="java.lang.Integer" description="Will tell the version of ctc or meddra to use" %>
<%@attribute name="instructions" fragment="true" %>
<%@attribute name="callbackFunctionName" required="true" description="The call back function in the parent page, that will be invoked with the selected terms"%>
<%@attribute name="title" required="false" %> 
<tags:dwrJavascriptLink objects="createAE"/>
<script type="text/javascript">
  
 	//This object will store the reference of the Window, will also contains function
 	//that will be called from the loaded page, that has to interact with the parent page(opener page)
 	
 	//Note:- for some reason, the javascript embeded inside javascript tag in the child page (the page loaded in the linline popup window via AJAX)
 	//is not properly executing, so I modified it to work based on inline-hidden div.
 	var catSel = null;
 	var CategorySelector = Class.create();
 	Object.extend(CategorySelector.prototype, {
		initialize: function(meddra, ver) {
			this.win = null;
			this.isMeddra = meddra;
			this.version = ver;
		},
		
		showWindow:function(wUrl, wTitle, wWidth, wHeight){
			win = new Window({className:"alphacube", destroyOnClose:true,title:wTitle,  width:wWidth,  height:wHeight, 
			onShow:this.show.bind(this),
			onBeforeShow:this.beforeShow.bind(this)
			});
			this.win = win;
			win.setContent('chooseCategory');
			win.show(true);
			
		},
		initializeAutoCompleter: function() {
			AE.createStandardAutocompleter('termCode', 
            		function(autocompleter, text){
            			if(this.isMeddra){
            				createAE.matchLowLevelTermsByCode(this.version,text, function(values) {
													autocompleter.setChoices(values)});
            			}else{
            				createAE.matchTerms(text, this.version, '', 25 , function(values){
            					autocompleter.setChoices(values)});
            			}
            		},
            		function(aterm) {
            			return aterm.fullName;
            		}
            	);
		},
		finishSingleTermSelection:function(){
			var selTermMap = new Hash();
			var termElement = $('termCode');
			var termElementInput = $('termCode-input');
			
			var termId = termElement.getValue();
			if(termId) selTermMap.set(termId, termElementInput.getValue());
			
			termElement.clear();
			termElementInput.clear();
			${callbackFunctionName}(selTermMap); //need to refactor, this is a rude way of calling a function
			
		},
		finishMultiTermsSelection:function() {
			var terms = $('terms');
			var categories = $('categories');
			
			var opts = terms.options;
			
			var selTermMap = new Hash();
			//each over iterator is not working, dont know why.
			if(opts.length > 0) {
				for(i = 0; i< opts.length; i++){
					if(opts[i].selected) selTermMap.set(opts[i].value, opts[i].text);
				}
			}
			Windows.close(this.win.getId());
			//reset the category and terms
			terms.options.length=0;
			categories.selectedIndex = -1;
			//call the call back
			${callbackFunctionName}(selTermMap); //need to refactor, this is a rude way of calling a function
		},
		beforeShow : function(){
			
		},
		show: function(){
			
		},
		showTerms: function(el){
			catIds = $(el).getValue();
			var terms = $('terms');
			terms.options.length=0;
			
			/* BiJu : Optimize this to make single call instead of multiple */
			
			catIds.each(function(catId){
				 createAE.getTermsByCategory(catId, function(ctcTerms) {
				 	ctcTerms.each(function(ctcTerm) {
                       var opt = new Option(ctcTerm.fullName, ctcTerm.id)
                       terms.options.add(opt);
                   })
				 });		
			} );
			
			
		}
		
		
 	});
 	
 	Element.observe(window, "load", function() {
 	 catSel = new CategorySelector(${isMeddra}, ${version});
 	 catSel.initializeAutoCompleter();
 	});
 	
 	function showCategoryBox(){
 		catSel.showWindow('<c:url value="/pages/selectCTCTerms" />', '${title}', 800, 380 );
 	}
 	
 	
</script>

<chrome:box title="Find &amp; add adverse event term(s)">
 	<c:if test="${not empty instructions}"><p class="instructions"><jsp:invoke fragment="instructions"/></p></c:if>
    <c:if test="${empty instructions}"><p class="instructions">Enter the term in the autocompleter and click on add to add a single solicited adverse event term, click on query to add multiple.</p></c:if>
 		isMeddra : ${isMeddra} , version : ${version}
 		<table id="fnd-0" class="query">
  			<tbody>
  				<tr>
  					<td class="one">
  						<div>
  							<tags:autocompleter displayName="abcd" propertyName="termCode" /><input type="button" value="Add" onclick="catSel.finishSingleTermSelection()" />
  						</div>
  						<div class="local-buttons">
  							
  						</div>
  					</td>
  					<c:if test="${not isMeddra}">
  					<td class="two">OR</td>
  					<td class="three"><input type="button" value="Add Multiple" onclick="showCategoryBox()" /></td>
  					</c:if>
  					
  				</tr>
  			</tbody>
  		</table>
  	</chrome:box>
  	<!-- the hidden window for category popup -->
  	<div style="display:none">
	<c:if test="${not isMeddra}">
	<div id="chooseCategory">
  	<chrome:box title="Choose CTC term(s):" autopad="true">
		<tags:renderRow>
			<jsp:attribute name="label">CTC categorie(s)</jsp:attribute>
			<jsp:attribute name="value"><select name="categories" id="categories" size="5" class="categories" multiple onChange="catSel.showTerms(this);">
				<c:forEach var="cat" items="${command.ctcVersion.categories}">
					<option value="${cat.id}">${cat.name}</option>
				</c:forEach>
			</select></jsp:attribute>
		</tags:renderRow>
		<tags:renderRow>
			<jsp:attribute name="label">CTC terms(s)</jsp:attribute>
			<jsp:attribute name="value"><select name="terms" id="terms" size="5" class="terms" multiple></select></jsp:attribute>
		</tags:renderRow>
		<hr />
		<div>		
		<input type="button" value="Ok" onclick="catSel.finishMultiTermsSelection();" />
		</div>		
	</chrome:box>
	</div>
	</c:if>
	<c:if test="${isMeddra}">
		<p>Addition of multiple terms is only supported for CTC terminology</p>
	</c:if>
  	
	</div>