package de.oglimmer.game.logic.action;

import java.util.HashMap;
import java.util.Map;

import de.oglimmer.game.com.ComConst;

public class ActionFactory {

	private static final ActionFactory singleton = new ActionFactory();

	public static ActionFactory getInstance() {
		return singleton;
	}

	private Map<String, Action> cache = new HashMap<String, Action>();

	private ActionFactory() {
		cache.put(ComConst.ACTION_INIT, new InitAction());
		cache.put(ComConst.ACTION_REINIT, new ReinitAction());
		cache.put(ComConst.ACTION_SELECT, new SelectAction());
		cache.put(ComConst.ACTION_END_TURN, new EndTurnAction());
		cache.put(ComConst.ACTION_NO_OP, new NoOpAction());
	}

	public Action getAction(String actionId) {
		return cache.get(actionId);
	}
}
