package de.oglimmer.game;

import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceFactory;
import org.atmosphere.handler.AbstractReflectorAtmosphereHandler;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.oglimmer.game.com.ActionMessage;
import de.oglimmer.game.logic.Player;

@AtmosphereHandlerService(path = "/srvcom")
public class SrvComAtmosphereHandler extends AbstractReflectorAtmosphereHandler
		implements AtmosphereHandler {

	private ThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(
			10);
	private static final Logger log = LoggerFactory.getLogger(SrvComAtmosphereHandler.class);

	@Override
	public void onRequest(AtmosphereResource r) throws IOException {
		AtmosphereRequest req = r.getRequest();
		if (req.getMethod().equalsIgnoreCase("GET")) {
			r.suspend();
		} else if (req.getMethod().equalsIgnoreCase("POST")) {
			handlePayload(r, req);
		}
	}

	@Override
	public void onStateChange(AtmosphereResourceEvent event) throws IOException {
		if (event.isSuspended()) {
			super.onStateChange(event);
		} else if (!event.isResuming()) {
			event.broadcaster().broadcast(
					"{\"error\":\"Opponent disconnected!\"}");
		}
	}

	private void handlePayload(AtmosphereResource r, AtmosphereRequest req)
			throws IOException {
		try {
			String message = req.getReader().readLine().trim();
			log.error("got action:" + message);
			JSONObject jsonInput = new JSONObject(new JSONTokener(message));
			ActionMessage am = ActionMessage.getInstance(jsonInput, r);
			if (am.getPlayer() == null) {
				registerNewPlayer(req, am);
			}

			executorService.submit(am);
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
	}

	private void registerNewPlayer(AtmosphereRequest req, final ActionMessage am) {
		AtmosphereResource resource = getOriginResource(req);
		Player player = am.getGame().getPlayers().registerPlayer(resource);
		am.setPlayer(player);
	}

	private AtmosphereResource getOriginResource(AtmosphereRequest req) {
		String uuid = (String) req
				.getAttribute(ApplicationConfig.SUSPENDED_ATMOSPHERE_RESOURCE_UUID);
		return AtmosphereResourceFactory.getDefault().find(uuid);
	}

}