package io.intrepid.commonutils.photos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Helper class for displaying the system default photo chooser, for use with the MVP pattern
 */
@SuppressWarnings("WeakerAccess")
public class PhotoContract {

    /**
     * Android-specific "{@link View}" interface
     */
    public interface View {
        /**
         * Returns the {@link Presenter} associated with this {@link View}.
         *
         * @return The {@link Presenter} associated with this {@link View}
         */
        @NonNull Presenter getPhotoPresenter();

        /**
         * Specifies what string should be shown in the photo chooser.
         * <p>
         * A common choice might be "Select photo source"
         *
         * @return String resource to be displayed
         */
        @StringRes
        int getChooserLabel();

        /**
         * Error message to log when user denies photo permission.
         * <p>
         * @return String to be logged via the {@link io.intrepid.commonutils.photos.PhotoContract.Presenter#logError logError} method
         */
        @NonNull String getPermissionDeniedMessage();

        /**
         * Error message to use when an IO Exception occurs.
         * <p>
         * @return String to be logged via the {@link io.intrepid.commonutils.photos.PhotoContract.Presenter#logError logError} method
         */
        @NonNull String getIOExceptionMessage();
    }

    /**
     * Non-Android Presenter controller interface
     *
     * @param <T> The {@link View} that the {@link Presenter} is associated with
     */
    public interface Presenter<T extends View> {
        /**
         * Callback function which is called once the user has selected an image.
         *
         * @param imagePath The path to the image the user selected.
         */
        void onPictureChosen(@NonNull String imagePath);

        /**
         * Callback function which is called if the user did not select an image.
         */
        void onNoPictureChosen();

        /**
         * Callback function which is called when the user denies permission to access photos.
         */
        void onPermissionDenied();

        /**
         * Called when the {@link Helper} wants to log an error message.
         *
         * @param message - The error message
         * @param e - The exception that occurred, if there was one
         */
        void logError(@NonNull String message, @Nullable Exception e);
    }

    /**
     * Interface which contains delegate {@link android.support.v4.app.Fragment Fragment} methods that must be called in order to use the {@link Helper}
      */
    public interface Helper {
        /**
         * Entry method to show the image picker
         */
        void showImagePicker();

        //region These methods MUST be called from their respective Android methods in order to use the Helper
        /**
         * Delegate for Fragment's {@link android.support.v4.app.Fragment#onActivityResult onActivityResult} method.
         * <p>
         * Receives response from the photo chooser activity containing the user's choice
         *
         * @param requestCode - The requested action (picking a photo)
         * @param resultCode - Indicates whether the operation was successful or not
         * @param data - Associated data (the path to the selected image, if available)
         */
        void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

        /**
         * Delegate for Fragment's {@link android.support.v4.app.Fragment#onCreate onCreate} method.
         * <p>
         * Initializes the helper by restoring internal state
         *
         * @param savedInstanceState - Previous saved state, if available
         */
        void onCreate(@Nullable Bundle savedInstanceState);

        /**
         * Delegate for Fragment's {@link android.support.v4.app.Fragment#onRequestPermissionsResult onRequestPermissionsResult} method.
         * <p>
         * Receives response as to whether user granted Photo permissions or not
         *
         * @param requestCode - The requested action (accessing the user's photos)
         * @param permissions - Unused
         * @param grantResults - Result of the user's choice (permission granted or not)
         */
        void onRequestPermissionsResult(int requestCode,
                                        @NonNull String[] permissions,
                                        @NonNull int[] grantResults);

        /**
         * Delegate for Fragment's {@link android.support.v4.app.Fragment#onSaveInstanceState onSaveInstanceState} method.
         * <p>
         * Saves internal state so it can be restored
         *
         * @param outState - The Bundle to save state information into
         */
        void onSaveInstanceState(@NonNull Bundle outState);
        //endregion
    }
}
