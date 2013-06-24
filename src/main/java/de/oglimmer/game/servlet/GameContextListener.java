package de.oglimmer.game.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import de.oglimmer.game.logic.GameManager;

// import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class GameContextListener
 * 
 */
// @WebListener
public class GameContextListener implements ServletContextListener {

	private static final Log log = LogFactory.getLog(GameContextListener.class);

	/**
	 * Default constructor.
	 */
	public GameContextListener() {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Server server = new Server(9999);

			WebAppContext webapp = new WebAppContext();
			webapp.setContextPath("/");
			webapp.setWar(System.getProperty("yatdg_path"));
			server.setHandler(webapp);

			server.start();
			// server.join();
		} catch (Exception e) {
			log.error("Failed to start Jetty", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		GameManager.getInstance().clear();
	}

}
