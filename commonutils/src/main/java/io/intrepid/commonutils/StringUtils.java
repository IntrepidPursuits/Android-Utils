package io.intrepid.commonutils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A utility class bundling a set of handy to use methods that deal with {@link String},
 * {@link CharSequence}, etc.
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public abstract class StringUtils {
    private static final ThreadLocal<Pattern[]> MATCHING_PATTERNS = new ThreadLocal<Pattern[]>() {
        @Override
        protected Pattern[] initialValue() {
            return new Pattern[] {
                    Pattern.compile("[\\t\\n\\u0590-\\uFFFF]"), // non-boring characters.
                    Pattern.compile("[^a-zA-Z0-9]"), // non-alpha-numeric.
                    Pattern.compile("^([a-z0-9A-Z]+[-_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$") // Email-address pattern.
            };
        }
    };

    private static final ThreadLocal<char[]> LOCAL_CHAR_ARRAY_BUFFER = new ThreadLocal<char[]>() {
        @Override
        protected char[] initialValue() {
            return new char[8192];
        }
    };

    // region TextUtils methods
    // These methods are copied from TextUtils class so that we don't need to mock them when running unit tests

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * Returns true if a and b are equal, including if they are both null.
     * <p><i>Note: In platform versions 1.1 and earlier, this method only worked well if
     * both the arguments were instances of String.</i></p>
     *
     * @param a first CharSequence to check
     * @param b second CharSequence to check
     * @return true if a and b are equal
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     *
     * @param tokens an array objects to be joined. Strings will be formed from
     *               the objects by calling object.toString().
     */
    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = tokens.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(delimiter);
                sb.append(it.next());
            }
        }
        return sb.toString();
    }

    // endregion

    /**
     * Removes any trailing and leading whitespaces from the input characters and returns
     * the resulting characters without trailing and leading whitespaces.
     *
     * @param s   Input characters.
     * @param <T> Type of the input characters.
     * @return The stripped characters.
     */
    public static <T extends CharSequence> T stripSurroundingWhiteSpace(T s) {
        if (isEmpty(s)) {
            return s;
        }

        final int len = s.length();

        int startIdx = -1;
        for (int idx = 0; idx < len; idx++) {
            if (!Character.isWhitespace(s.charAt(idx))) {
                startIdx = idx;
                break;
            }
        }

        if (startIdx < 0) {
            //noinspection unchecked
            return (T) ((s instanceof String) ? "" : s.subSequence(0, 0));
        }

        int endIdx = startIdx - 1;
        for (int idx = len - 1; idx > endIdx; idx--) {
            if (!Character.isWhitespace(s.charAt(idx))) {
                endIdx = idx;
                break;
            }
        }

        //noinspection unchecked
        return (T) s.subSequence(startIdx, endIdx + 1);
    }

    /**
     * Replaces non-boring character with the given replacement string.
     * Note that the replacement string must contain only boring characters.
     *
     * @param string      String whose non-boring character will be replace with the given replacement.
     * @param replacement The replacement.
     * @return The string without boring characters.
     * @see android.text.BoringLayout
     * <p>
     * Boring characters are all character less than \u0590 and not a tab
     * and not a new line.
     */
    public static String replaceNonBoringCharacters(@NonNull CharSequence string, @NonNull String replacement) {
        return isEmpty(string) ? "" : MATCHING_PATTERNS.get()[0].matcher(string).replaceAll(replacement);
    }

    /**
     * Strips all non alpha-numeric characters from the input string.
     * An alpha-numeric character is a...z, A...Z and 0...
     *
     * @param s Input string
     * @return The string without any alpha-numeric characters.
     */
    public static String stripNonAlphaNumeric(CharSequence s) {
        return MATCHING_PATTERNS.get()[1].matcher(s).replaceAll("");
    }

    /**
     * Checks if the input string is a valid email.
     *
     * @param email The string to check.
     * @return True only if the given string is a valid email address.
     */
    public static boolean isEmailAddress(CharSequence email) {
        return !isEmpty(email) && MATCHING_PATTERNS.get()[2].matcher(email).matches();
    }

    /**
     * Returns the name of the enumeration value or null if the input is null.
     *
     * @param enumValue The enumeration value.
     * @param <E>       The enumeration type.
     * @return The name of the enumeration value or null.
     */
    public static <E extends Enum<E>> String toEnumString(E enumValue) {
        return (enumValue != null) ? enumValue.name() : null;
    }

    /**
     * Returns the enumeration value whose name is the given input string or null if the input string is null.
     *
     * @param enumClass The class of the enumeration that will be returned.
     * @param enumName  The name of the enumeration.
     * @param <E>       The type of the enumeration.
     * @return The enumeration value.
     */
    public static <E extends Enum<E>> E toEnum(@NonNull Class<E> enumClass, Object enumName) {
        return (enumName != null) ? Enum.valueOf(enumClass, enumName.toString()) : null;
    }

    /**
     * Given a string with elements that are separated by a separator-character, return
     * the elements in that string as a string-array. Whitespace of each string in the
     * returned array has been trimmed.
     *
     * @param separatedList List of elements separated by the separator.
     * @param separator     The separator.
     * @return The elements in the given list.
     */
    public static String[] parseSeparatedArray(String separatedList, @NonNull String separator) {
        if (separatedList == null) {
            return null;
        }

        if (isEmpty(separatedList)) {
            return new String[0];
        }

        return stripSurroundingWhiteSpace(separatedList).split("\\s*" + Pattern.quote(separator) + "\\s*");
    }

    /**
     * Given a string with elements that are separated by a separator-character, return
     * the elements in that string as a list of strings.
     *
     * @param separatedList List of elements separated by the separator.
     * @param separator     The separator.
     * @return The elements in the given list.
     */
    public static List<String> parseSeparatedList(String separatedList, @NonNull String separator) {
        String[] array = parseSeparatedArray(separatedList, separator);
        if (array == null) {
            return null;
        }

        List<String> retVal = new ArrayList<>(array.length);
        if (array.length == 0) {
            return retVal;
        }

        retVal.addAll(Arrays.asList(array));
        return retVal;
    }

    /**
     * Returns the provided list of items as a String where each item is separated by the given separator string.
     * Items that are empty or null are skipped entirely (i.e. there won't be two or more separators directly
     * following each other).
     * <p>
     * E.g.
     * separateItemsWith(", ", "Hello", "This", null, "is", "", "my name")
     * will return
     * "Hello, This, is, my name"
     *
     * @param separator The string with which to separate the items.
     * @param items     The items that appear in the list.
     * @return The String of items.
     */
    public static CharSequence separateItemsWith(CharSequence separator, CharSequence... items) {
        StringBuilder sb = new StringBuilder();
        for (CharSequence item : items) {
            if (isEmpty(item)) {
                continue;
            }

            if (sb.length() == 0) {
                sb.append(item);
            } else {
                sb.append(separator).append(item);
            }
        }
        return sb;
    }

    /**
     * @param separator The string with which to separate the items.
     * @param items     The items that appear in the list.
     * @return The String of items.
     * @see #separateItemsWith(CharSequence, CharSequence...)
     */
    public static CharSequence separateItemsAsStringsWith(CharSequence separator, List<?> items) {
        if ((items == null) || items.isEmpty()) {
            return "";
        }

        CharSequence[] itemsArray = new CharSequence[items.size()];
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            itemsArray[i] = (item != null) ? items.get(i).toString() : null;
        }
        return separateItemsWith(separator, itemsArray);
    }

    /**
     * @param separator The string with which to separate the items.
     * @param items     The items that appear in the list.
     * @return The String of items.
     * @see #separateItemsWith(CharSequence, CharSequence...)
     */
    public static CharSequence separateItemsWith(CharSequence separator, List<? extends CharSequence> items) {
        if ((items == null) || items.isEmpty()) {
            return "";
        }

        CharSequence[] itemsArray = items.toArray(new CharSequence[items.size()]);
        return separateItemsWith(separator, itemsArray);
    }

    /**
     * Repeats the input string a number of times, each repetition separated from the other by the separator.
     * The returned string will never start or end with the given separator unless the input string is empty.
     *
     * @param string      The string to repeat.
     * @param separator   The separator between repetitions.
     * @param repeatCount The times that the given string should be repeated.
     * @return The repeated string.
     */
    public static CharSequence repeat(CharSequence string, CharSequence separator, int repeatCount) {
        if (repeatCount < 1) {
            return "";
        }

        if (repeatCount == 1) {
            return string;
        }

        if (isEmpty(string) && isEmpty(separator)) {
            return string;
        }

        if (string == null) {
            string = "";
        }

        if (separator == null) {
            separator = "";
        }

        final boolean stringIsNotEmpty = string.length() > 0;
        final boolean separatorIsNotEmpty = separator.length() > 0;

        final StringBuilder sb = new StringBuilder((string.length() + separator.length()) * repeatCount);

        for (int i = 0; i < repeatCount; i++) {
            if (separatorIsNotEmpty && (i > 0)) {
                sb.append(separator);
            }

            if (stringIsNotEmpty) {
                sb.append(string);
            }
        }

        return sb.toString();
    }

    /**
     * Calculates the Levenshtein Distance between two string, i.e. how many edits/changes would it
     * take to get from string1 to string2.
     * http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance
     *
     * @param string1 The first string.
     * @param string2 The second string.
     * @return The distance between the two strings.
     */
    public static int getLevenshteinDistance(CharSequence string1, CharSequence string2) {
        int len1 = string1.length() + 1;
        int len2 = string2.length() + 1;

        // the array of distances
        int[] cost = new int[len1];
        int[] newcost = new int[len1];

        // initial cost of skipping prefix in String string1
        for (int i = 0; i < len1; i++) cost[i] = i;

        // dynamicaly computing the array of distances

        // transformation cost for each letter in string2
        for (int j = 1; j < len2; j++) {
            // initial cost of skipping prefix in String string2
            newcost[0] = j;

            // transformation cost for each letter in string1
            for (int i = 1; i < len1; i++) {
                // matching current letters in both strings
                int match = (string1.charAt(i - 1) == string2.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len1 - 1];
    }

    /**
     * Has the same function as {@link java.lang.String#indexOf(int, int)} but it works on CharSequences as well.
     *
     * @param string The string to check.
     * @param c      The character to look for.
     * @param start  The index to start at (must be 0 &le; stat &lt; string.length())
     * @return The first index on or after 'start' on which the character has been found or -1 if not found.
     */
    public static int indexOf(@NonNull CharSequence string, char c, int start) {
        for (int i = start; i < string.length(); i++) {
            if (string.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Has the same function as {@link java.lang.String#compareTo(String)} but it works on CharSequences as well.
     *
     * @param cs1 First string.
     * @param cs2 Second string.
     * @return The same value that {@link java.lang.String#compareTo(String)} would return when given the same
     * instance and input parameters.
     */
    public static int compare(CharSequence cs1, CharSequence cs2) {
        if (cs1 instanceof Comparable && cs2 instanceof Comparable) {
            //noinspection unchecked
            return ((Comparable) cs1).compareTo(cs2);
        }

        if (cs1 == null && cs2 == null) {
            return 0;
        } else if (cs1 == null) {
            return -1;
        } else if (cs2 == null) {
            return 1;
        }

        final int len1 = cs1.length();
        final int len2 = cs2.length();
        final int length = Math.min(len1, len2);

        for (int i = 0; i < length; i++) {
            final int c1 = cs1.charAt(i);
            final int c2 = cs2.charAt(i);
            final int diff = c1 - c2;
            if (diff < 0 || diff > 0) {
                return diff;
            }
        }

        return len1 - len2;
    }

    /**
     * Returns a String representation of the contents of an InputStream
     *
     * @param is The input stream of UTF-8 characters.
     * @return The String obtained from the input stream.
     * @throws IOException If an I/O error occurs
     */
    public static String toString(InputStream is) throws IOException {
        final char[] buffer = LOCAL_CHAR_ARRAY_BUFFER.get();
        final Writer writer = new StringWriter();

        try {
            final Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

            return writer.toString();
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }
}
