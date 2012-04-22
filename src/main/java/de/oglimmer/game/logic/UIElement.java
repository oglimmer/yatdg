package de.oglimmer.game.logic;

import java.util.HashMap;
import java.util.Map;

import de.oglimmer.game.util.RandomString;

abstract public class UIElement {

	private final String id;
	private final Map<Player, PlayerProperties> playerProps = new HashMap<Player, PlayerProperties>();
	private Point point;

	public UIElement(Point point) {
		this.id = RandomString.getRandomStringHex(8);
		this.point = point;
	}

	public String getId() {
		return id;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public PlayerProperties getPlayerProps(Player p) {
		PlayerProperties pp = playerProps.get(p);
		if (pp == null) {
			pp = new PlayerProperties();
			playerProps.put(p, pp);
		}
		return pp;
	}

	abstract public boolean isSelectable(Player player);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UIElement other = (UIElement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UIElement [point=" + point + "]";
	}
}
