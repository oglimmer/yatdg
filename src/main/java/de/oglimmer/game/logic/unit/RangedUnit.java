package de.oglimmer.game.logic.unit;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.board.Fields;
import de.oglimmer.game.logic.turnprocessing.Battle.Type;

public class RangedUnit extends Unit {

	private final int rangedAttack;

	public RangedUnit(Player owningPlayer, String name, int y, int meleeAttack,
			int rangedAttack, int maxLife, String text) {
		super(owningPlayer, name, y, meleeAttack, maxLife, text);
		this.rangedAttack = rangedAttack;
	}

	public int getRangedAttack() {
		return rangedAttack;
	}

	@Override
	public int getDamage(Type type) {
		if (type == Type.RANGED) {
			return (int) (rangedAttack * Math.random());
		} else {
			return super.getDamage(type);
		}
	}

	public Units getPossibleTargets() {
		Field baseField = getCurrentPosAsField();
		Fields nearbyFields = baseField.getAdjacentFieldsDouble();
		return nearbyFields.getEnemyUnits(getOwningPlayer());
	}
}
