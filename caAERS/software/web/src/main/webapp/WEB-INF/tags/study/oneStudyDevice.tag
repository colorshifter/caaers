<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/ui"%>
<%@taglib prefix="caaers" uri="http://gforge.nci.nih.gov/projects/caaers/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@attribute name="index" type="java.lang.Integer" required="true" %>
<%@attribute name="studyDevice" type="gov.nih.nci.cabig.caaers.domain.StudyDevice" required="true" %>
<%@attribute name="collapsed" type="java.lang.Boolean" %>

<c:set var="_deviceName" value="${studyDevice.displayName}" />
<c:if test="${not empty studyDevice.device}">
   <c:set var="_deviceName" value="${studyDevice.device.displayName}" />
</c:if>

<c:set var="_readOnly" value="${_deviceName ne ''}" />
<c:set var="_device" value="${studyDevice.device != null}" />
<chrome:division collapsable="true" collapsed="${collapsed}" id="StudyDevice_${index}" title="&nbsp;${_deviceName}" enableDelete="true" deleteParams="'removeStudyDevice', '${index}'">

<table width="100%">
<tr>
<td valign="top">
    <c:if test="${!_readOnly}">
    <div class="row">
        <div class="label"><input type='radio' name='other${index}' id="radioDevice${index}" value="0" onclick="toggleDeviceOrOther(${index})" checked="true">&nbsp;Device</div>
        <div class="value">
            <ui:autocompleter path="study.studyDevices[${index}].device" initialDisplayValue="Begin typing here..." size="50" title="Study Device">
                          <jsp:attribute name="populatorJS">
                              function(autocompleter, text){
                                  createStudy.fetchDevicesByText(text, function(values) {
                                      autocompleter.setChoices(values)
                                  })
                              }
                          </jsp:attribute>
                          <jsp:attribute name="selectorJS">
                              function (obj) {
                                  return obj.displayName;
                              }
                          </jsp:attribute>
                          <jsp:attribute name="optionsJS"> {
<%--
                                afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                                    alert(selectedChoice);
                                }
--%>
                            }
                         </jsp:attribute>
                      </ui:autocompleter>
        </div>
    </div>

    <div class="row">
        <div class="label"><input type='radio' name='other${index}' id="radioOther${index}" value="1" onclick="toggleDeviceOrOther(${index})">&nbsp;&nbsp;&nbsp;Other</div>
        <div class="value"></div>
    </div>
    </c:if>

    <c:if test="${_device}">
        <div class="row">
            <div class="label">Common name</div>
            <div class="value">${command.study.studyDevices[index].device.commonName}</div>
        </div>
        <div class="row">
            <div class="label">Brand name</div>
            <div class="value">${command.study.studyDevices[index].device.brandName}</div>
        </div>
        <div class="row">
            <div class="label">Device type</div>
            <div class="value">${command.study.studyDevices[index].device.type}</div>
        </div>
    </c:if>
    
    <div id="study.studyDevices[${index}].otherDevice" style="display:${command.study.studyDevices[index].otherDevice ? 'inline' : 'none'};">
        <div class="row">
            <div class="label">Other common name</div>
            <div class="value"><ui:text path="study.studyDevices[${index}].otherCommonName" size="40"/></div>
        </div>
        <div class="row">
            <div class="label">Other brand name</div>
            <div class="value"><ui:text path="study.studyDevices[${index}].otherBrandName" size="40"/></div>
        </div>
        <div class="row">
            <div class="label">Other device type</div>
            <div class="value"><ui:text path="study.studyDevices[${index}].otherDeviceType" size="40"/></div>
        </div>
    </div>
</td>    
<td valign="top">
    <div class="row">
        <div class="label">Catalog number</div>
        <div class="value"><ui:text path="study.studyDevices[${index}].catalogNumber" size="40"/></div>
    </div>
    <div class="row">
        <div class="label">Manufacturer name</div>
        <div class="value"><ui:text path="study.studyDevices[${index}].manufacturerName" size="40"/></div>
    </div>
    <div class="row">
        <div class="label">Manufacturer city</div>
        <div class="value"><ui:text path="study.studyDevices[${index}].manufacturerCity" size="40"/></div>
    </div>
    <div class="row">
        <div class="label">Manufacturer state</div>
        <div class="value"><ui:text path="study.studyDevices[${index}].manufacturerState" size="40"/></div>
    </div>
    <div class="row">
        <div class="label">Model number</div>
        <div class="value"><ui:text path="study.studyDevices[${index}].modelNumber" size="40"/></div>
    </div>
</td>
</tr>
</table>
</chrome:division>