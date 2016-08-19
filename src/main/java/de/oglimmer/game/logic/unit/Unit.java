package de.oglimmer.game.logic.unit;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.Point;
import de.oglimmer.game.logic.UIElement;
import de.oglimmer.game.logic.board.Area;
import de.oglimmer.game.logic.board.Board;
import de.oglimmer.game.logic.board.DeployField;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.turnprocessing.Battle.Type;

public class Unit extends UIElement {

	private static final Logger log = LoggerFactory.getLogger(Unit.class);

	private final String name;

	private final int attack;

	private final int maxLife;

	private final Player owningPlayer;

	private final Set<Point> pointHistory = new HashSet<Point>();

	private final String text;

	private int life;

	private Point nextPoint;

	public Unit(Player owningPlayer, String name, int y, int attack,
			int maxLife, String text) {
		super(new Point(0, y, Area.HAND));
		this.name = name;
		this.attack = attack;
		this.maxLife = maxLife;
		this.life = maxLife;
		this.owningPlayer = owningPlayer;
		this.text = text;
	}

	public Player getOwningPlayer() {
		return owningPlayer;
	}

	public String getName() {
		return name;
	}

	public int getAttack() {
		return attack;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = Math.max(life, 0);
	}

	public Point getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(Point point) {
		this.nextPoint = point;
	}

	public Point getPoint(Player forPlayer) {
		if (nextPoint != null && owningPlayer == forPlayer) {
			return nextPoint;
		}
		return getPoint();
	}

	public int getMaxLife() {
		return maxLife;
	}

	@Override
	public boolean isSelectable(Player forPlayer) {
		boolean inHand = getPoint(forPlayer).getArea() == Area.HAND;
		boolean isDeployable = hasAvailableDeployPoint();
		boolean unitIsOwnedByPlayer = forPlayer == owningPlayer;

		return inHand && isDeployable && unitIsOwnedByPlayer;
	}

	public boolean moveForward() {
		pointHistory.add(getPoint());
		Field field = getToMoveForwardAdjacentRoadField();
		if (field != null) {
			setPoint(field.getPoint());
		}
		return field != null;
	}

	public boolean hasMoveForward() {
		return getToMoveForwardAdjacentRoadField() != null;
	}

	public Field getCurrentPosAsField() {
		assert getPoint().getArea() == Area.BOARD;
		return getBoard().getField(getPoint().getX(), getPoint().getY());
	}

	public void moveBack() {
		Set<Point> pointHistoryLocal = new HashSet<Point>();
		do {
			pointHistoryLocal.add(getPoint());
			moveOneFieldBack(pointHistoryLocal);
		} while (moreThanOneOwnUnitOnCurrentField());
	}

	public void kill() {
		setPoint(new Point(0, 0, Area.GRAVEYARD));
		owningPlayer.removeUnit(this);
		log.debug("killed " + this);
	}

	public String getText() {
		return text;
	}

	public int getDamage(Type type) {
		return (int) (attack * Math.random());
	}

	public boolean isAliveOnTable() {
		return getPoint().getArea() == Area.BOARD;
	}

	private boolean hasAvailableDeployPoint() {
		boolean hasFreeDeployable = false;
		for (DeployField field : getBoard().getGameMap().getDeployFields(
				owningPlayer)) {
			if (!field.isUnitHere() && field.availableForUnit(this)) {
				hasFreeDeployable = true;
			}
		}
		return hasFreeDeployable;
	}

	protected Board getBoard() {
		return owningPlayer.getGame().getBoard();
	}

	private boolean moreThanOneOwnUnitOnCurrentField() {
		for (Unit unit : owningPlayer.getUnits()) {
			if (unit != this && unit.getPoint().equals(getPoint())) {
				return true;
			}
		}
		return false;
	}

	private Field getToMoveForwardAdjacentRoadField() {
		Field retField = null;
		if (getPoint().getArea() == Area.BOARD) {
			for (Field field : getCurrentPosAsField().getAdjacentFields()) {
				if (canMoveForwardTo(field)) {
					retField = field;
					break;
				}
			}
		}
		return retField;
	}

	private boolean canMoveForwardTo(Field field) {
		return field.isRoadFor(owningPlayer)
				&& !field.isUnitHereForPlayer(owningPlayer)
				&& !pointHistory.contains(field.getPoint());
	}

	private void moveOneFieldBack(Set<Point> pointHistoryLocal) {
		for (Field field : getCurrentPosAsField().getAdjacentFields()) {
			if (field.isRoadFor(owningPlayer)
					&& !pointHistoryLocal.contains(field.getPoint())) {
				pointHistory.remove(getPoint());
				setPoint(field.getPoint());
			}
		}
	}

	@Override
	public String toString() {
		return "Unit [name=" + name + ", life=" + life + ", getPoint()="
				+ getPoint() + "]";
	}

}
