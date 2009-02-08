<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%@attribute name="index" required="true" type="java.lang.Integer" %>
<%@attribute name="style"%>
<%@attribute name="radiation" type="gov.nih.nci.cabig.caaers.domain.RadiationIntervention" %>
<%@attribute name="collapsed" type="java.lang.Boolean" %>

<ae:fieldGroupDivision fieldGroupFactoryName="radiationIntervention" index="${index}" style="${style}" enableDelete="true" deleteParams="'radiation', ${index}, '_radiations'" collapsed="${collapsed}">
    <tags:errors path="aeReport.radiationInterventions[${index}]"/>

    <ui:row path="aeReport.radiationInterventions[${index}].administration">
         <jsp:attribute name="label"><ui:label path="${fieldGroup.fields[0].propertyName}" text="${fieldGroup.fields[0].displayName}" required="true"/></jsp:attribute>
         <jsp:attribute name="value"><ui:select path="${fieldGroup.fields[0].propertyName}" options="${fieldGroup.fields[0].attributes.options}" /></jsp:attribute>
    </ui:row>

    <ui:row path="aeReport.radiationInterventions[${index}].dosage">
         <jsp:attribute name="label"><ui:label path="${fieldGroup.fields[1].propertyName}" text="${fieldGroup.fields[1].displayName}" required="true"/></jsp:attribute>
         <jsp:attribute name="value"><ui:text path="${fieldGroup.fields[1].propertyName}" /></jsp:attribute>
    </ui:row>

    <ui:row path="aeReport.radiationInterventions[${index}].dosageUnit">
         <jsp:attribute name="label"><ui:label path="${fieldGroup.fields[2].propertyName}" text="${fieldGroup.fields[2].displayName}" required="true"/></jsp:attribute>
         <jsp:attribute name="value"><ui:select path="${fieldGroup.fields[2].propertyName}" options="${fieldGroup.fields[2].attributes.options}"/></jsp:attribute>
    </ui:row>

    <ui:row path="aeReport.radiationInterventions[${index}].lastTreatmentDate">
         <jsp:attribute name="label"><ui:label path="${fieldGroup.fields[3].propertyName}" text="${fieldGroup.fields[3].displayName}" required="true"/></jsp:attribute>
         <jsp:attribute name="value"><ui:date path="${fieldGroup.fields[3].propertyName}" /></jsp:attribute>
    </ui:row>

    <ui:row path="aeReport.radiationInterventions[${index}].fractionNumber">
         <jsp:attribute name="label"><ui:label path="${fieldGroup.fields[4].propertyName}" text="${fieldGroup.fields[4].displayName}" required="true"/></jsp:attribute>
         <jsp:attribute name="value"><ui:text path="${fieldGroup.fields[4].propertyName}" /></jsp:attribute>
    </ui:row>

    <ui:row path="aeReport.radiationInterventions[${index}].daysElapsed">
         <jsp:attribute name="label"><ui:label path="${fieldGroup.fields[5].propertyName}" text="${fieldGroup.fields[5].displayName}" required="true"/></jsp:attribute>
         <jsp:attribute name="value"><ui:text path="${fieldGroup.fields[5].propertyName}" /></jsp:attribute>
    </ui:row>

    <ui:row path="aeReport.radiationInterventions[${index}].adjustment">
         <jsp:attribute name="label"><ui:label path="${fieldGroup.fields[6].propertyName}" text="${fieldGroup.fields[6].displayName}" required="true"/></jsp:attribute>
         <jsp:attribute name="value"><ui:select path="${fieldGroup.fields[6].propertyName}" options="${fieldGroup.fields[6].attributes.options}"/></jsp:attribute>
    </ui:row>

    <script language="JavaScript">
        AE.registerCalendarPopups();
    </script>

</ae:fieldGroupDivision>

<script>
    function setTitleRadiation_${index}() {
        var titleID = "titleOf_radiationIntervention-" + ${index};
        var fieldObject = $("aeReport.radiationInterventions[${index}].administration");
        var selectedOption = fieldObject.options[fieldObject.selectedIndex];
        var selectedValue = selectedOption.text;
        $(titleID).innerHTML = selectedValue;
    }

    setTitleRadiation_${index}.defer();
    Event.observe($("aeReport.radiationInterventions[${index}].administration"), "change", function() {
        setTitleRadiation_${index}();
    });
</script>
