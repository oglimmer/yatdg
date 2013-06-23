<!DOCTYPE html>
<html>
<head>
<meta charset=utf-8 />
<title>A Multiplayer Tower-Defense Fantasy Combat Game</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
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
				<a href="createGame.jsp">Create new game and wait for another
					player</a>
			</div>

			<div id="joinGameDiv">
				Currently available games:<br />
				<div id="openGamesDiv"></div>
			</div>
		</div>

	</div>

	<div id="footer">Copyright 2012 by oglimmer.de - Oliver Kurt -
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
	function
				setCookie(cookieName, cookieValue, nDays) {
		var today=new
				Date();
		var expire=new Date();
		if (nDays== null || nDays==
				0) {
			nDays=1;
				}
		expire.setTime(today.getTime() + 3600000 * 24 * nDays);
		document.cookie=cookieName
				+ "=" + escape(cookieValue) + ";expires="
				+ expire.toGMTString();
	};
</script>
</html>