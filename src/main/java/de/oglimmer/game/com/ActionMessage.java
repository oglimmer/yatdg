package de.oglimmer.game.com;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.AtmosphereResource;
import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.GameManager;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.UIElement;
import de.oglimmer.game.logic.action.Action;
import de.oglimmer.game.logic.action.ActionFactory;

public class ActionMessage implements Runnable {
	private static final Log log = LogFactory.getLog(ActionMessage.class);

	public static ActionMessage getInstance(JSONObject message,
			AtmosphereResource r) throws JSONException {
		return new ActionMessage(message, r);
	}

	private Game game;

	private Player player;

	private Action action;

	private UIElement uiElement;

	private JSONObject parameters;

	private AtmosphereResource r;

	private ActionMessage(JSONObject message, AtmosphereResource r)
			throws JSONException {
		initGame(message);
		initPlayer(message);
		initAction(message);
		initUiElement(message);
		initParameters(message);
		this.r = r;
	}

	private void initParameters(JSONObject message) {
		this.parameters = message.optJSONObject(ComConst.RO_PARAMETERS);
	}

	private void initUiElement(JSONObject message) {
		String uiElementId = message.optString(ComConst.RO_UIELEMENTID);
		if (uiElementId != null && !uiElementId.isEmpty()) {
			this.uiElement = this.game.getUiElement(uiElementId);
		}
	}

	private void initAction(JSONObject message) throws JSONException {
		String actionId = message.getString(ComConst.RO_ACTIONID);
		this.action = ActionFactory.getInstance().getAction(actionId);
	}

	private void initPlayer(JSONObject message) {
		String playerId = message.optString(ComConst.RO_PLAYERID);
		if (playerId != null && !playerId.isEmpty()) {
			this.player = this.game.getPlayers().getPlayer(playerId);
		}
	}

	private void initGame(JSONObject message) throws JSONException {
		String gameId = message.getString(ComConst.RO_GAMEID);
		this.game = GameManager.getInstance().getGame(gameId);
	}

	public Game getGame() {
		return game;
	}

	public Player getPlayer() {
		return player;
	}

	public Action getAction() {
		return action;
	}

	public JSONObject getParameters() {
		return parameters;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public UIElement getUiElement() {
		return uiElement;
	}

	@Override
	public void run() {
		try {
			getAction().execute(getGame(), getPlayer(), getUiElement(),
					getParameters(), r.getBroadcaster());
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
	}

}
