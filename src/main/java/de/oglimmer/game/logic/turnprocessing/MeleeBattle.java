package de.oglimmer.game.logic.turnprocessing;

import java.util.HashSet;
import java.util.Set;

import org.atmosphere.cpr.Broadcaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.unit.Unit;

public class MeleeBattle extends Battle {

	private static final Logger log = LoggerFactory.getLogger(MeleeBattle.class);

	public MeleeBattle(Game game, Set<Unit> updatedUnits) {
		super(game, updatedUnits);
	}

	public void doMeleeBattles(Broadcaster bc) {
		for (Field field : getAllBattlegrounds()) {
			Unit u1 = field.getUnit(0);
			Unit u2 = field.getUnit(1);

			log.debug("Battleground " + field + ", Unit_1:" + u1 + "/Unit_2:"
					+ u2);

			fight(u1, u2, Type.MELEE, bc);
			fight(u2, u1, Type.MELEE, bc);

		}
		processDeath();
	}

	public Set<Field> getAllBattlegrounds() {
		Set<Field> retSet = new HashSet<Field>();
		for (Field field : game.getBoard().getFields()) {
			if (field.getUnit(0) != null && field.getUnit(1) != null) {
				retSet.add(field);
			}
		}
		return retSet;
	}
}
