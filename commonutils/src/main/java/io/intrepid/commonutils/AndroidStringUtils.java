package io.intrepid.commonutils;

import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

/**
 * String utility methods that touches Android classes. Methods inside this class
 * need to be mocked for unit tests
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public abstract class AndroidStringUtils {
    /**
     * Appends a string to another string.
     * <p>
     * The 'string' input parameter may change if the other string can be appended in place.
     *
     * @param string         The string to be appended.
     * @param stringToAppend The string to append.
     * @return The appended string. The returned object may be the same as 'string'.
     */
    public static CharSequence append(CharSequence string, CharSequence stringToAppend) {
        if (string instanceof Editable) {
            return ((Editable) string).append(stringToAppend);
        } else {
            SpannableStringBuilder sb = (string !=
                    null) ? new SpannableStringBuilder(string) : new SpannableStringBuilder();
            return sb.append(stringToAppend);
        }
    }

    /**
     * Appends a character to a string.
     * <p>
     * The 'string' input parameter may change if the character can be appended in place.
     *
     * @param string       The string to be appended.
     * @param charToAppend The character to append.
     * @return The appended string. This may be the same as 'string'.
     */
    public static CharSequence append(CharSequence string, char charToAppend) {
        if (string instanceof Editable) {
            return ((Editable) string).append(charToAppend);
        } else {
            SpannableStringBuilder sb = (string !=
                    null) ? new SpannableStringBuilder(string) : new SpannableStringBuilder();
            return sb.append(charToAppend);
        }
    }

    /**
     * Implements the {@link android.text.SpannableStringBuilder#append(CharSequence, Object, int)} for versions below api-level 21.
     *
     * @param builder The build to which 'text' will be appended.
     * @param text    The text to append.
     * @param what    The object to be spanned over the appended text.
     * @param flags   ee {@link Spanned}.
     * @return The 'builder' parameter.
     */
    public static SpannableStringBuilder append(SpannableStringBuilder builder,
                                                CharSequence text,
                                                Object what,
                                                int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return builder.append(text, what, flags);
        } else {
            final int start = builder.length();
            builder.append(text);
            builder.setSpan(what, start, builder.length(), flags);

            return builder;
        }
    }

    /**
     * Implements {@link Html#fromHtml(String, int)}. Prior to API level 24, the flags will not be
     * applied and this method simply calls through to {@link Html#fromHtml(String)}.
     * @param source The string to be styled with HTML tags
     * @param flags  Any behavior flags in {@link Html}, or 0 for default behavior.
     * @return The text styled with HTML tags.
     */
    public static Spanned fromHtml(String source, int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, flags);
        } else {
            //noinspection deprecation
            return Html.fromHtml(source);
        }
    }
}
