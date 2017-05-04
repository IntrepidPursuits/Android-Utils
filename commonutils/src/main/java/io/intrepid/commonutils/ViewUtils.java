package io.intrepid.commonutils;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.Toast;

@SuppressWarnings("WeakerAccess")
public class ViewUtils {

    /**
     * Sets the visibility of multiple views
     *
     * @param visibility the new visibility
     * @param views      the views whose visibility will be changed
     */
    public static void setVisibilities(int visibility, View... views) {
        if (views == null) {
            return;
        }
        for (View view : views) {
            if (view == null) {
                continue;
            }
            view.setVisibility(visibility);
        }
    }

    /**
     * Toggle a view's visibility between VISIBLE and GONE. If input view's visibility is INVISIBLE,
     * the method will toggle it to VISIBLE
     *
     * @param view the view that will have its visibility toggled
     */
    public static void toggleVisibility(@NonNull View view) {
        final int currentVisibility = view.getVisibility();
        view.setVisibility(currentVisibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    /**
     * Sets the width of a view
     *
     * @param view  the input view
     * @param width the new width
     */
    public static void setWidth(@NonNull View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    /**
     * Sets the height of a view
     *
     * @param view   the input view
     * @param height the new height in pixels
     */
    public static void setHeight(@NonNull View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    /**
     * Sets the width and height of a view
     *
     * @param view   the input view
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    public static void setWidthAndHeight(@NonNull View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    /**
     * Sets the layout weight of a view
     *
     * @param view   the input view
     * @param weight the new weight
     * @throws IllegalArgumentException If the view's parent container is not a LinearLayout
     */
    public static void setLayoutWeight(View view, float weight) throws IllegalArgumentException {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) layoutParams;
            linearLayoutParams.weight = weight;
            view.setLayoutParams(linearLayoutParams);
        } else {
            throw new IllegalArgumentException("The view's parent must be a linear layout");
        }
    }

    /**
     * Sets a view's left padding while retaining the padding from other sides
     *
     * @param view       the input view
     * @param newPadding the new left padding in pixels
     */
    public static void setPaddingLeft(@NonNull View view, int newPadding) {
        view.setPadding(newPadding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * Sets a view's right padding while retaining the padding from other sides
     *
     * @param view       the input view
     * @param newPadding the new right padding in pixels
     */
    public static void setPaddingRight(@NonNull View view, int newPadding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), newPadding, view.getPaddingBottom());
    }

    /**
     * Sets a view's top padding while retaining the padding from other sides
     *
     * @param view       the input view
     * @param newPadding the new top padding in pixels
     */
    public static void setPaddingTop(@NonNull View view, int newPadding) {
        view.setPadding(view.getPaddingLeft(), newPadding, view.getPaddingRight(), view.getPaddingBottom());
    }

    /**
     * Sets a view's start margin while retaining the margin from other sides
     *
     * @param view      the input view
     * @param newMargin the new start margin in pixels
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMarginStart(@NonNull View view, int newMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.setMarginStart(newMargin);
        } else {
            params.leftMargin = newMargin;
        }
        view.setLayoutParams(params);
    }

    /**
     * Sets a view's start margin while retaining the margin from other sides
     *
     * @param view      the input view
     * @param newMargin the new start margin in pixels
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMarginEnd(@NonNull View view, int newMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.setMarginEnd(newMargin);
        } else {
            params.rightMargin = newMargin;
        }
        view.setLayoutParams(params);
    }

    /**
     * Sets a view's bottom padding while retaining the padding from other sides
     *
     * @param view       the input view
     * @param newPadding the new bottom padding in pixels
     */
    public static void setPaddingBottom(@NonNull View view, int newPadding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), newPadding);
    }

    /**
     * Sets a view's left margin while retaining the margin from other sides
     *
     * @param view      the input view
     * @param newMargin the new left margin in pixels
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMarginLeft(@NonNull View view, int newMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        params.leftMargin = newMargin;
        view.setLayoutParams(params);
    }

    /**
     * Sets a view's right margin while retaining the margin from other sides
     *
     * @param view      the input view
     * @param newMargin the new right margin in pixels
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMarginRight(@NonNull View view, int newMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        params.rightMargin = newMargin;
        view.setLayoutParams(params);
    }

    /**
     * Sets a view's top margin while retaining the margin from other sides
     *
     * @param view      the input view
     * @param newMargin the new top margin in pixels
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMarginTop(@NonNull View view, int newMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        params.topMargin = newMargin;
        view.setLayoutParams(params);
    }

    /**
     * Sets a view's bottom margin while retaining the margin from other sides
     *
     * @param view      the input view
     * @param newMargin the new bottom margin in pixels
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMarginBottom(@NonNull View view, int newMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        params.bottomMargin = newMargin;
        view.setLayoutParams(params);
    }

    /**
     * Sets all four sides of a view's margin
     *
     * @param view      the input view
     * @param newMargin the margin in pixel that will be applied to all sides
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMargins(@NonNull View view, int newMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        params.leftMargin = newMargin;
        params.topMargin = newMargin;
        params.rightMargin = newMargin;
        params.bottomMargin = newMargin;
        view.setLayoutParams(params);
    }

    /**
     * Sets all four sides of a view's margin
     *
     * @param view         the input view
     * @param leftMargin   the new left margin in pixels
     * @param topMargin    the new top margin in pixels
     * @param rightMargin  the new right margin in pixels
     * @param bottomMargin the new bottom margin in pixels
     * @throws IllegalArgumentException If the view's parent container does not support margin params
     */
    public static void setMargins(@NonNull View view,
                                  int leftMargin,
                                  int topMargin,
                                  int rightMargin,
                                  int bottomMargin) throws IllegalArgumentException {
        ViewGroup.MarginLayoutParams params = getMarginLayoutParams(view);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        params.rightMargin = rightMargin;
        params.bottomMargin = bottomMargin;
        view.setLayoutParams(params);
    }

    private static ViewGroup.MarginLayoutParams getMarginLayoutParams(View view) throws IllegalArgumentException {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            return (ViewGroup.MarginLayoutParams) layoutParams;
        } else {
            throw new IllegalArgumentException("The view's parent must support margin parameters");
        }
    }

    /**
     * Find the index/position of a view within its parent
     *
     * @param view the input view
     * @return the index/position of the input view within its parent, or -1 if it doesn't have a parent
     */
    public static int getPositionInParent(@NonNull View view) {
        ViewParent parentView = view.getParent();
        if (parentView != null && parentView instanceof ViewGroup) {
            return ((ViewGroup) parentView).indexOfChild(view);
        } else {
            return -1;
        }
    }

    /**
     * Convenience method for setting the ripple hotspot during an onTouch event for Lollipop+ devices
     *
     * @param view  The input view
     * @param event The MotionEvent object from onTouch method
     */
    public static void setTouchEventHotSpot(View view, MotionEvent event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable background = view.getBackground();
            if (background != null) {
                background.setHotspot(event.getX(), event.getY());
            }
        }
    }

    /**
     * Shows a Toast next to a view/button describing what it does
     * <p>
     * This is basically copied over from ActionMenuItemView.onLongClick()
     *
     * @param anchorView The view where the toast will appear next to
     * @param text       the text inside the Toast
     */
    public static void showToolTip(@NonNull View anchorView, @NonNull CharSequence text) {
        final int[] screenPos = new int[2];
        final Rect displayFrame = new Rect();
        anchorView.getLocationOnScreen(screenPos);
        anchorView.getWindowVisibleDisplayFrame(displayFrame);

        final Context context = anchorView.getContext();
        final int width = anchorView.getWidth();
        final int height = anchorView.getHeight();
        final int midy = screenPos[1] + height / 2;
        int referenceX = screenPos[0] + width / 2;
        if (ViewCompat.getLayoutDirection(anchorView) == ViewCompat.LAYOUT_DIRECTION_LTR) {
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            referenceX = screenWidth - referenceX; // mirror
        }
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        if (midy < displayFrame.height()) {
            // Show along the top; follow action buttons
            toast.setGravity(Gravity.TOP | GravityCompat.END, referenceX,
                             screenPos[1] + height - displayFrame.top);
        } else {
            // Show along the bottom center
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
        }
        toast.show();
    }

    /**
     * Similar to {@link #showToolTip(View, CharSequence)}, but takes in a string resource instead
     */
    public static void showToolTip(@NonNull View anchorView, int textResource) {
        showToolTip(anchorView, anchorView.getResources().getString(textResource));
    }

    /**
     * Similar to {@link #showToolTip(View, CharSequence)}, but displays the view's contentDescription
     */
    public static void showToolTip(@NonNull View view) {
        showToolTip(view, view.getContentDescription());
    }

    /**
     * In older version of android (&lt; 5.0), setting certain background drawable (mainly layer-lists) will cause view's
     * padding to reset. So we need to explicitly remember and restore those paddings
     * <p>
     * https://code.google.com/p/android/issues/detail?id=27235
     *
     * @param view          The view whose background will be set
     * @param backgroundRes The drawable resource id for the background
     */
    public static void setBackgroundWhileRetainingPadding(@NonNull View view, @DrawableRes int backgroundRes) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int paddingTop = view.getPaddingTop();
            int paddingLeft = view.getPaddingLeft();
            int paddingRight = view.getPaddingRight();
            int paddingBottom = view.getPaddingBottom();
            view.setBackgroundResource(backgroundRes);
            view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        } else {
            view.setBackgroundResource(backgroundRes);
        }
    }
}
