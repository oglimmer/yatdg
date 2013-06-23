package de.oglimmer.game.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.AtmosphereResource;

import de.oglimmer.game.logic.unit.Unit;

public class Players implements Iterable<Player> {

	private static final Log log = LogFactory.getLog(Players.class);

	private final Player[] players;

	private final Collection<Player> playersCol;

	public Players(Game game) {
		players = new Player[2];
		players[0] = new Player(game, 0);
		players[1] = new Player(game, 1);
		playersCol = Collections.unmodifiableCollection(Arrays.asList(players));
	}

	public Player get0() {
		return players[0];
	}

	public Player get1() {
		return players[1];
	}

	public Player get(int playerNo) {
		return players[playerNo];
	}

	@Override
	public Iterator<Player> iterator() {
		return playersCol.iterator();
	}

	public Player getPlayer(String playerId) {
		for (Player player : players) {
			if (player.getId().equals(playerId)) {
				return player;
			}
		}
		throw new GameException("no player with " + playerId);
	}

	public Player getOtherPlayer(Player onePlayer) {
		Player retPlayer = null;
		for (Player player : players) {
			if (player != onePlayer) {
				retPlayer = player;
			}
		}
		assert retPlayer != null;
		return retPlayer;
	}

	public Player registerPlayer(AtmosphereResource resource) {
		for (Player player : players) {
			if (!player.isRegistered()) {
				log.debug("setRegistered " + player.getId());
				player.setRegistered(true);
				player.setAtmosphereResource(resource);
				return player;
			}
		}
		throw new GameException("No more free players");
	}

	public boolean isBothPlayersRegistered() {
		int sum = 0;
		for (Player player : players) {
			if (player.isRegistered()) {
				sum++;
			}
		}
		return sum == players.length;
	}

	public UIElement getUiElement(String uiElementId) {
		UIElement retObj = null;
		for (Player player : players) {
			for (Unit unit : player.getUnits()) {
				if (unit.getId().equals(uiElementId))
					retObj = unit;
			}
		}
		return retObj;
	}
}
