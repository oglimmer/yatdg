<!DOCTYPE html>
<html>
<head>
<meta charset=utf-8 />
<title>A Multiplayer Tower-Defense Fantasy Combat Game</title>
<!--[if IE]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<style>
article,aside,figure,footer,header,hgroup,menu,nav,section {
	display: block;
}

body,canvas,div {
	-webkit-touch-callout: none;
	-webkit-user-select: none;
	-khtml-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

#overlay {
	visibility: hidden;
	position: absolute;
	left: 0px;
	top: 0px;
	width: 100%;
	height: 100%;
	text-align: center;
	z-index: 1000;
	background-color: #000;
	opacity: 0.9;
}

#overlay div {
	width: 300px;
	margin: 100px auto;
	background-color: #fff;
	border: 1px solid #000;
	padding: 15px;
	text-align: center;
}
</style>
</head>
<body onunload="window.atmosphere.unsubscribe();">
	<div>
		<button id="endTurnButton" onclick="endTurn();">No more deployments - end turn</button>
	</div>
	<div style="width: 750px; height: 50px;">
		<span id="helpText"></span>
	</div>
	<canvas id="board" width="785" height="355" style="background: #f1f1f1"></canvas>
	<canvas id="hand" width="65" height="250" style="background: #f1f1f1"></canvas>
	<canvas id="info" width="200" height="300" style="background: #f1f1f1"></canvas>
	<div id="overlay">
		<div id="overlayText"></div>
	</div>
</body>
<script type="text/javascript">
var gameId = "<%=request.getParameter("gameId")%>";
</script>
<script src="javascript/atmosphere-min.js"></script>
<script src="code.js?foo=<%=Math.random()%>"></script>

</html>