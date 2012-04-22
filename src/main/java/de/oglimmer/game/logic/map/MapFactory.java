package de.oglimmer.game.logic.map;

public class MapFactory {

	private static final MapFactory singleton = new MapFactory();

	public static MapFactory getInstance() {
		return singleton;
	}

	public GameMap getGameMap(String id) {
		return new DefaultMap();
	}

}
