package de.oglimmer.game.logic.map;

import java.util.Set;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.Players;
import de.oglimmer.game.logic.board.Board;
import de.oglimmer.game.logic.board.DeployField;
import de.oglimmer.game.logic.board.Field;

public interface GameMap {

	Board createBoard(Game game, Players players);

	Set<DeployField> getDeployFields(Player player);

	Field getCastle();
}
