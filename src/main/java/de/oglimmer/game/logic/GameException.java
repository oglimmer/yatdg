package de.oglimmer.game.logic;

public class GameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GameException() {
		super();
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameException(String message) {
		super(message);
	}

	public GameException(Throwable cause) {
		super(cause);
	}

}
