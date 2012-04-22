package de.oglimmer.game.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadHelper {

	private static final Log log = LogFactory.getLog(ThreadHelper.class);

	private ThreadHelper() {

	}

	public static void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			log.error(e.toString(), e);
		}
	}
}
