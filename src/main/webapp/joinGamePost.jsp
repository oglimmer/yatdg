<%@page import="de.oglimmer.game.logic.*"%>
<%
	//Game game = GameManager.getInstance().createGame(new GameCreator());
	Game game = GameManager.getInstance().getGame(request.getParameter("gameId"));
	if (game.getPlayers().isBothPlayersRegistered()) {
		response.sendRedirect("index.jsp");
	} else {
		response.sendRedirect("board.jsp?gameId=" + request.getParameter("gameId"));
	}
%>
