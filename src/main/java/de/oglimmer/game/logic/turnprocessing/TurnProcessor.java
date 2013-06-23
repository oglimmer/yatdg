package de.oglimmer.game.logic.turnprocessing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.Broadcaster;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.game.com.ComConst;
import de.oglimmer.game.com.UnitResponseBuilder;
import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.unit.Unit;
import de.oglimmer.game.util.ThreadHelper;

public class TurnProcessor implements Runnable {

	private static final Log log = LogFactory.getLog(TurnProcessor.class);

	private final Game game;
	private final Set<Unit> updatedUnits;
	private final Movement movement;
	private final MeleeBattle mb;
	private final RangedBattle rb;

	private final Broadcaster bc;

	public TurnProcessor(Game game, Broadcaster bc) {
		this.game = game;
		updatedUnits = new HashSet<Unit>();
		movement = new Movement(game, updatedUnits);
		mb = new MeleeBattle(game, updatedUnits);
		rb = new RangedBattle(game, updatedUnits);
		this.bc = bc;
	}

	@Override
	public void run() {
		try {
			log.debug("started asyncTurnEnd");

			sendNewUnitsToClients(bc);

			ThreadHelper.sleep(25);

			moveAndBattle(bc);

			game.newTurn(bc);

			game.setRunningMove(false);

			log.debug("ended asyncTurnEnd");
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
	}

	private void moveAndBattle(Broadcaster bc) throws JSONException {

		while (movement.anyPlayerHasMovingUnit()
				|| !mb.getAllBattlegrounds().isEmpty()) {

			movement.doMoveTurn();
			sendNewPosToClients(bc);
			updatedUnits.clear();

			doBattle(bc);
			sendNewPosToClients(bc);
			updatedUnits.clear();
			ThreadHelper.sleep(25);
		}
	}

	private void doBattle(Broadcaster bc) {
		rb.doRangedBattles(bc);
		mb.doMeleeBattles(bc);
	}

	private void sendNewUnitsToClients(Broadcaster bc) throws JSONException {
		Map<Player, Set<Unit>> playerToUnits = new HashMap<Player, Set<Unit>>();
		for (Player player : game.getPlayers()) {
			for (Unit unit : player.getUnits()) {
				addNewlyDeployedUnitsToUpdateMap(playerToUnits, player, unit);
			}
		}
		for (Player player : playerToUnits.keySet()) {
			sendUnitDataToClient(player, playerToUnits.get(player),
					ComConst.RO_NEWUNITS,bc);
		}
	}

	private void addNewlyDeployedUnitsToUpdateMap(
			Map<Player, Set<Unit>> playerToUnits, Player player, Unit unit) {
		if (unit.getNextPoint() != null) {
			unit.setPoint(unit.getNextPoint());
			unit.setNextPoint(null);
			addUnitToNewUnitsMap(playerToUnits, game.getPlayers()
					.getOtherPlayer(player), unit);
		}
	}

	private void addUnitToNewUnitsMap(Map<Player, Set<Unit>> playerToUnits,
			Player player, Unit unit) {

		Set<Unit> set = playerToUnits.get(player);
		if (set == null) {
			set = new HashSet<Unit>();
			playerToUnits.put(player, set);
		}
		set.add(unit);
	}

	private void sendNewPosToClients(Broadcaster bc) throws JSONException {
		if (!updatedUnits.isEmpty()) {
			for (Player player : game.getPlayers()) {
				sendUnitDataToClient(player, updatedUnits,
						ComConst.RO_UPDATEUNITS, bc);
			}
		}
	}

	private static void sendUnitDataToClient(Player player,
			Set<Unit> updatedUnits, String key, Broadcaster bc) throws JSONException {

		JSONObject message = new JSONObject();

		message.put(ComConst.RO_SHOWBATTLE, false);
		message.put(ComConst.RO_HELPTEXT, ComConst.MSG_MOVEMENT);

		JSONArray arrUnits = new JSONArray();
		message.put(key, arrUnits);

		for (Unit unit : updatedUnits) {
			if (ComConst.RO_NEWUNITS.equals(key)) {
				arrUnits.put(UnitResponseBuilder.createUnit(unit, player));
			} else {
				arrUnits.put(UnitResponseBuilder.updateUnit(unit, player));
			}
		}

		// Server.getInstance().send(player, message);
		bc.broadcast(message.toString(), player.getAtmosphereResource());
	}

}
