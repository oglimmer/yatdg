package de.oglimmer.game.logic.turnprocessing;

import java.util.HashSet;
import java.util.Set;

import org.atmosphere.cpr.Broadcaster;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.game.com.ComConst;
import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.unit.Unit;
import de.oglimmer.game.util.ThreadHelper;

public class Battle {

	private static final Logger log = LoggerFactory.getLogger(Battle.class);

	public static enum Type {
		MELEE, RANGED
	}

	protected final Game game;
	private final Set<Unit> updatedUnits;
	private final Set<Unit> markedForDeath = new HashSet<Unit>();

	public Battle(Game game, Set<Unit> updatedUnits) {
		super();
		this.game = game;
		this.updatedUnits = updatedUnits;
	}

	/**
	 * Calculates a flight
	 * 
	 * @param attacker
	 * @param target
	 * @param type
	 * @return returns the damage done by the attacker to the target.
	 */
	protected int fight(Unit attacker, Unit target, Type type, Broadcaster bc) {

		int dmg = attacker.getDamage(type);

		log.debug("Attack: " + attacker + " hit :" + target + " for " + dmg);

		if (dmg > 0) {
			target.setLife(target.getLife() - dmg);

			sendDmgToClients(attacker, target, dmg, bc);

			if (target.getLife() <= 0) {
				markedForDeath.add(target);
			}

			updatedUnits.add(target);
		}

		return dmg;
	}

	protected void processDeath() {
		for (Unit unit : markedForDeath) {
			unit.kill();
		}
	}

	private void sendDmgToClients(Unit attacker, Unit target, int dmg, Broadcaster bc) {
		try {
			JSONObject o = new JSONObject();
			o.put(ComConst.RO_SHOWBATTLE, true);
			o.put(ComConst.RO_ATTACKER, attacker.getId());
			o.put(ComConst.RO_TARGET, target.getId());
			o.put(ComConst.RO_DAMAGE, dmg);
			for (Player player : game.getPlayers()) {
				String helpText = attacker.getOwningPlayer() == player ? ComConst.MSG_DMG_ATTACKE
						: ComConst.MSG_DMG_TARGET;
				String textColor = attacker.getOwningPlayer() == player ? "green"
						: "red";
				o.put(ComConst.RO_HELPTEXT, helpText);
				o.put(ComConst.RO_BATTLEINFOCOLOR, textColor);
				o.put("waitTime", 700);
//				Server.getInstance().send(player, o);
				bc.broadcast(o.toString(), player.getAtmosphereResource());
			}
			ThreadHelper.sleep(25);
		} catch (JSONException e) {
			log.error(e.toString(), e);
		}
	}

}
