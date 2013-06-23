package de.oglimmer.game.logic.action;

import org.atmosphere.cpr.Broadcaster;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.game.com.ComConst;
import de.oglimmer.game.com.UnitResponseBuilder;
import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.UIElement;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.unit.Unit;

public class SelectAction implements Action {

	@Override
	public void execute(Game game, Player player, UIElement uiElement,
			JSONObject parameters, Broadcaster bc) throws JSONException {

		boolean first;
		if (player.getCurrentlySelected() == null) {
			handleFirstClick(game, player, uiElement);
			first = true;
		} else {
			handleSecondClick(game, player, uiElement);
			first = false;
		}

		sendUpdatedUnitsFieldsToClient(game, player, first, bc);

		player.checkForEndTurn(bc);
	}

	private void handleFirstClick(Game game, Player player, UIElement uiElement)
			throws JSONException {

		assert uiElement instanceof Unit;

		player.setCurrentlySelected((Unit) uiElement);

		player.getUnits().deactivateExceptToDeploy();
		player.getDeployFields().enablePotentialDeployFields();
	}

	private void handleSecondClick(Game game, Player player, UIElement uiElement)
			throws JSONException {

		// skip for de-select of selected unit
		if (uiElement != player.getCurrentlySelected()) {

			Field f = (Field) uiElement;
			Unit u = player.getCurrentlySelected();

			u.setNextPoint(f.getPoint());
		}

		player.setCurrentlySelected(null);

		player.getUnits().enablePotential();
		player.getDeployFields().deactivate();
	}

	private void sendUpdatedUnitsFieldsToClient(Game game, Player player,
			boolean firstClick, Broadcaster bc) throws JSONException {
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

		message.put(ComConst.RO_HELPTEXT,
				(firstClick ? ComConst.MSG_AFTER_SELECT
						: ComConst.MSG_AFTER_DEPLOY));

		// Server.getInstance().send(player, message);
		bc.broadcast(message.toString(), player.getAtmosphereResource());
	}
}
