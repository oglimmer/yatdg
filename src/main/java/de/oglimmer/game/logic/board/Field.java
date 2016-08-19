package de.oglimmer.game.logic.board;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.Point;
import de.oglimmer.game.logic.UIElement;
import de.oglimmer.game.logic.unit.Unit;

public class Field extends UIElement {

	private static final Logger log = LoggerFactory.getLogger(Field.class);

	private FieldTypeRelation fieldTypeRelation;

	private Board board;

	public Field(Board board, Point point, FieldTypeRelation fieldTypeRelation) {
		super(point);
		this.board = board;
		this.fieldTypeRelation = fieldTypeRelation;
		log.debug("Created Field " + point + "/" + fieldTypeRelation);
	}

	public FieldTypeRelation getFieldTypeRelation() {
		return fieldTypeRelation;
	}

	public void setFieldTypeRelation(FieldTypeRelation fieldTypeRelation) {
		this.fieldTypeRelation = fieldTypeRelation;
	}

	@Override
	public boolean isSelectable(Player player) {
		return false;
	}

	public boolean isUnitHere() {
		return getUnit() != null;
	}

	public boolean isUnitHereForPlayer(Player player) {
		return getUnit(player) != null;
	}

	public Unit getUnit(int playerNo) {
		Player player = board.getGame().getPlayers().get(playerNo);
		return getUnit(player);
	}

	public Unit getUnit() {
		Unit retUnit = null;
		for (Player player : board.getGame().getPlayers()) {
			Unit unit = getUnit(player);
			if (unit != null) {
				assert retUnit == null;
				retUnit = unit;
			}
		}
		return retUnit;
	}

	public Unit getUnit(Player player) {
		Unit retUnit = null;
		for (Unit unit : player.getUnits()) {
			if (unit.getPoint(player).equals(getPoint())) {
				assert retUnit == null;
				retUnit = unit;
			}
		}
		return retUnit;
	}

	public Fields getAdjacentFields() {
		Set<Field> retSet = new HashSet<Field>();
		for (int[] cor : getAdjacentFieldCoordinates()) {
			Field field = board.getField(cor[0], cor[1]);
			if (field != null) {
				retSet.add(field);
			}
		}
		return new Fields(retSet);
	}

	public Fields getAdjacentFieldsDouble() {
		Set<Field> nearbyFields = new HashSet<Field>();

		// range-1
		nearbyFields.addAll(getAdjacentFields().getCollection());

		// range-2
		Set<Field> allOuterFields = new HashSet<Field>();
		for (Field f : nearbyFields) {
			allOuterFields.addAll(f.getAdjacentFields().getCollection());
		}
		nearbyFields.addAll(allOuterFields);

		// the base field should not be returned
		nearbyFields.remove(this);
		return new Fields(nearbyFields);
	}

	private int[][] getAdjacentFieldCoordinates() {
		int x = getPoint().getX();
		int y = getPoint().getY();

		int[][] cords = new int[6][2];
		cords[0] = new int[] { (y % 2 == 0 ? x - 1 : x), y - 1 };
		cords[1] = new int[] { (y % 2 == 0 ? x : x + 1), y - 1 };
		cords[2] = new int[] { x - 1, y };
		cords[3] = new int[] { x + 1, y };
		cords[4] = new int[] { (y % 2 == 0 ? x - 1 : x), y + 1 };
		cords[5] = new int[] { (y % 2 == 0 ? x : x + 1), y + 1 };
		return cords;
	}

	public boolean isRoadFor(Player owningPlayer) {
		return fieldTypeRelation.isForPlayer(owningPlayer)
				&& fieldTypeRelation.getFieldType() == FieldType.STREET
				|| getFieldTypeRelation().getFieldType() == FieldType.CASTLE;
	}

	@Override
	public String toString() {
		return "Field [getPoint()=" + getPoint() + "]";
	}

}
