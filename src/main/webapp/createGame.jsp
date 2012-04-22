<!DOCTYPE html>
<%@page import="de.oglimmer.game.logic.Game"%>
<%@page import="de.oglimmer.game.logic.GameManager"%>
<html>
<head>
<meta charset=utf-8 />
<title>A Multiplayer Tower-Defense Fantasy Combat Game</title>
<!--[if IE]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<style>
</style>
<body>
	<h1>A Tower-Defense Fantasy Combat Game</h1>

	<div>
		Here you would select the map and your deck.<br /> <br /> <a
			href="createGamePost.jsp">Create and wait for other player</a>
	</div>
</body>
</html>
<% request.getRequestDispatcher("createGamePost.jsp").forward(request, response); %>