<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>

<%@ taglib prefix="csmauthz" uri="http://csm.ncicb.nci.nih.gov/authz" %>

<html>
<head><title>Welcome to caAERS</title></head>
<body><chrome:box title="Task">
<chrome:division title="Regular Tasks">
    <table id="test" class="autoclear" width="100%">
        <tr class="results" >
            <td align="left" valign="top" width="30%">
                <c:forEach begin="0" end="1" items="${taskgroups}" var="taskGroup">
                    <csmauthz:accesscontrol domainObject="${taskGroup}"
                                            authorizationCheckName="taskGroupAuthorizationCheck">

                        <ul><chrome:division title="${taskGroup.displayName}">
                            <c:forEach items="${taskGroup.taskList}" var="task">
                                <c:if test="${test}"></c:if>
                                <csmauthz:accesscontrol domainObject="${task}"
                                                        authorizationCheckName="taskAuthorizationCheck">
                                    <li><a href="<c:url value="${task.url}"/>">${task.displayName}</a></li>
                                </csmauthz:accesscontrol>

                            </c:forEach>
                        </chrome:division>
                        </ul>

                    </csmauthz:accesscontrol>
                </c:forEach>

            </td>

            <td align="left" valign="top" width="30%">
                <c:forEach begin="2" end="3" items="${taskgroups}" var="taskGroup">
                    <csmauthz:accesscontrol domainObject="${taskGroup}"
                                            authorizationCheckName="taskGroupAuthorizationCheck">

                        <ul><chrome:division title="${taskGroup.displayName}">
                            <c:forEach items="${taskGroup.taskList}" var="task">
                                <c:if test="${test}"></c:if>
                                <csmauthz:accesscontrol domainObject="${task}"
                                                        authorizationCheckName="taskAuthorizationCheck">
                                    <li><a href="<c:url value="${task.url}"/>">${task.displayName}</a></li>
                                </csmauthz:accesscontrol>

                            </c:forEach>
                        </chrome:division>
                        </ul>
                    </csmauthz:accesscontrol>
                </c:forEach>

            </td>
            <td align="left" valign="top" width="30%">
                <c:forEach begin="4" end="4" items="${taskgroups}" var="taskGroup">
                    <csmauthz:accesscontrol domainObject="${taskGroup}"
                                            authorizationCheckName="taskGroupAuthorizationCheck">

                        <ul><chrome:division title="${taskGroup.displayName}">
                            <c:forEach items="${taskGroup.taskList}" var="task">
                                <c:if test="${test}"></c:if>
                                <csmauthz:accesscontrol domainObject="${task}"
                                                        authorizationCheckName="taskAuthorizationCheck">
                                    <li><a href="<c:url value="${task.url}"/>">${task.displayName}</a></li>
                                </csmauthz:accesscontrol>

                            </c:forEach>
                        </chrome:division>
                        </ul>
                    </csmauthz:accesscontrol>
                </c:forEach>

            </td>

        </tr>
    </table>

</chrome:division>
<chrome:division title="Setup and Administration Tasks">
    <table id="test" width="100%" >
        <tr class="results">
            <td align="left"  valign="top" width="30%">
                <c:forEach begin="5" end="6" items="${taskgroups}" var="taskGroup">
                    <csmauthz:accesscontrol domainObject="${taskGroup}"
                                            authorizationCheckName="taskGroupAuthorizationCheck">

                        <ul><chrome:division title="${taskGroup.displayName}">
                            <c:forEach items="${taskGroup.taskList}" var="task">
                                <c:if test="${test}"></c:if>
                                <csmauthz:accesscontrol domainObject="${task}"
                                                        authorizationCheckName="taskAuthorizationCheck">
                                    <li><a href="<c:url value="${task.url}"/>">${task.displayName}</a></li>
                                </csmauthz:accesscontrol>

                            </c:forEach>
                        </chrome:division>
                        </ul>
                    </csmauthz:accesscontrol>
                </c:forEach>

            </td>

            <td align="left" valign="top" width="30%">
                <c:forEach begin="7" end="8" items="${taskgroups}" var="taskGroup">
                    <csmauthz:accesscontrol domainObject="${taskGroup}"
                                            authorizationCheckName="taskGroupAuthorizationCheck">
                        <ul><chrome:division title="${taskGroup.displayName}">
                            <c:forEach items="${taskGroup.taskList}" var="task">
                                <csmauthz:accesscontrol domainObject="${task}"
                                                        authorizationCheckName="taskAuthorizationCheck">
                                    <li><a href="<c:url value="${task.url}"/>">${task.displayName}</a></li>
                                </csmauthz:accesscontrol>

                            </c:forEach>
                        </chrome:division>
                        </ul>
                    </csmauthz:accesscontrol>
                </c:forEach>

            </td>
            <td align="left" valign="top" width="30%">
                <c:forEach begin="9" end="10" items="${taskgroups}" var="taskGroup">
                    <csmauthz:accesscontrol domainObject="${taskGroup}"
                                            authorizationCheckName="taskGroupAuthorizationCheck">
                        <ul><chrome:division title="${taskGroup.displayName}">
                            <c:forEach items="${taskGroup.taskList}" var="task">
                                <c:if test="${test}"></c:if>
                                <csmauthz:accesscontrol domainObject="${task}"
                                                        authorizationCheckName="taskAuthorizationCheck">
                                    <li><a href="<c:url value="${task.url}"/>">${task.displayName}</a></li>
                                </csmauthz:accesscontrol>

                            </c:forEach>
                        </chrome:division>
                        </ul>
                    </csmauthz:accesscontrol>
                </c:forEach>

            </td>

        </tr>
    </table>
</chrome:division>


</chrome:box>
</body>
</html>