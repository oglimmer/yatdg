package de.oglimmer.game.logic.unit;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.board.Fields;

public class RamUnit extends RangedUnit {

	public RamUnit(Player owningPlayer, int y) {
		super(owningPlayer, "Ram", y, 0, 25, 5,
				"Inflicts great damage to enemy units whenever in 1 fields range,"
						+ " useless when fighting in melee range.");
	}

	public Units getPossibleTargets() {
		Field baseField = getCurrentPosAsField();
		Fields nearbyFields = baseField.getAdjacentFields();
		return nearbyFields.getEnemyUnits(getOwningPlayer());
	}
}
