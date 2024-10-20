
<%@ page import="com.tekdays.TekUser" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tekUser.label', default: 'TekUser')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
%{--		<script>--}%
%{--			function callDelete() {--}%
%{--				if (confirm("Are you sure? ")) {--}%
%{--					$.ajax({--}%
%{--						type: "POST",--}%
%{--						url: "/TekDays/tekUser/delete/",--}%

%{--						cache: false,--}%
%{--						success: function () {--}%
%{--							alert("The user has been deleted!")--}%
%{--						},--}%
%{--						error: function () {--}%
%{--							console.log('Error during getting services to be added for partner with id: ' + $(this).children("option:selected").val());--}%
%{--							alert("This user can not be deleted")--}%
%{--						}--}%
%{--					});--}%
%{--				}--}%
%{--			}--}%
%{--		</script>--}%
	</head>
	<body>
	<a href="#show-tekUser" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-tekUser" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list tekUser">
			
				<g:if test="${tekUserInstance?.fullName}">
				<li class="fieldcontain">
					<span id="fullName-label" class="property-label"><g:message code="tekUser.fullName.label" default="Full Name" /></span>
					
						<span class="property-value" aria-labelledby="fullName-label"><g:fieldValue bean="${tekUserInstance}" field="fullName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${tekUserInstance?.userName}">
				<li class="fieldcontain">
					<span id="userName-label" class="property-label"><g:message code="tekUser.userName.label" default="User Name" /></span>
					
						<span class="property-value" aria-labelledby="userName-label"><g:fieldValue bean="${tekUserInstance}" field="userName"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${tekUserInstance?.email}">
				<li class="fieldcontain">
					<span id="email-label" class="property-label"><g:message code="tekUser.email.label" default="Email" /></span>
					
						<span class="property-value" aria-labelledby="email-label"><g:fieldValue bean="${tekUserInstance}" field="email"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${tekUserInstance?.website}">
				<li class="fieldcontain">
					<span id="website-label" class="property-label"><g:message code="tekUser.website.label" default="Website" /></span>
					
						<span class="property-value" aria-labelledby="website-label"><g:fieldValue bean="${tekUserInstance}" field="website"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${tekUserInstance?.bio}">
				<li class="fieldcontain">
					<span id="bio-label" class="property-label"><g:message code="tekUser.bio.label" default="Bio" /></span>
					
						<span class="property-value" aria-labelledby="bio-label"><g:fieldValue bean="${tekUserInstance}" field="bio"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${tekUserInstance?.password}">
				<li class="fieldcontain">
					<span id="password-label" class="property-label"><g:message code="tekUser.password.label" default="Password" /></span>
					
						<span class="property-value" aria-labelledby="password-label"><g:fieldValue bean="${tekUserInstance}" field="password"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:tekUserInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${tekUserInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
%{--					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="callDelete()" />--}%
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}"/>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
