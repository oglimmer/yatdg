package de.oglimmer.game.logic.map;

import java.util.HashSet;
import java.util.Set;

import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.Players;
import de.oglimmer.game.logic.Point;
import de.oglimmer.game.logic.board.Area;
import de.oglimmer.game.logic.board.Board;
import de.oglimmer.game.logic.board.DeployField;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.board.FieldType;
import de.oglimmer.game.logic.board.FieldTypeRelation;

public class DefaultMap implements GameMap {

	private static final int width = 19;
	private static final int height = 9;

	private Game game;
	private DeployField deployA1;
	private DeployField deployA2;
	private DeployField deployB1;
	private DeployField deployB2;
	private Field castle;

	@Override
	public Board createBoard(Game game, Players players) {
		this.game = game;

		Board board = new Board(game, this);
		createDeployFields(board, players);
		createPlainField(board);

		// main street
		for (int i = 1; i < 9; i++) {
			board.getField(i, 4).setFieldTypeRelation(
					new FieldTypeRelation(FieldType.STREET, players.get0()));
		}
		castle = board.getField(9, 4);
		castle.setFieldTypeRelation(new FieldTypeRelation(FieldType.CASTLE));
		for (int i = 10; i < width - 1; i++) {
			board.getField(i, 4).setFieldTypeRelation(
					new FieldTypeRelation(FieldType.STREET, players.get1()));
		}

		// street B
		for (int i = 1; i < 16; i++) {
			board.getField(i, 7).setFieldTypeRelation(
					new FieldTypeRelation(FieldType.STREET, players.get0()));
		}
		board.getField(16, 6).setFieldTypeRelation(
				new FieldTypeRelation(FieldType.STREET, players.get0()));
		board.getField(16, 5).setFieldTypeRelation(
				new FieldTypeRelation(FieldType.STREET, players.get0()));

		// street A
		for (int i = 3; i < width - 1; i++) {
			board.getField(i, 1).setFieldTypeRelation(
					new FieldTypeRelation(FieldType.STREET, players.get1()));
		}
		board.getField(3, 2).setFieldTypeRelation(
				new FieldTypeRelation(FieldType.STREET, players.get1()));
		board.getField(2, 3).setFieldTypeRelation(
				new FieldTypeRelation(FieldType.STREET, players.get1()));

		return board;
	}

	private void createPlainField(Board board) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (board.getField(x, y) == null) {
					board.addField(new Field(board,
							new Point(x, y, Area.BOARD), new FieldTypeRelation(
									FieldType.GRAS)));
				}
			}
		}
	}

	private void createDeployFields(Board board, Players players) {
		deployA1 = new DeployField(board, new Point(0, 4, Area.BOARD),
				new FieldTypeRelation(FieldType.DEPLOY, players.get0()),
				DeployField.Type.MAINROAD);
		deployB1 = new DeployField(board, new Point(width - 1, 4, Area.BOARD),
				new FieldTypeRelation(FieldType.DEPLOY, players.get1()),
				DeployField.Type.MAINROAD);
		deployA2 = new DeployField(board, new Point(0, 7, Area.BOARD),
				new FieldTypeRelation(FieldType.DEPLOY, players.get0()),
				DeployField.Type.DEADENDROAD);
		deployB2 = new DeployField(board, new Point(width - 1, 1, Area.BOARD),
				new FieldTypeRelation(FieldType.DEPLOY, players.get1()),
				DeployField.Type.DEADENDROAD);
		board.addField(deployA1);
		board.addField(deployB1);
		board.addField(deployA2);
		board.addField(deployB2);
	}

	@Override
	public Set<DeployField> getDeployFields(Player player) {
		Set<DeployField> set = new HashSet<DeployField>();
		if (player.getNo() == 0) {
			if (game.getTurnNo() > 0) {
				set.add(deployA1);
			}
			set.add(deployA2);
		}
		if (player.getNo() == 1) {
			if (game.getTurnNo() > 0) {
				set.add(deployB1);
			}
			set.add(deployB2);
		}
		return set;
	}

	@Override
	public Field getCastle() {
		return castle;
	}
}
