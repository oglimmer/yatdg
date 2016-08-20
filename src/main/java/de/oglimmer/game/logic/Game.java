package de.oglimmer.game.logic;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.atmosphere.cpr.Broadcaster;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.game.com.ComConst;
import de.oglimmer.game.com.UnitResponseBuilder;
import de.oglimmer.game.logic.board.Board;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.map.GameMap;
import de.oglimmer.game.logic.map.MapFactory;
import de.oglimmer.game.logic.turnprocessing.TurnProcessor;
import de.oglimmer.game.logic.unit.Unit;
import de.oglimmer.utils.date.DateHelper;
import de.oglimmer.utils.random.RandomString;

public class Game {

	private static final Logger log = LoggerFactory.getLogger(Game.class);

	private final String id;

	private final Players players = new Players(this);

	private final Board board;

	private final ExecutorService executorService = Executors
			.newFixedThreadPool(1);

	private final Date creationDate;

	private boolean active;

	private boolean runningMove;

	private int turnNo;

	public Game(GameCreator gameCreator) {
		this.id = RandomString.getRandomStringHex(16);
		this.creationDate = new Date();
		GameMap gameMap = MapFactory.getInstance().getGameMap(
				gameCreator.getMapId());
		board = gameMap.createBoard(this, players);
		gameCreator.createDefaultUnits(this, players);
		for (Player player : players) {
			player.getUnits().enablePotential();
		}
		log.debug("Created game " + getId());
	}

	public String getId() {
		return id;
	}

	public Board getBoard() {
		return board;
	}

	public Players getPlayers() {
		return players;
	}

	public void setRunningMove(boolean runningMove) {
		this.runningMove = runningMove;
	}

	public int getTurnNo() {
		return turnNo;
	}

	public String getCreationDate() {
		return DateHelper.formatDateDifference(new Date(), creationDate);
	}

	public boolean isRunning() {
		return turnNo < 5 && board.anyUnitInHandArea();
	}

	public void activate() {
		active = true;
	}

	public boolean isActive() {
		return active;
	}

	public void endTurn(Player player, Broadcaster bc) throws JSONException {

		player.setDone(true);

		if (players.get0().isDone() && players.get1().isDone() && !runningMove) {
			runningMove = true;
			TurnProcessor mp = new TurnProcessor(this, bc);
			executorService.submit(mp);
		}
	}

	public void newTurn(Broadcaster bc) throws JSONException {

		turnNo++;
		if (!isRunning()) {
			endGame(bc);
		} else {
			for (Player player : players) {
				player.setDone(false);
				player.getUnits().enablePotential();
			}
			sendNewTurnToClients(bc);
		}
	}

	public UIElement getUiElement(String uiElementId) {
		UIElement retObj = board.getUiElement(uiElementId);
		if (retObj == null) {
			retObj = players.getUiElement(uiElementId);
		}
		return retObj;
	}

	private void endGame(Broadcaster bc) throws JSONException {
		Field castle = getBoard().getGameMap().getCastle();

		Unit castleUnit = castle.getUnit();

		JSONObject o = new JSONObject();
		o.put(ComConst.RO_ENDGAME, true);
		if (castleUnit == null) {
			processEndGameTie(o, bc);
		} else {
			processEndGameWinLoss(castleUnit, o, bc);
		}
		GameManager.getInstance().removeGame(this);
	}

	private void processEndGameWinLoss(Unit u, JSONObject o, Broadcaster bc)
			throws JSONException {
		o.put(ComConst.RO_ENDGAMETEXT,
				"You won!<br/><a href='index.jsp'>Start over</a>");
		// Server.getInstance().send(u.getOwningPlayer(), o);
		bc.broadcast(o.toString(), u.getOwningPlayer().getAtmosphereResource());
		o.put(ComConst.RO_ENDGAMETEXT,
				"You lose!<br/><a href='index.jsp'>Start over</a>");
		// Server.getInstance().send(players.getOtherPlayer(u.getOwningPlayer()),
		// o);
		bc.broadcast(o.toString(), players.getOtherPlayer(u.getOwningPlayer())
				.getAtmosphereResource());
	}

	private void processEndGameTie(JSONObject o, Broadcaster bc)
			throws JSONException {
		o.put(ComConst.RO_ENDGAMETEXT,
				"Tie. Nobody won.<br/><a href='index.jsp'>Start over</a>");
		for (Player player : getPlayers()) {
			// Server.getInstance().send(player, o);
			bc.broadcast(o.toString(), player.getAtmosphereResource());
		}
	}

	private void sendNewTurnToClients(Broadcaster bc) throws JSONException {
		for (Player player : getPlayers()) {
			JSONObject message = new JSONObject();

			JSONArray arrUnits = new JSONArray();
			message.put(ComConst.RO_UPDATEUNITS, arrUnits);
			for (Unit u : player.getUnits()) {
				arrUnits.put(UnitResponseBuilder.updateUnit(u, player));
			}

			JSONArray arrFields = new JSONArray();
			message.put(ComConst.RO_UPDATEFIELDS, arrFields);
			for (Field f : getBoard().getGameMap().getDeployFields(player)) {
				arrFields.put(UnitResponseBuilder.updateField(f, player));
			}
			message.put(ComConst.RO_ENABLEENDTURN, true);
			message.put(ComConst.RO_HELPTEXT, ComConst.MSG_NEW_TURN);

			// Server.getInstance().send(player, message);
			bc.broadcast(message.toString(), player.getAtmosphereResource());

			player.checkForEndTurn(bc);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + "]";
	}

}
