package de.oglimmer.game.logic.board;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.Point;
import de.oglimmer.game.logic.UIElement;
import de.oglimmer.game.logic.map.GameMap;
import de.oglimmer.game.logic.unit.Unit;

public class Board {

	private static final Log log = LogFactory.getLog(Board.class);

	private final Game game;

	private final GameMap gameMap;

	private final Fields fields = new Fields();

	public Board(Game game, GameMap gameMap) {
		this.game = game;
		this.gameMap = gameMap;
		log.debug("Created Board");
	}

	public Game getGame() {
		return game;
	}

	public UIElement getUiElement(String uiElementId) {
		UIElement retObj = null;
		for (Field field : fields) {
			if (field.getId().equals(uiElementId)) {
				retObj = field;
			}
		}
		return retObj;
	}

	public Iterable<Field> getFields() {
		return fields;
	}

	public Fields getFieldsClone() {
		return (Fields) fields.clone();
	}

	public Field getField(int x, int y) {
		Point toSearch = new Point(x, y, Area.BOARD);
		return fields.getByPoint(toSearch);
	}

	public void addField(Field field) {
		fields.add(field);
	}

	public boolean anyUnitInHandArea() {
		for (Player player : game.getPlayers()) {
			for (Unit unit : player.getUnits()) {
				if (unit.getPoint().getArea() == Area.HAND) {
					return true;
				}
			}
		}
		return false;
	}

	public GameMap getGameMap() {
		return gameMap;
	}
}
