package de.oglimmer.game.logic.board;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.Point;
import de.oglimmer.game.logic.unit.RangedUnit;
import de.oglimmer.game.logic.unit.Unit;

public class DeployField extends Field {

	public static enum Type {
		MAINROAD, DEADENDROAD
	}

	private final Type type;

	public DeployField(Board board, Point point,
			FieldTypeRelation fieldTypeRelation, Type type) {
		super(board, point, fieldTypeRelation);
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public boolean availableForUnit(Unit unit) {
		return type == Type.MAINROAD || unit instanceof RangedUnit;
	}

	@Override
	public boolean isSelectable(Player player) {
		boolean retBool = !isUnitHere()
				&& getFieldTypeRelation().getFieldType() == FieldType.DEPLOY;
		return retBool;
	}

}
