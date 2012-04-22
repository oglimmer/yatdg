package de.oglimmer.game.logic;

import de.oglimmer.game.logic.board.Area;

public class Point {

	private final int x;

	private final int y;

	private final Area area;

	public Point(int x, int y, Area area) {
		super();
		this.x = x;
		this.y = y;
		this.area = area;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Area getArea() {
		return area;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + x;
		result = prime * result + y;
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
		Point other = (Point) obj;
		if (area != other.area)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", area=" + area + "]";
	}

}
