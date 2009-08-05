<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/ui"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@taglib prefix="caaers" uri="http://gforge.nci.nih.gov/projects/caaers/tags" %>
<%@attribute name="index" required="true" type="java.lang.Integer" %>
<%@attribute name="style"%>
<%@attribute name="cssClass" required="true" %>
<%@attribute name="readOnly" type="java.lang.Boolean" %>
<%@attribute name="isNew" type="java.lang.Boolean" %>
<%@attribute name="sp" type="gov.nih.nci.cabig.caaers.domain.StudyPersonnel" %>

<c:set var="mainGroup">main${index}</c:set>
<c:set var="css">${cssClass} ${index % 2 ne 0 ? 'even' : 'odd'} ${sectionClass}</c:set>

<tr id="${cssClass}-${empty idSuffix ? index : idSuffix}" class="${css}" onmouseout="this.className='${css}'" onmouseover="this.className='highlight'" style="${style}" valign="top">
	 <td style="border-right:none;">
	 <c:set var="_staffField" value="${fieldGroups[mainGroup].fields[0]}" />
	 <ui:autocompleter path="${_staffField.propertyName}" 
	 		displayNamePath="${_staffField.propertyName}.researchStaff.fullName"
			required="${_staffField.required}" 
			validationJSClass="${_staffField.validatorClassName}" 
			readonly="${readOnly}" 
			size="${_staffField.attributes.size}"
			title="${field.displayName}"
			enableClearButton="${_staffField.attributes.enableClear}" 
			initialDisplayValue="Begin typing here..." />
	 </td>

    <td style="border-right:none; width:300px;">
	    <c:set var="_staffRoleField" value="${fieldGroups[mainGroup].fields[1]}" />
	 	<ui:select options="${_staffRoleField.attributes.options}"
	 		path="${_staffRoleField.propertyName}" 
	 		required="true"
	 		validationJSClass="${_staffRoleField.validatorClassName}" disabled="${readOnly}"/>
	 </td>

    <td style="border-left:none;">
    <c:set var="_staffStatusField" value="${fieldGroups[mainGroup].fields[2]}" />
        <c:set var="isActive"><jsp:attribute name="value"><caaers:value path="${_staffStatusField.propertyName}"/></jsp:attribute></c:set>
        <c:if test="${isActive}">Active</c:if>
        <c:if test="${!isActive}">Inactive</c:if>
    </td>

    <td style="border-left:none;">
        <c:if test="${!isNew && sp.siteResearchStaff.researchStaff != null}">
            <c:if test="${isActive}"><tags:button type="button" color="red" cssClass="" value="Deactivate"size="small" onclick="deactivate(${index})"/></c:if>
            <c:if test="${!isActive}"><tags:button type="button" color="green" cssClass="" value="Activate" size="small"onclick="activate(${index})"/></c:if>
        </c:if>
        
        <c:if test="${isNew || sp.siteResearchStaff.researchStaff == null}">
            <a id="del-${index}" class="del-${cssClass}" href="javascript:fireDelete(${index},'${cssClass}-${index}');">
                <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete" style="vertical-align:middle">
            </a>
        </c:if>

<%--
--%>
	</td>
</tr>
