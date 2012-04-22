package de.oglimmer.game.logic.board;

public enum FieldType {

	GRAS("Gras"), STREET("Road"), CASTLE("Castle"), DEPLOY("Road");

	private String name;

	private FieldType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
