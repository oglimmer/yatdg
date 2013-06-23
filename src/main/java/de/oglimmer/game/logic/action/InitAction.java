package de.oglimmer.game.logic.action;

import org.atmosphere.cpr.Broadcaster;
import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.game.com.ComConst;
import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;

public class InitAction extends ReinitAction {

	protected void addData(Game game, Player player, JSONObject retData,
			Broadcaster bc) throws JSONException {

		game.activate();

		retData.put(ComConst.RO_PLAYERID, player.getId());
		super.addData(game, player, retData, bc);
	}

}
