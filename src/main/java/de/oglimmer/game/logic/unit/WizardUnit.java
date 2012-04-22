package de.oglimmer.game.logic.unit;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.board.Fields;

public class WizardUnit extends RangedUnit {

	public WizardUnit(Player owningPlayer, int y) {
		super(owningPlayer, "Wizard", y, 0, 6, 15,
				"The Wizard can attack enemy units whereever ther are. "
						+ "Cannot make any damage in melee fight");
	}

	@Override
	public Units getPossibleTargets() {
		Field baseField = getCurrentPosAsField();
		Fields allFields = getBoard().getFieldsClone();
		allFields.remove(baseField);
		return allFields.getEnemyUnits(getOwningPlayer());
	}
}
