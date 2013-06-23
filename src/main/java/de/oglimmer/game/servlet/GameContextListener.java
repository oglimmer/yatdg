package de.oglimmer.game.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.oglimmer.game.logic.GameManager;
// import javax.servlet.annotation.WebListener;

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
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		GameManager.getInstance().clear();
	}

}
