package io.intrepid.commonutils.photos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static io.intrepid.commonutils.photos.PhotoHelper.CAMERA_IMAGE_FILE_PATH;
import static io.intrepid.commonutils.photos.PhotoHelper.REMOVE_UNUSED_PHOTOS;
import static io.intrepid.commonutils.photos.PhotoHelper.REQUEST_CODE_PICK_PHOTO;
import static io.intrepid.commonutils.photos.PhotoHelper.REQUEST_CODE_WRITE_STORAGE_PERMISSION;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PhotoHelperTest {
    @Mock
    PhotoContract.View mockView;
    @Mock
    PhotoContract.Presenter<PhotoContract.View> mockPresenter;
    @Mock
    Fragment mockFragment;

    private PhotoContract.Helper photoHelper;

    @Before
    public void setup() {
        photoHelper = spy(new PhotoHelper(mockFragment, mockView, false));
        when(mockView.getPhotoPresenter()).thenReturn(mockPresenter);
    }

    @Test
    public void onActivityResult_selectFromLibrary() {
        final String PHOTO_URI = "http://example.com/foo.png";
        Uri mockUri = Mockito.mock(Uri.class);
        when(mockUri.toString()).thenReturn(PHOTO_URI);
        Intent mockIntent = Mockito.mock(Intent.class);
        when(mockIntent.getData()).thenReturn(mockUri);

        photoHelper.onActivityResult(REQUEST_CODE_PICK_PHOTO, Activity.RESULT_OK, mockIntent);

        verify(mockPresenter).onPictureChosen(PHOTO_URI);
        verify(mockPresenter, never()).onNoPictureChosen();
        PhotoHelper realPhotoHelper = (PhotoHelper) photoHelper;
        verify(realPhotoHelper).deleteUnusedPhotos();
    }

    @Test
    public void onActivityResult_takeNewPhoto() {
        final String PHOTO_URI = "http://example.com/foo.png";
        PhotoHelper realPhotoHelper = (PhotoHelper) photoHelper;
        realPhotoHelper.cameraImageFilePath = PHOTO_URI;

        photoHelper.onActivityResult(REQUEST_CODE_PICK_PHOTO, Activity.RESULT_OK, null);

        verify(mockPresenter).onPictureChosen(PHOTO_URI);
        verify(mockPresenter, never()).onNoPictureChosen();
        verify(realPhotoHelper, never()).deleteUnusedPhotos();
    }

    @Test
    public void onActivityResult_cancel() {
        Intent mockIntent = Mockito.mock(Intent.class);

        photoHelper.onActivityResult(REQUEST_CODE_PICK_PHOTO, Activity.RESULT_CANCELED, mockIntent);

        verify(mockPresenter, never()).onPictureChosen(anyString());
        verify(mockPresenter).onNoPictureChosen();
        PhotoHelper realPhotoHelper = (PhotoHelper) photoHelper;
        verify(realPhotoHelper).deleteUnusedPhotos();
    }

    @Test
    public void onCreate_withSavedState() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        final String SAVED_CAMERA_IMAGE_FILE_PATH = "path";
        when(mockBundle.getString(CAMERA_IMAGE_FILE_PATH)).thenReturn(SAVED_CAMERA_IMAGE_FILE_PATH);
        final boolean SAVED_REMOVE_UNUSED_PHOTOS = true;
        when(mockBundle.getBoolean(REMOVE_UNUSED_PHOTOS)).thenReturn(SAVED_REMOVE_UNUSED_PHOTOS);

        photoHelper.onCreate(mockBundle);

        verify(mockBundle).getString(CAMERA_IMAGE_FILE_PATH);
        verify(mockBundle).getBoolean(REMOVE_UNUSED_PHOTOS);
    }

    @Test
    public void onRequestPermissionsResult_permissionGranted() {
        // Mock out the showImagePickerIntent() method, since it's dependent on Android-specific code
        PhotoHelper realPhotoHelper = (PhotoHelper) photoHelper;
        doNothing().when(realPhotoHelper).showImagePickerIntent();

        photoHelper.onRequestPermissionsResult(REQUEST_CODE_WRITE_STORAGE_PERMISSION,
                                               new String[] {},
                                               new int[] { PackageManager.PERMISSION_GRANTED });

        verify(realPhotoHelper).showImagePickerIntent();
        verify(mockPresenter, never()).logError(anyString(), (Exception) anyObject());
        verify(mockPresenter, never()).onPermissionDenied();
    }

    @Test
    public void onRequestPermissionsResult_permissionDenied() {
        PhotoHelper realPhotoHelper = (PhotoHelper) photoHelper;
        final String PERMISSION_DENIED_MESSAGE = "DENIED";
        when(mockView.getPermissionDeniedMessage()).thenReturn(PERMISSION_DENIED_MESSAGE);

        photoHelper.onRequestPermissionsResult(REQUEST_CODE_WRITE_STORAGE_PERMISSION,
                                               new String[] {},
                                               new int[] { PackageManager.PERMISSION_DENIED });

        verify(realPhotoHelper, never()).showImagePickerIntent();
        verify(mockView).getPermissionDeniedMessage();
        verify(mockPresenter).logError(PERMISSION_DENIED_MESSAGE, null);
    }

    @Test
    public void onSaveInstanceState_dontRemoveUnusedPhotos() {
        Bundle mockBundle = Mockito.mock(Bundle.class);

        photoHelper.onSaveInstanceState(mockBundle);

        verify(mockBundle).putBoolean(REMOVE_UNUSED_PHOTOS, false);
    }

    @Test
    public void onSaveInstanceState_removeUnusedPhotos() {
        // Re-initialize with passing true to remove used photos
        photoHelper = spy(new PhotoHelper(mockFragment, mockView, true));
        Bundle mockBundle = Mockito.mock(Bundle.class);

        photoHelper.onSaveInstanceState(mockBundle);

        verify(mockBundle).putBoolean(REMOVE_UNUSED_PHOTOS, true);
    }

    @Test
    public void onSaveInstanceState_noCameraImageFilePath() {
        Bundle mockBundle = Mockito.mock(Bundle.class);

        photoHelper.onSaveInstanceState(mockBundle);

        verify(mockBundle, never()).putString(CAMERA_IMAGE_FILE_PATH, null);
    }

    @Test
    public void onSaveInstanceState_withCameraImageFilePath() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        final String CAMERA_PATH = "path";
        PhotoHelper realPhotoHelper = (PhotoHelper) photoHelper;
        realPhotoHelper.cameraImageFilePath = CAMERA_PATH;

        photoHelper.onSaveInstanceState(mockBundle);

        verify(mockBundle).putString(CAMERA_IMAGE_FILE_PATH, CAMERA_PATH);
    }
}
