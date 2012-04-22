package de.oglimmer.game.logic.turnprocessing;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.unit.Unit;

public class Movement {

	private final Game game;
	private final Set<Unit> updatedUnits;

	public Movement(Game game, Set<Unit> updatedUnits) {
		this.game = game;
		this.updatedUnits = updatedUnits;
	}

	public boolean anyPlayerHasMovingUnit() {
		return !getAllPlayersMovingUnits().isEmpty();
	}

	public void doMoveTurn() throws JSONException {
		for (Unit unit : getAllPlayersMovingUnits()) {
			if (unit.moveForward()) {
				updatedUnits.add(unit);
			}
		}
	}

	private List<Unit> getAllPlayersMovingUnits() {
		List<Unit> retList = new ArrayList<Unit>();
		for (Player player : game.getPlayers()) {
			for (Unit unit : player.getUnits()) {
				if (unit.hasMoveForward()) {
					retList.add(unit);
				}
			}
		}
		return retList;
	}
}
