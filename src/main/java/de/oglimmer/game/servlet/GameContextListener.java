package de.oglimmer.game.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
// import javax.servlet.annotation.WebListener;

import de.oglimmer.game.Main;
import de.oglimmer.game.logic.GameManager;

/**
 * Application Lifecycle Listener implementation class GameContextListener
 * 
 */
// @WebListener
public class GameContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public GameContextListener() {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Main.startUp();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Main.shutDown();
		GameManager.getInstance().clear();
	}

}
