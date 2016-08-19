package de.oglimmer.game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameManager {

	private static final Logger log = LoggerFactory.getLogger(GameManager.class);

	private final static GameManager singleton = new GameManager();

	public static GameManager getInstance() {
		return singleton;
	}

	private Map<String, Game> games = new HashMap<String, Game>();

	public Game createGame(GameCreator gc) {
		Game game = new Game(gc);
		games.put(game.getId(), game);
		return game;
	}

	public Game getGame(String id) {
		Game game = games.get(id);
		if (game == null) {
			game = createGame(new GameCreator());
			//throw new GameException("There is no game with id = " + id);
		}
		return game;
	}

	public List<Game> getOpenGames() {
		List<Game> retList = new ArrayList<Game>();
		for (Game game : games.values()) {
			if (!game.getPlayers().isBothPlayersRegistered() && game.isActive()) {
				retList.add(game);
			}
		}
		return retList;
	}

	public void clear() {
		games.clear();
	}

	public void removeGame(Game game) {
		log.debug("Removed game:" + game.getId());
		games.remove(game.getId());
	}
}
