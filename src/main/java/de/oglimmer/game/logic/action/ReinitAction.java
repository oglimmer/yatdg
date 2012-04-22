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
import de.oglimmer.game.logic.board.Area;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.unit.Unit;

public class ReinitAction implements Action {

	@Override
	public void execute(Game game, Player player, UIElement uiElement,
			JSONObject parameters) throws JSONException {

		JSONObject retData = new JSONObject();
		addData(game, player, retData);
		Server.getInstance().send(player, retData);

	}

	protected void addData(Game game, Player player, JSONObject retData)
			throws JSONException {

		handleNewFields(game, player, retData);

		handleWaitDialog(game, player, retData);

		handleNewUnits(game, player, retData);

		retData.put(ComConst.RO_HELPTEXT, ComConst.MSG_INITIAL);
	}

	private void handleNewUnits(Game game, Player player, JSONObject retData)
			throws JSONException {
		JSONArray arr = new JSONArray();
		retData.put(ComConst.RO_NEWUNITS, arr);
		for (Unit u : game.getPlayers().getOtherPlayer(player).getUnits()) {
			if (u.getPoint(player).getArea() == Area.BOARD) {
				JSONObject jsonField = UnitResponseBuilder
						.createUnit(u, player);
				arr.put(jsonField);
			}
		}
		for (Unit u : player.getUnits()) {
			JSONObject jsonField = UnitResponseBuilder.createUnit(u, player);
			arr.put(jsonField);
		}
	}

	private void handleWaitDialog(Game game, Player player, JSONObject retData)
			throws JSONException {
		if (!game.getPlayers().isBothPlayersRegistered()) {
			retData.put(ComConst.RO_SHOW_WAIT_DIALOG, true);
			retData.put(ComConst.RO_WAIT_DIALOG_TEXT,
					"Waiting for another player");
		} else {
			JSONObject infoOtherPlayer = new JSONObject();
			infoOtherPlayer.put(ComConst.RO_SHOW_WAIT_DIALOG, false);
			Server.getInstance().send(game.getPlayers().getOtherPlayer(player),
					infoOtherPlayer);
		}
	}

	private void handleNewFields(Game game, Player p, JSONObject retData)
			throws JSONException {
		JSONArray arr = new JSONArray();
		retData.put(ComConst.RO_NEWFIELDS, arr);
		for (Field f : game.getBoard().getFields()) {
			JSONObject jsonField = UnitResponseBuilder.createField(f, p);
			arr.put(jsonField);
		}
	}

}
