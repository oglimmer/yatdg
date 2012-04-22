package de.oglimmer.game.logic.board;

import java.util.Set;

import de.oglimmer.game.logic.Player;

public class DeployFields {

	private final Player player;

	public DeployFields(Player player) {
		super();
		this.player = player;
	}

	public void deactivate() {
		for (DeployField f : getDeployFields()) {
			f.getPlayerProps(player).setSelectable(false);
		}
	}

	public void enablePotentialDeployFields() {
		for (DeployField field : getDeployFields()) {
			boolean sel = field.isSelectable(player)
					&& field.availableForUnit(player.getCurrentlySelected());
			field.getPlayerProps(player).setSelectable(sel);
		}
	}

	private Set<DeployField> getDeployFields() {
		return player.getGame().getBoard().getGameMap().getDeployFields(player);
	}

}
