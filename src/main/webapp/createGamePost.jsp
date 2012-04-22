<%@page import="de.oglimmer.game.logic.*"%>
<%
	Game game = GameManager.getInstance().createGame(new GameCreator());
	response.sendRedirect("board.jsp?gameId=" + game.getId());
%> 