<%@ page import="com.tekdays.TekMessage" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'tekMessage.label',
            default: 'TekMessage')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <g:javascript library="jquery" />
</head>
<body>
<a href="#list-tekMessage" class="skip" tabindex="-1"><g:message
        code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message
                code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"
                    params='["tekEvent.id":"${event?.id}"]'><g:message
                    code="default.new.label" args="[entityName]" /></g:link></li>

    </ul>
</div>
<div id="list-tekMessage" class="content scaffold-list" role="main">
    <h1>${event?.name} - Forum Messages</h1>
    <div id="messageList">
        <g:messageThread messages="${tekMessageInstanceList}" />
    </div>
    <h3>Message Details</h3>
    <div id="details">
    </div>
</div>
</body>
</html>