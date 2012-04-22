package de.oglimmer.game.logic.turnprocessing;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.board.Area;
import de.oglimmer.game.logic.unit.RangedUnit;
import de.oglimmer.game.logic.unit.Unit;
import de.oglimmer.game.logic.unit.Units;

public class RangedBattle extends Battle {

	private static final Log log = LogFactory.getLog(RangedBattle.class);

	public RangedBattle(Game game, Set<Unit> updatedUnits) {
		super(game, updatedUnits);
	}

	public void doRangedBattles() {
		for (RangedUnit rangedUnit : getAllRangedUnitFields()) {
			battle(rangedUnit);
		}
		processDeath();
	}

	private void battle(RangedUnit rangedUnit) {
		Units possibleTargets = rangedUnit.getPossibleTargets();
		log.debug("Ranged " + rangedUnit + " has " + possibleTargets.size()
				+ " targets: " + possibleTargets);
		if (!possibleTargets.isEmpty()) {
			Unit target = possibleTargets.getRandomEnemy();
			fight(rangedUnit, target, Type.RANGED);
		}
	}

	private Set<RangedUnit> getAllRangedUnitFields() {
		Set<RangedUnit> retUnits = new HashSet<RangedUnit>();
		for (Player player : game.getPlayers()) {
			retUnits.addAll(getAllRangedUnitFields(player));
		}
		return retUnits;
	}

	private Set<RangedUnit> getAllRangedUnitFields(Player player) {
		Set<RangedUnit> retUnits = new HashSet<RangedUnit>();
		for (Unit unit : player.getUnits()) {
			if (unit instanceof RangedUnit
					&& unit.getPoint().getArea() == Area.BOARD) {
				retUnits.add((RangedUnit) unit);
			}
		}
		return retUnits;
	}
}
