package de.oglimmer.game.logic;

public class PlayerProperties {

	private boolean selectable;

	public PlayerProperties() {
		super();
		this.selectable = false;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	@Override
	public String toString() {
		return "PlayerProperties [selectable=" + selectable + "]";
	}

}
