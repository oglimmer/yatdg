<%@page import="de.oglimmer.game.logic.Game"%>
<%@page import="de.oglimmer.game.logic.GameManager"%>
<%
	for (Game game : GameManager.getInstance().getOpenGames()) {
		out.println("&nbsp;- <a href='joinGame.jsp?gameId="
				+ game.getId() + "'>Join this game created "
				+ game.getCreationDate() + " ago</a><br/>");
	}
	if(GameManager.getInstance().getOpenGames().isEmpty()) {
		out.println("No open games");
	}
%>