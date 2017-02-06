package io.intrepid.commonutils.photos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Concrete implementation of the {@link io.intrepid.commonutils.photos.PhotoContract.Helper PhotoContract.Helper} interface
 */
@SuppressWarnings("WeakerAccess")
public class PhotoHelper implements PhotoContract.Helper {
    @VisibleForTesting
    static final String CAMERA_IMAGE_FILE_PATH = "camera_image_file_path";
    @VisibleForTesting
    static final String REMOVE_UNUSED_PHOTOS = "remove_unused_photos";
    @VisibleForTesting
    static final int REQUEST_CODE_PICK_PHOTO = 31;
    @VisibleForTesting
    static final int REQUEST_CODE_WRITE_STORAGE_PERMISSION = 45;

    private Fragment fragment;
    private PhotoContract.View view;
    @VisibleForTesting
    private boolean removeUnusedPhotos;
    String cameraImageFilePath;

    /**
     * Creates a PhotoHelper instance
     *
     * @param fragment           - The {@link Fragment} that will be calling the {@link PhotoHelper}
     * @param view               - The {@link io.intrepid.commonutils.photos.PhotoContract.View View} interface (may very likely be the {@link Fragment})
     * @param removeUnusedPhotos - If true, delete unused new photos that the user takes with the camera
     */
    public PhotoHelper(@NonNull Fragment fragment, @NonNull PhotoContract.View view, boolean removeUnusedPhotos) {
        this.fragment = fragment;
        this.view = view;
        this.removeUnusedPhotos = removeUnusedPhotos;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_PHOTO) {
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();
                    // User picked the picture from gallery
                    view.getPhotoPresenter().onPictureChosen(imageUri.toString());
                    deleteUnusedPhotos();
                    return;
                } else {
                    if (cameraImageFilePath != null) {
                        // User took a new photo
                        view.getPhotoPresenter().onPictureChosen(cameraImageFilePath);

                        // Reset this for next time
                        cameraImageFilePath = null;
                        return;
                    }
                }
            }
        }

        view.getPhotoPresenter().onNoPictureChosen();
        deleteUnusedPhotos();
    }

    /**
     * Helper function to delete unused photos
     */
    @VisibleForTesting
    void deleteUnusedPhotos() {
        if (removeUnusedPhotos && cameraImageFilePath != null) {
            File file = new File(cameraImageFilePath);
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        cameraImageFilePath = null;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            cameraImageFilePath = savedInstanceState.getString(CAMERA_IMAGE_FILE_PATH);
            removeUnusedPhotos = savedInstanceState.getBoolean(REMOVE_UNUSED_PHOTOS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImagePickerIntent();
            } else {
                PhotoContract.Presenter photoPresenter = view.getPhotoPresenter();
                photoPresenter.logError(view.getPermissionDeniedMessage(), null);
                photoPresenter.onPermissionDenied();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (cameraImageFilePath != null) {
            outState.putString(CAMERA_IMAGE_FILE_PATH, cameraImageFilePath);
        }

        outState.putBoolean(REMOVE_UNUSED_PHOTOS, removeUnusedPhotos);
    }

    @Override
    public void showImagePicker() {
        if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            fragment.requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CODE_WRITE_STORAGE_PERMISSION);
        } else {
            showImagePickerIntent();
        }
    }

    /**
     * Helper function which launches the image picker intent
     */
    @VisibleForTesting
    void showImagePickerIntent() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");

        final List<Intent> extraIntents = new ArrayList<>();
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(fragment.getActivity().getPackageManager()) != null) {
                File storageDir = fragment.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File tempCameraImageFile = File.createTempFile(UUID.randomUUID().toString(), ".jpg", storageDir);
                Uri uri = Uri.fromFile(tempCameraImageFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                extraIntents.add(cameraIntent);

                cameraImageFilePath = "file://" + tempCameraImageFile.getAbsolutePath();
            }
        } catch (IOException e) {
            view.getPhotoPresenter().logError(view.getIOExceptionMessage(), e);
        }

        final Intent chooserIntent = Intent.createChooser(galleryIntent, fragment.getString(view.getChooserLabel()));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toArray(new Parcelable[extraIntents.size()]));
        fragment.startActivityForResult(chooserIntent, REQUEST_CODE_PICK_PHOTO);
    }
}
