package io.intrepid.commonutils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class JavaUtils {

    /**
     * Returns the string representation of the input byte array. The resulting string will be
     * in the same order as the input array. Ex: if the input is [0xab,0x12], the resulting string
     * will be "ab12"
     */
    @NonNull
    public static String bytesToHexString(@NonNull byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static int byteToUnsignedInt(byte b) {
        return b & 0xff;
    }

    /**
     * Returns a non-null string regardless of whether the input string is null
     *
     * @param string Input string, can be null
     * @return The input string if it's not null, or a empty "" string if the input is null
     */
    @NonNull
    public static String nonNullString(@Nullable String string) {
        return string == null ? "" : string;
    }

    /**
     * Move an element from an index to another and shifts other elements accordingly. This is similar to performing
     * a list.remove() and then list.add(), but is done in place so that the list doesn't need to resize.
     * This is often used in conjunction with RecyclerView.Adapter.notifyItemMoved(fromIndex, toIndex) to move an
     * item in RecyclerView.
     *
     * @param list      The input list
     * @param fromIndex The index of the object that will be moved
     * @param toIndex   The destination index.
     */
    public static void moveElementInList(@NonNull List<?> list, int fromIndex, int toIndex) {
        if (fromIndex == toIndex || fromIndex < 0 || toIndex > list.size() - 1) {
            return;
        }

        if (fromIndex < toIndex) {
            Collections.rotate(list.subList(fromIndex, toIndex + 1), -1);
        } else {
            Collections.rotate(list.subList(toIndex, fromIndex + 1), 1);
        }
    }
}
