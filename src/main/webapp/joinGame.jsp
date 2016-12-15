<!DOCTYPE html>
<%@page import="de.oglimmer.game.logic.Game"%>
<%@page import="de.oglimmer.game.logic.GameManager"%>
<html>
<head>
<meta charset=utf-8 />
<title>A Multiplayer Tower-Defense Fantasy Combat Game</title>
<body>
	<h1>A Tower-Defense Fantasy Combat Game</h1>

	<div>
		Here you would your deck.<br /> <br /> <a
			href="joinGamePost.jsp?gameId=<%=request.getParameter("gameId")%>">join game</a>
	</div>
</body>
</html>
<% request.getRequestDispatcher("joinGamePost.jsp").forward(request, response); %>