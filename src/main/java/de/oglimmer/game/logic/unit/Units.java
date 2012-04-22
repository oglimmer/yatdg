package de.oglimmer.game.logic.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import de.oglimmer.game.logic.Player;

public class Units implements Iterable<Unit> {

	private final Collection<Unit> units = new ArrayList<Unit>();

	private final Player owningPlayer;

	public Units(Player owningPlayer) {
		super();
		this.owningPlayer = owningPlayer;
	}

	public Units(Player player, Collection<Unit> units) {
		this(player);
		this.units.addAll(units);
	}

	public void add(Unit unit) {
		units.add(unit);
	}

	public void remove(Unit unit) {
		units.remove(unit);
	}

	@Override
	public Iterator<Unit> iterator() {
		return units.iterator();
	}

	public void enablePotential() {
		for (Unit unit : units) {
			unit.getPlayerProps(owningPlayer).setSelectable(
					unit.isSelectable(owningPlayer));
		}
	}

	public void deactivateExceptToDeploy() {
		for (Unit unit : units) {
			unit.getPlayerProps(owningPlayer).setSelectable(
					unit == owningPlayer.getCurrentlySelected());
		}
	}

	public void deactivateSelectables() {
		for (Unit u : units) {
			u.getPlayerProps(owningPlayer).setSelectable(false);
		}
	}

	public Unit getRandomEnemy() {
		int random = (int) (Math.random() * units.size());
		Unit target = null;
		int i = 0;
		Iterator<Unit> it = units.iterator();
		do {
			target = it.next();
		} while (i++ < random);
		return target;
	}

	public boolean isEmpty() {
		return units.isEmpty();
	}

	public long size() {
		return units.size();
	}

	public boolean isSelectable() {
		boolean selectables = false;
		for (Unit unit : units) {
			selectables |= unit.getPlayerProps(owningPlayer).isSelectable();
		}
		return selectables;
	}
}
