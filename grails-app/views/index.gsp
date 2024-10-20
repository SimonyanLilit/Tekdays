<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>TekDays - The Community is the Conference!</title>
		<style type="text/css" media="screen">







			h3 {
				margin-top: 1em;
				margin-bottom: 0.3em;
				margin-left: 20px;
				font-size: 1em;
			}

			p {
				line-height: 1.5;
				margin: 0.25em 0;
				margin-left: 20px;
				margin-right: 10%;
			}



		</style>
	</head>
<body>
<div id="welcome">
	<br />
	<h3>Welcome to TekDays.com</h3>
	<p>TekDays.com is a site dedicated to assisting individuals and
	communities to organize technology conferences. To bring great
	minds with common interests and passions together for the good
	of greater geekdom!</p>
</div>
<g:organizerEvents />
<g:volunteerEvents />
<div class="homeCell">
	<h3>Find a Tek Event</h3>
	<p> See if there's a technical event in the works that strikes your
	fancy. If there is, you can volunteer to help or just let the
	organizers know that you'd be interested in attending.
	Everybody has a role to play.</p>
	<span class="buttons" style="margin-left: 71%" >
		<g:link controller="tekEvent" action="index">Find a Tek Event</g:link>
	</span>
</div>
<div class="homeCell">
	<h3>Organize a Tek Event</h3>
	<p>If you don't see anything that suits your interest and location,
	then why not get the ball rolling. It's easy to get started and
	there may be others out there ready to get behind you to make it
	happen.</p>
	<span class="buttons" style="margin-left: 67%">
		<g:link controller="tekEvent" action="create"> Organize a Tek Event</g:link>



	</span>
</div>
<div class="homeCell">
	<h3>Sponsor a Tek Event</h3>
	<p>If you are part of a business or organization that is involved in
	technology then sponsoring a tek event would be a great way to
	let the community know that you're there and you're involved.</p>
	<span class="buttons" style="margin-left: 70%" >
		<g:link controller="sponsor" action="create">Sponsor a Tek Event</g:link>
	</span>
</div>
<div class="homeCell">
	<h3>Chat with another Tek Users </h3>
	<p> Chat with another tek users in the chatroom </p>
	<span class="buttons" style="margin-left: 71%" >
		<g:link controller="tekUser" action="chatroom">Go to the chatroom</g:link>
	</span>
</div>
</body>
</html>

