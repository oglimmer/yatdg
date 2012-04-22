package de.oglimmer.game.util;

import java.util.Random;

/**
 * Creates a random string. It could be specified which character codes should
 * be used.
 * 
 * @author Oli
 */
final public class RandomString {

	private RandomString() {
		// no code here
	}

	private static final Random RAN = new Random(System.currentTimeMillis());

	/**
	 * Creates a size byte long unicode string. All codes are > 32.
	 * 
	 * @param size
	 * @return
	 */
	public static String getRandomStringUnicode(final int size) {
		final StringBuilder buff = new StringBuilder(size);

		for (int i = 0; i < size; i++) {
			buff.append((char) (RAN.nextInt(65503) + 32));
		}
		return buff.toString();
	}

	/**
	 * Creates a size byte long unicode string. All codes are between 32 and 255
	 * 
	 * @param size
	 * @return
	 */
	public static String getRandomString8Bit(final int size) {
		final StringBuilder buff = new StringBuilder(size);

		for (int i = 0; i < size; i++) {
			buff.append((char) (RAN.nextInt(223) + 32));
		}
		return buff.toString();
	}

	/**
	 * Creates a size byte long unicode string. All codes are between a..z and
	 * A..Z
	 * 
	 * @param size
	 * @return
	 */
	public static String getRandomStringASCII(final int size) {
		final StringBuilder buff = new StringBuilder(size);

		for (int i = 0; i < size; i++) {
			char nextChar;
			do {
				nextChar = (char) (RAN.nextInt(75) + 48);
			} while ((nextChar >= 58 && nextChar <= 64)
					|| (nextChar >= 91 && nextChar <= 96));
			buff.append(nextChar);
		}
		return buff.toString();
	}

	/**
	 * Creates a size byte long unicode string. All codes are from the set
	 * "stringSet"
	 * 
	 * @param size
	 * @param stringSet
	 * @return
	 */
	public static String getRandomString(final int size, final String stringSet) {

		final StringBuilder buff = new StringBuilder(size);

		for (int i = 0; i < size; i++) {
			final char nextChar = stringSet.charAt(RAN.nextInt(stringSet
					.length()));
			buff.append(nextChar);
		}
		return buff.toString();
	}

	/**
	 * Creates a size byte long unicode string. All codes are 0..9 and a..f.
	 * 
	 * @param size
	 * @return
	 */
	public static String getRandomStringHex(final int size) {
		return getRandomString(size, "0123456789ABCDEF");
	}

}