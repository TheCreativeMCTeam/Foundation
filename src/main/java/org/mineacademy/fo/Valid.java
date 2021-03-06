package org.mineacademy.fo;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.model.RangedValue;
import org.mineacademy.fo.settings.SimpleLocalization;

import lombok.experimental.UtilityClass;

/**
 * Utility class for checking conditions and throwing our safe exception that is
 * logged into file.
 */
@UtilityClass
public final class Valid {

	/**
	 * Matching valid integers
	 */
	private final Pattern PATTERN_INTEGER = Pattern.compile("-?\\d+");

	/**
	 * Matching valid whole numbers
	 */
	private final Pattern PATTERN_DECIMAL = Pattern.compile("-?\\d+.\\d+");

	// ------------------------------------------------------------------------------------------------------------
	// Checking for validity and throwing errors if false or null
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Throws an error if the given object is null
	 *
	 * @param toCheck
	 */
	public void checkNotNull(Object toCheck) {
		if (toCheck == null)
			throw new FoException();
	}

	/**
	 * Throws an error with a custom message if the given object is null
	 *
	 * @param toCheck
	 * @param falseMessage
	 */
	public void checkNotNull(Object toCheck, String falseMessage) {
		if (toCheck == null)
			throw new FoException(falseMessage);
	}

	/**
	 * Throws an error if the given expression is false
	 *
	 * @param expression
	 */
	public void checkBoolean(boolean expression) {
		if (!expression)
			throw new FoException();
	}

	/**
	 * Throws an error with a custom message if the given expression is false
	 *
	 * @param expression
	 * @param falseMessage
	 */
	public void checkBoolean(boolean expression, String falseMessage) {
		if (!expression)
			throw new FoException(falseMessage);
	}

	/**
	 * Throws an error with a custom message if the given toCheck string is not a number!
	 *
	 * @param toCheck
	 * @param falseMessage
	 */
	public void checkInteger(String toCheck, String falseMessage) {
		if (!isInteger(toCheck))
			throw new FoException(falseMessage);
	}

	/**
	 * Throws an error with a custom message if the given collection is null or empty
	 *
	 * @param collection
	 * @param message
	 */
	public void checkNotEmpty(Collection<?> collection, String message) {
		if (collection == null || collection.size() == 0)
			throw new IllegalArgumentException(message);
	}

	/**
	 * Checks if the player has the given permission, if false we send him {@link SimpleLocalization#NO_PERMISSION}
	 * message and return false, otherwise no message is sent and we return true
	 *
	 * @param sender
	 * @param permission
	 * @return
	 */
	public boolean checkPermission(CommandSender sender, String permission) {
		if (!PlayerUtil.hasPerm(sender, permission)) {
			Common.tell(sender, SimpleLocalization.NO_PERMISSION.replace("{permission}", permission));

			return false;
		}

		return true;
	}

	/**
	 * Checks if the code calling this method is run from the main thread,
	 * failing with the error message if otherwise
	 *
	 * @param syncErrorMessage
	 */
	public void checkSync(String asyncErrorMessage) {
		checkBoolean(Bukkit.isPrimaryThread(), asyncErrorMessage);
	}

	/**
	 * Checks if the code calling this method is run from a different than main thread,
	 * failing with the error message if otherwise
	 *
	 * @param syncErrorMessage
	 */
	public void checkAsync(String syncErrorMessage) {
		checkBoolean(!Bukkit.isPrimaryThread(), syncErrorMessage);
	}

	// ------------------------------------------------------------------------------------------------------------
	// Checking for true without throwing errors
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Returns true if the given string is a valid integer
	 *
	 * @param raw
	 * @return
	 */
	public boolean isInteger(String raw) {
		return PATTERN_INTEGER.matcher(raw).find();
	}

	/**
	 * Returns true if the given string is a valid whole number
	 *
	 * @param raw
	 * @return
	 */
	public boolean isDecimal(String raw) {
		return PATTERN_DECIMAL.matcher(raw).find();
	}

	/**
	 * Return true if the array consists of null or empty string values only
	 *
	 * @param array
	 * @return
	 */
	public boolean isNullOrEmpty(Collection<?> array) {
		return array == null ? true : isNullOrEmpty(array.toArray());
	}

	/**
	 * Return true if the array consists of null or empty string values only
	 *
	 * @param array
	 * @return
	 */
	public boolean isNullOrEmpty(Object[] array) {
		if (array != null)
			for (final Object object : array)
				if (object instanceof String) {
					if (!((String) object).isEmpty())
						return false;

				} else if (object != null)
					return false;

		return true;
	}

	/**
	 * Return true if the given message is null or empty
	 *
	 * @param message
	 * @return
	 */
	public boolean isNullOrEmpty(String message) {
		return message == null || message.isEmpty();
	}

	/**
	 * Return true if all x-y-z coordinates of the given vector are finite valid numbers
	 * (see {@link Double#isFinite(double)})
	 *
	 * @param vector
	 * @return
	 */
	public boolean isFinite(Vector vector) {
		return Double.isFinite(vector.getX()) && Double.isFinite(vector.getY()) && Double.isFinite(vector.getZ());
	}

	/**
	 * Return true if the given value is between bounds
	 *
	 * @param value
	 * @param ranged
	 * @return
	 */
	public boolean isInRange(long value, RangedValue ranged) {
		return value >= ranged.getMinLong() && value <= ranged.getMaxLong();
	}

	/**
	 * Return true if the given value is between bounds
	 *
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public boolean isInRange(double value, double min, double max) {
		return value >= min && value <= max;
	}

	/**
	 * Return true if the given value is between bounds
	 *
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public boolean isInRange(long value, long min, long max) {
		return value >= min && value <= max;
	}

	// ------------------------------------------------------------------------------------------------------------
	// Equality checks
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Returns true if the two locations has same world and block positions
	 *
	 * @param first
	 * @param sec
	 * @return
	 */
	public boolean locationEquals(Location first, Location sec) {
		if (!first.getWorld().getName().equals(sec.getWorld().getName()))
			return false;

		return first.getBlockX() == sec.getBlockX() && first.getBlockY() == sec.getBlockY() && first.getBlockZ() == sec.getBlockZ();
	}

	/**
	 * Compare two lists. Two lists are considered equal if they are same length and all values are the same.
	 * Exception: Strings are stripped of colors before comparation.
	 *
	 * @param first, first list to compare
	 * @param second, second list to compare with
	 * @return true if lists are equal
	 */
	public <T> boolean listEquals(List<T> first, List<T> second) {
		if (first == null && second == null)
			return true;

		if (first == null && second != null)
			return false;

		if (first != null && second == null)
			return false;

		if (first != null) {
			if (first.size() != second.size())
				return false;

			for (int i = 0; i < first.size(); i++) {
				final T f = first.get(i);
				final T s = second.get(i);

				if (f == null && s != null)
					return false;

				if (f != null && s == null)
					return false;

				if (f != null && s != null && !f.equals(s))
					if (!Common.stripColors(f.toString()).equalsIgnoreCase(Common.stripColors(s.toString())))
						return false;
			}
		}

		return true;
	}

	/**
	 * Returns true if two strings are equal regardless of their colors
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public boolean colorlessEquals(String first, String second) {
		return Common.stripColors(first).equals(Common.stripColors(second));
	}

	/**
	 * Returns true if two string lists are equal regardless of their colors
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public boolean colorlessEquals(List<String> first, List<String> second) {
		return colorlessEquals(Common.toArray(first), Common.toArray(second));
	}

	/**
	 * Returns true if two string arrays are equal regardless of their colors
	 *
	 * @param firstArray
	 * @param secondArray
	 * @return
	 */
	public boolean colorlessEquals(String[] firstArray, String[] secondArray) {
		for (int i = 0; i < firstArray.length; i++) {
			final String first = Common.stripColors(firstArray[i]);
			final String second = i < secondArray.length ? Common.stripColors(secondArray[i]) : "";

			if (!first.equals(second))
				return false;
		}

		return true;
	}

	// ------------------------------------------------------------------------------------------------------------
	// Matching in lists
	// ------------------------------------------------------------------------------------------------------------

	/**
	 * Returns true if the given is in the given list, depending on the mode
	 *
	 * If isBlacklist mode is enabled, we return true when element is NOT in the list,
	 * if it is false, we return true if element is in the list.
	 *
	 * @param element
	 * @param isBlacklist
	 * @param list
	 * @return
	 */
	public boolean isInList(String element, boolean isBlacklist, Iterable<String> list) {
		return isBlacklist == isInList(element, list);
	}

	/**
	 * Returns true if any element in the given list equals (case ignored) to your given element
	 *
	 * @param element
	 * @param list
	 * @return
	 */
	public boolean isInList(String element, Iterable<String> list) {
		try {
			for (final String matched : list)
				if (normalizeEquals(element).equals(normalizeEquals(matched)))
					return true;

		} catch (final ClassCastException ex) { // for example when YAML translates "yes" to "true" to boolean (!) (#wontfix)
		}

		return false;
	}

	/**
	 * Returns true if any element in the given list starts with (case ignored) your given element
	 *
	 * @param element
	 * @param list
	 * @return
	 */
	public boolean isInListStartsWith(String element, Iterable<String> list) {
		try {
			for (final String matched : list)
				if (normalizeEquals(element).startsWith(normalizeEquals(matched)))
					return true;
		} catch (final ClassCastException ex) { // for example when YAML translates "yes" to "true" to boolean (!) (#wontfix)
		}

		return false;
	}

	/**
	 * Returns true if any element in the given list contains (case ignored) your given element
	 *
	 * @param element
	 * @param list
	 * @return
	 */
	public boolean isInListContains(String element, Iterable<String> list) {
		try {
			for (final String matched : list)
				if (normalizeEquals(element).contains(normalizeEquals(matched)))
					return true;

		} catch (final ClassCastException ex) { // for example when YAML translates "yes" to "true" to boolean (!) (#wontfix)
		}

		return false;
	}

	/**
	 * Returns true if any element in the given list matches your given element.
	 *
	 * A regular expression is compiled from that list element.
	 *
	 * @param element
	 * @param list
	 * @return
	 */
	public boolean isInListRegex(String element, Iterable<String> list) {
		try {
			for (final String regex : list)
				if (Common.regExMatch(regex, element))
					return true;

		} catch (final ClassCastException ex) { // for example when YAML translates "yes" to "true" to boolean (!) (#wontfix)
		}

		return false;
	}

	/**
	 * Prepares the message for isInList comparation - lowercases it and removes the initial slash /
	 *
	 * @param message
	 * @return
	 */
	private String normalizeEquals(String message) {
		if (message.startsWith("/"))
			message = message.substring(1);

		return message.toLowerCase();
	}
}
