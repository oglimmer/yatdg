package de.oglimmer.game.logic;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.game.logic.action.EndTurnAction;
import de.oglimmer.game.logic.board.DeployFields;
import de.oglimmer.game.logic.unit.Unit;
import de.oglimmer.game.logic.unit.Units;
import de.oglimmer.game.util.RandomString;

public class Player {

	private static final Logger log = LoggerFactory.getLogger(Player.class);

	private final String id;

	private final int no;

	private final Game game;

	private final Units units = new Units(this);

	private final DeployFields deployFields = new DeployFields(this);

	private boolean registered;

	private boolean done;

	private Unit currentlySelected;

	public Player(Game game, int no) {
		this.id = "player" + RandomString.getRandomStringHex(4);
		this.no = no;
		this.game = game;
		this.registered = false;
		log.debug("Created Player " + id);
	}

	public String getId() {
		return id;
	}

	public int getNo() {
		return no;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public Units getUnits() {
		return units;
	}

	public Unit getUnit(String unitId) {
		Unit retUnit = null;
		for (Unit unit : units) {
			if (unit.getId().equals(unitId)) {
				retUnit = unit;
			}
		}
		assert retUnit != null;
		return retUnit;
	}

	public Unit getCurrentlySelected() {
		return currentlySelected;
	}

	public void setCurrentlySelected(Unit currentlySelected) {
		this.currentlySelected = currentlySelected;
	}

	public DeployFields getDeployFields() {
		return deployFields;
	}

	public Game getGame() {
		return game;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public void removeUnit(Unit unit) {
		units.remove(unit);
	}

	public String getUnitColor() {
		return game.getPlayers().get0() == this ? "yellow" : "brown";
	}

	public void checkForEndTurn(Broadcaster bc) throws JSONException {
		if (!getUnits().isSelectable()) {
			new EndTurnAction().execute(getGame(), this, null, null, bc);
		}
	}

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
		Player other = (Player) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Player [no=" + no + "]";
	}

	private AtmosphereResource ar;

	public void setAtmosphereResource(AtmosphereResource ar) {
		if (ar == null) {
			throw new RuntimeException("errror");
		}
		this.ar = ar;
	}

	public AtmosphereResource getAtmosphereResource() {
		return ar;
	}

}
