package de.oglimmer.game.logic.action;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.game.Server;
import de.oglimmer.game.com.ComConst;
import de.oglimmer.game.com.UnitResponseBuilder;
import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.UIElement;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.unit.Unit;

public class EndTurnAction implements Action {

	@Override
	public void execute(Game game, Player player, UIElement uiElement,
			JSONObject parameters) throws JSONException {

		player.getUnits().deactivateSelectables();
		player.getDeployFields().deactivate();

		updateClient(game, player);

		game.endTurn(player);

	}

	private void updateClient(Game game, Player player) throws JSONException {
		JSONObject message = new JSONObject();

		JSONArray arrUnits = new JSONArray();
		message.put(ComConst.RO_UPDATEUNITS, arrUnits);
		for (Unit u : player.getUnits()) {
			arrUnits.put(UnitResponseBuilder.updateUnit(u, player));
		}

		JSONArray arrFields = new JSONArray();
		message.put(ComConst.RO_UPDATEFIELDS, arrFields);
		for (Field f : game.getBoard().getGameMap().getDeployFields(player)) {
			arrFields.put(UnitResponseBuilder.updateField(f, player));
		}

		message.put(ComConst.RO_ENABLEENDTURN, false);
		message.put(ComConst.RO_HELPTEXT, ComConst.MSG_WAIT);

		Server.getInstance().send(player, message);
	}

}
