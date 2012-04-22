package de.oglimmer.game;

import java.net.InetSocketAddress;
import java.nio.channels.NotYetConnectedException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketServer;
import org.java_websocket.handshake.ClientHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.oglimmer.game.com.ActionMessage;
import de.oglimmer.game.com.ComConst;
import de.oglimmer.game.logic.Game;
import de.oglimmer.game.logic.GameException;
import de.oglimmer.game.logic.GameManager;
import de.oglimmer.game.logic.Player;

public class Server extends WebSocketServer {

	private static final Log log = LogFactory.getLog(Server.class);

	private static final Server singleton = new Server(new InetSocketAddress(
			"0.0.0.0", 8081));

	public static Server getInstance() {
		return singleton;
	}

	private Map<Player, WebSocket> commMap = new HashMap<Player, WebSocket>();
	private boolean debug = false;
	private ExecutorService executorService = Executors.newFixedThreadPool(30);

	private Server(InetSocketAddress isa) {
		super(isa);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// no code here
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

		synchronized (commMap) {
			Player toRemove = null;
			for (Map.Entry<Player, WebSocket> en : commMap.entrySet()) {
				if (en.getValue() == conn) {
					toRemove = en.getKey();
				}
			}
			if (toRemove != null) {
				log.debug("removed player " + toRemove + " from webSocketMap");
				commMap.remove(toRemove);

				if (!debug) {
					Game game = toRemove.getGame();
					if (game.getPlayers().isBothPlayersRegistered()
							&& game.isRunning()) {
						JSONObject json = new JSONObject();
						try {
							json.put(ComConst.RO_ERROR,
									"Other player terminated connection.");
						} catch (JSONException e) {
							log.error(e.toString(), e);
						}
						Player otherPlayer = game.getPlayers().getOtherPlayer(
								toRemove);
						send(otherPlayer, json);
						commMap.remove(otherPlayer);
					}
					GameManager.getInstance().removeGame(game);
				}
			}

		}
	}

	@Override
	public void onMessage(final WebSocket conn, String message) {
		try {
			log.debug("got action:" + message);
			final JSONObject jsonInput = new JSONObject(
					new JSONTokener(message));
			final ActionMessage am = ActionMessage.getInstance(jsonInput, conn);
			if (am.getPlayer() == null) {
				Player player = am.getGame().getPlayers().registerPlayer();
				am.setPlayer(player);
			}
			if (!commMap.containsKey(am.getPlayer())) {
				synchronized (commMap) {
					log.debug("added player " + am.getPlayer()
							+ " to webSocketMap");
					commMap.put(am.getPlayer(), conn);
				}
			}
			executorService.submit(am);
		} catch (Exception e) {
			log.error(e.toString(), e);
			handleExcpetion(conn, e);
		}
	}

	public void handleExcpetion(WebSocket conn, Exception e) {
		try {
			JSONObject error = new JSONObject();
			error.put(ComConst.RO_ERROR, e.toString());
			conn.send(error.toString());
		} catch (NotYetConnectedException e1) {
			log.error(e1.toString(), e1);
		} catch (IllegalArgumentException e1) {
			log.error(e1.toString(), e1);
		} catch (InterruptedException e1) {
			log.error(e1.toString(), e1);
		} catch (JSONException e1) {
			log.error(e1.toString(), e1);
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		log.error("Exception caught", ex);
	}

	public void send(Player p, JSONObject message) {
		try {
			String str = message.toString();
			log.debug("response to " + p + ": " + str);
			WebSocket ws = commMap.get(p);
			if (ws == null) {
				throw new GameException("Player " + p
						+ " is not registered with a WebSocket");
			}
			ws.send(str);
		} catch (NotYetConnectedException e) {
			log.error(e.toString(), e);
		} catch (IllegalArgumentException e) {
			log.error(e.toString(), e);
		} catch (InterruptedException e) {
			log.error(e.toString(), e);
			// } catch (JSONException e) {
			// log.error(e.toString(), e);
		}
	}
}
