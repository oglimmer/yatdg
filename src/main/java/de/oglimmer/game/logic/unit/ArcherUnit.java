package de.oglimmer.game.logic.unit;

import de.oglimmer.game.logic.Player;

public class ArcherUnit extends RangedUnit {

	public ArcherUnit(Player owningPlayer, int y) {
		super(owningPlayer, "Archer", y, 3, 15, 20,
				"Attacks enemy units whenever in 2 fields range,"
						+ " weak when fighting in melee range.");
	}

}
