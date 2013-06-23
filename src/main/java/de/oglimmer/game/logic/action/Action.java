package de.oglimmer.game.logic.action;

import org.atmosphere.cpr.Broadcaster;
import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.UIElement;

public interface Action {

	void execute(Game game, Player player, UIElement uiElement,
			JSONObject parameters, Broadcaster bc) throws JSONException;

}
