package de.oglimmer.game;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java_websocket.WebSocket;

abstract public class Main {

	private static final Log log = LogFactory.getLog(Main.class);

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String... args) throws InterruptedException {

		startUp();

		synchronized (Main.class) {
			Main.class.wait();
		}
	}

	/**
	 * 
	 */
	public static void startUp() {
		WebSocket.DEBUG = false;
		Server server = Server.getInstance();
		server.start();
		log.info("Server started on port: " + server.getPort());
	}

	/**
	 * 
	 */
	public static void shutDown() {
		try {
			Server server = Server.getInstance();
			server.stop();
		} catch (IOException e) {
			log.error(e.toString(), e);
		}
	}

}
