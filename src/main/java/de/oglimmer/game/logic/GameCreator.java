package de.oglimmer.game.logic;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.oglimmer.game.logic.unit.ArcherUnit;
import de.oglimmer.game.logic.unit.RamUnit;
import de.oglimmer.game.logic.unit.SwordmanUnit;
import de.oglimmer.game.logic.unit.Unit;
import de.oglimmer.game.logic.unit.WizardUnit;

public class GameCreator {

	private static final Log log = LogFactory.getLog(GameCreator.class);

	private final List<Class<? extends Unit>> clazzes = new ArrayList<Class<? extends Unit>>();

	public GameCreator() {
		clazzes.add(SwordmanUnit.class);
		clazzes.add(SwordmanUnit.class);
		clazzes.add(ArcherUnit.class);
		clazzes.add(WizardUnit.class);
		clazzes.add(RamUnit.class);
	}

	public void createDefaultUnits(Game game, Players players) {

		try {

			for (Player player : players) {
				int i = 0;
				for (Class<? extends Unit> clazz : clazzes) {
					Unit unit = clazz.getConstructor(Player.class, int.class)
							.newInstance(player, i++);
					player.getUnits().add(unit);
				}
			}
		} catch (IllegalArgumentException e) {
			log.error(e.toString(), e);
		} catch (SecurityException e) {
			log.error(e.toString(), e);
		} catch (InstantiationException e) {
			log.error(e.toString(), e);
		} catch (IllegalAccessException e) {
			log.error(e.toString(), e);
		} catch (InvocationTargetException e) {
			log.error(e.toString(), e);
		} catch (NoSuchMethodException e) {
			log.error(e.toString(), e);
		}

	}

	public String getMapId() {
		return "default";
	}

}
