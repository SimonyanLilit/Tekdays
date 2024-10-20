
<%@ page import="com.tekdays.TekEvent" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tekEvent.label', default: 'TekEvent')}" />
%{--		<g:javascript library="jquery" />--}%
%{--		<r:require module="jquery-ui" />--}%
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>

	<body>

	<a href="#show-tekEvent" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>

				<g:if test="${session.user}">
					<li>
						<g:link class="list" controller="dashboard" action="dashboard" id="${tekEventInstance.id}"> Event Dashboard</g:link>
					</li>
				</g:if>

				<li><g:volunteerButton eventId="${tekEventInstance.id}"/> </li>
			</ul>
		</div>
		<div id="show-tekEvent" class="content scaffold-show" role="main">
			<h1>${tekEventInstance?.name}</h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list tekEvent">

%{--				<g:if test="${tekEventInstance?.name}">--}%
%{--				<li class="fieldcontain">--}%
%{--					<span id="name-label" class="property-label"><g:message code="tekEvent.name.label" default="Name" /></span>--}%
%{--					--}%
%{--						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${tekEventInstance}" field="name"/></span>--}%
%{--					--}%
%{--				</li>--}%
%{--				</g:if>--}%
%{--			--}%
				<g:if test="${tekEventInstance?.description}">
					<li class="fieldcontain">
						<span id="description-label" class="property-label"><g:message code="tekEvent.description.label" default="Description" /></span>

						<span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${tekEventInstance}" field="description"/></span>

					</li>
				</g:if>
				<g:if test="${tekEventInstance?.city}">
				<li class="fieldcontain">
					<span id="city-label" class="property-label">Location</span>

						<span class="property-value" aria-labelledby="city-label">
							<g:fieldValue bean="${tekEventInstance}" field="venue"/>
							<g:fieldValue bean="${tekEventInstance}" field="city"/>
						</span>

				</li>
				</g:if>


				<g:if test="${tekEventInstance?.startDate}">
				<li class="fieldcontain">
					<span id="startDate-label" class="property-label"><g:message code="tekEvent.startDate.label" default="Start Date" /></span>

						<span class="property-value" aria-labelledby="startDate-label">
							<g:formatDate format="MMMM dd, yyyy"
										  date="${tekEventInstance?.startDate}" /></span>

				</li>
				</g:if>

				<g:if test="${tekEventInstance?.endDate}">
				<li class="fieldcontain">
					<span id="endDate-label" class="property-label"><g:message code="tekEvent.endDate.label" default="End Date" /></span>

						<span class="property-value" aria-labelledby="endDate-label">
							<g:formatDate format="MMMM dd, yyyy" date="${tekEventInstance?.endDate}" /></span>

				</li>
				</g:if>

				<g:if test="${tekEventInstance?.sponsorships}">
				<li class="fieldcontain">
					<span id="sponsorships-label" class="property-label"><g:message code="tekEvent.sponsorships.label" default="Sponsorships" /></span>

						<g:each in="${tekEventInstance.sponsorships}" var="s">
						<span class="property-value" aria-labelledby="sponsorships-label">
							<g:link controller="sponsorship" action="show" id="${s.id}">${s?.sponsor.encodeAsHTML()}</g:link></span>
						</g:each>

				</li>
				</g:if>

%{--				<g:if test="${tekEventInstance?.tasks}">--}%
%{--				<li class="fieldcontain">--}%
%{--					<span id="tasks-label" class="property-label"><g:message code="tekEvent.tasks.label" default="Tasks" /></span>--}%
%{--					--}%
%{--						<g:each in="${tekEventInstance.tasks}" var="t">--}%
%{--						<span class="property-value" aria-labelledby="tasks-label"><g:link controller="task" action="show" id="${t.id}">${t?.encodeAsHTML()}</g:link></span>--}%
%{--						</g:each>--}%
%{--					--}%
%{--				</li>--}%
%{--				</g:if>--}%
%{--			--}%
%{--				<g:if test="${tekEventInstance?.messages}">--}%
%{--				<li class="fieldcontain">--}%
%{--					<span id="messages-label" class="property-label"><g:message code="tekEvent.messages.label" default="Messages" /></span>--}%

%{--						<g:each in="${tekEventInstance.messages}" var="m">--}%
%{--						<span class="property-value" aria-labelledby="messages-label"><g:link controller="tekMessage" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></span>--}%
%{--						</g:each>--}%

%{--				</li>--}%
%{--				</g:if>--}%

				<g:if test="${tekEventInstance?.respondents}">
				<li class="fieldcontain">
					<span id="respondents-label" class="property-label"><g:message code="tekEvent.respondents.label" default="Respondents" /></span>

						<span class="property-value" aria-labelledby="respondents-label"><g:fieldValue bean="${tekEventInstance}" field="respondents"/></span>

				</li>
				</g:if>



				<g:if test="${tekEventInstance?.organizer}">
					<li class="fieldcontain">
						<span id="organizer-label" class="property-label"><g:message code="tekEvent.organizer.label" default="Organizer" /></span>

						<span class="property-value" aria-labelledby="organizer-label"><g:link controller="tekUser" action="show" id="${tekEventInstance?.organizer?.id}">${tekEventInstance?.organizer?.encodeAsHTML()}</g:link></span>

					</li>
				</g:if>
				<g:if test="${tekEventInstance?.tasks}">
					<li class="fieldcontain">
						<span id="tasks-label" class="property-label"><g:message
								code="tekEvent.tasks.label" default="Tasks" /></span>
						<g:each in="${tekEventInstance.tasks}" var="t">
							<span class="property-value" aria-labelledby="tasks-label">
								<g:link controller="task" action="show"
										id="${t.id}">${t.title}</g:link></span>
						</g:each>
					</li>
				</g:if>
				<g:if test="${tekEventInstance?.messages}">
					<li class="fieldcontain">
						<span id="messages-label" class="property-label"><g:message
								code="tekEvent.messages.label" default="Messages" /></span>
						<span class="property-value" aria-labelledby="messages-label">
							<g:link controller="tekMessage" action="index"
									id="${tekEventInstance.id}">
								View Messages
							</g:link></span>
					</li>
				</g:if>
				<g:if test="${tekEventInstance?.volunteers}">
				<li class="fieldcontain">
					<span id="volunteers-label" class="property-label"><g:message code="tekEvent.volunteers.label" default="Volunteers" /></span>

						<g:each in="${tekEventInstance.volunteers}" var="v">
						<span class="property-value" aria-labelledby="volunteers-label"><g:link controller="tekUser" action="show" id="${v.id}">${v?.encodeAsHTML()}</g:link></span>
						</g:each>

				</li>
				</g:if>



			</ol>
			<g:form url="[resource:tekEventInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${tekEventInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>

	<script type="text/javascript">
		$(document).ready(function() {
			$('#volunteerDialog').hide();
			$( "#volunteerButton" ).click(function() {
				$("#volunteerDialog").dialog({
					resizable: false,
					height: 180,
					width: 420,
					modal: false,
					buttons: {
						"Submit": function() {
							$.ajax({
								type: "post",
								dataType: "html",
								url: "${g.createLink(action:'volunteer')}",
								async: false,
								data: $("#volunteerForm").serialize(),
								success: function (response, status, xml) {

									$("#volunteerSpan").html(response);
								}
							});
							$(this).dialog("close");
						},
						Cancel: function() {

							$(this).dialog( "close" );
						}
					}
				});
			});
		});

	</script>
	<div id="volunteerDialog" title="Volunteer for ${tekEventInstance.name}">
		<g:form name="volunteerForm" action="volunteer">
			<g:hiddenField name="id" value="${tekEventInstance.id}" />
			<p>Welcome to the team! Your help will make a huge difference.</p>
		</g:form>
	</div>
	</body>
</html>
