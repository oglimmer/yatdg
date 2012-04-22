package de.oglimmer.game.logic.board;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.Point;
import de.oglimmer.game.logic.unit.Unit;
import de.oglimmer.game.logic.unit.Units;

public class Fields implements Iterable<Field>, Cloneable {

	private Map<Point, Field> fields = new HashMap<Point, Field>();

	public Fields() {
	}

	public Fields(Collection<Field> nearbyFields) {
		for (Field f : nearbyFields) {
			fields.put(f.getPoint(), f);
		}
	}

	public Units getEnemyUnits(Player otherPlayer) {
		Player enemy = otherPlayer.getGame().getPlayers()
				.getOtherPlayer(otherPlayer);
		Set<Unit> retSet = new HashSet<Unit>();
		for (Field field : fields.values()) {
			Unit unit = field.getUnit(enemy);
			if (unit != null) {
				retSet.add(unit);
			}
		}
		return new Units(enemy, retSet);
	}

	public Field getByPoint(Point point) {
		Field retField = fields.get(point);
		return retField;
	}

	public void add(Field field) {
		fields.put(field.getPoint(), field);
	}

	@Override
	public Iterator<Field> iterator() {
		return fields.values().iterator();
	}

	public Collection<? extends Field> getCollection() {
		return fields.values();
	}

	@Override
	protected Object clone() {
		try {
			Fields clone = (Fields) super.clone();
			clone.fields = new HashMap<Point, Field>(this.fields);
			return clone;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void remove(Field baseField) {
		for (Iterator<Map.Entry<Point, Field>> it = fields.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<Point, Field> en = it.next();
			if (baseField == en.getValue()) {
				it.remove();
			}
		}
	}
}
