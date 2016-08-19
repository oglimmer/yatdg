package de.oglimmer.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadHelper {

	private static final Logger log = LoggerFactory.getLogger(ThreadHelper.class);

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
