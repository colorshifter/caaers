<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/ui" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="caaers" uri="http://gforge.nci.nih.gov/projects/caaers/tags" %>
<caaers:message var="_syncpopmsg" code="ae.study.sync.message" text="Study is being synchronized with CTEP-ESYS. This popup will automatically close once the data is in sync. Thank you for your patience." />
<c:if test="${command.studyOutOfSync}">
    <div id="divStudySync" style="display: none;" class="info-box message"><p>${_syncpopmsg}</p></div>
    <script type="text/javascript">
        var studyDbIdVal = ${empty command.aeReport.study.id ? 0 : command.aeReport.study.id};
        var popupDiv;
        jQuery(document).ready(function() {
            popupDiv = new Window({destroyOnClose: false, className:"alphacube", width:600, height:150, zIndex:100, resizable:false, recenterAuto:true, draggable:false, closable:false, minimizable:false, maximizable:false});
            popupDiv.setContent("divStudySync");
            popupDiv.showCenter(true);
            popupDiv.show();
            createAE.syncStudyWithAdEERS(studyDbIdVal, function (_resultId) {
                setTimeout(function() {closePopup()}, 2 * 1000);
            });
        });

        function closePopup() {
            popupDiv.close();
        }
    </script>
</c:if>
