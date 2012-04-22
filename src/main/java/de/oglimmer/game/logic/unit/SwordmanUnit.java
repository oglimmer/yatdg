package de.oglimmer.game.logic.unit;

import de.oglimmer.game.logic.Player;

public class SwordmanUnit extends Unit {

	public SwordmanUnit(Player owningPlayer, int y) {
		super(owningPlayer, "Swordman", y, 10, 40,
				"Inflicts severe damage to enemy units at the same location."
						+ " Must be deployed to the main road.");
	}

}
