<!DOCTYPE html>
<html>
<head>
<meta charset=utf-8 />
<title>A Multiplayer Tower-Defense Fantasy Combat Game</title>
<script
	src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<style>
html,body {
	height: 100%;
}

#wrap {
	min-height: 100%;
}

#main {
	overflow: auto;
	padding-bottom: 30px; /* must be same height as the footer */
}

#footer {
	position: relative;
	margin-top: -30px; /* negative value of footer height */
	height: 30px;
	clear: both;
}

/*Opera Fix*/
body:before {
	content: "";
	height: 100%;
	float: left;
	width: 0;
	margin-top: -32767px;
}
</style>
<body>
	<div id="wrap">
		<div id="main">

			<h1>A Multiplayer Tower-Defense Fantasy Combat Game (V0.2)</h1>

			<div style="margin-bottom: 20px;">
				Feedback appreciated: <a
					href="mailto:&#103;&#97;&#109;&#101;&#64;&#111;&#103;&#108;&#105;&#109;&#109;&#101;&#114;&#46;&#100;&#101;">&#103;&#97;&#109;&#101;&#64;&#111;&#103;&#108;&#105;&#109;&#109;&#101;&#114;&#46;&#100;&#101;</a>
			</div>

			<div id="startGameDiv" style="margin-bottom: 10px;">
        We have stored 2 session cookies on your computer. They don't store any personal information and are just used to identify your computer on subsequent http calls unless you will close your browser. If you are not okay with those cookies, just leave this page and your browser will delete them as soon as you terminate your browser application.<br/>
				<a href="createGame.jsp">Create new game and wait for another
					player</a>
			</div>

			<div id="joinGameDiv">
				Currently available games:<br />
				<div id="openGamesDiv"></div>
			</div>
		</div>

	</div>

	<div id="footer">Copyright 2013-2018 by oglimmer.de - Oliver Zimpasser -
		All rights reserved.</div>

</body>
<script type="text/javascript">
	reload();
	function reload() {
		$.get("openGames.jsp", function(data) {
			$("#openGamesDiv").html(data);
			setTimeout("reload();", 2000);
		});
	}
	
	setCookie("playerId", "");
	function setCookie(cookieName, cookieValue) {
	 document.cookie=cookieName + "=" + escape(cookieValue);
	};
</script>
</html>