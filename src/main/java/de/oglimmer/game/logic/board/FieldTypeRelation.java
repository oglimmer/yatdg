package de.oglimmer.game.logic.board;

import java.awt.Color;

import de.oglimmer.game.logic.Player;

public class FieldTypeRelation {

	private FieldType fieldType;

	private Player relatedTo;

	private Color color;

	public FieldTypeRelation(FieldType fieldType) {
		assert fieldType == FieldType.GRAS || fieldType == FieldType.CASTLE;
		this.fieldType = fieldType;
		initColor();
	}

	public FieldTypeRelation(FieldType fieldType, Player player) {
		assert fieldType == FieldType.STREET || fieldType == FieldType.DEPLOY;
		this.fieldType = fieldType;
		this.relatedTo = player;
		initColor();
	}

	private void initColor() {
		switch (fieldType) {
		case GRAS:
			color = Color.GREEN;
			break;
		case CASTLE:
			color = Color.BLACK;
			break;
		case STREET:
		case DEPLOY:
			if (relatedTo.getNo() == 0) {
				color = Color.RED;
			} else {
				color = Color.ORANGE;
			}
			break;
		}
	}

	public Color getColor() {
		return color;
	}

	public FieldType getFieldType() {
		return fieldType;
	}

	public boolean isForPlayer(Player owningPlayer) {
		return relatedTo == null || owningPlayer == relatedTo;
	}

}
