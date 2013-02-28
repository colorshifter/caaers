<%--
Copyright SemanticBits, Northwestern University and Akaza Research

Distributed under the OSI-approved BSD 3-Clause License.
See http://ncip.github.com/caaers/LICENSE.txt for details.
--%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="ae" tagdir="/WEB-INF/tags/ae"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tags:noform>
<c:forEach items="${indexes}" var="index">
    <ae:onePriorTherapyAgent index="${index}" parentIndex="${parentIndex}" agent="${priorTherapyAgents[index] }"/>
</c:forEach>
</tags:noform>

