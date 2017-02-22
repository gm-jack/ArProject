package com.rtmap.game.camera;

import android.hardware.Camera;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.interfaces.DeviceCameraControl;

import java.util.List;

public class AndroidDeviceCameraController implements DeviceCameraControl,
        Camera.PictureCallback, Camera.AutoFocusCallback {

    private AndroidLauncher androidLauncher;
    //	private static final int ONE_SECOND_IN_MILI = 1000;
    private CameraSurface cameraSurface;

    public AndroidDeviceCameraController(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public synchronized void prepareCamera() {
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        display.getMetrics(displayMetrics);
//        activity.setFixedSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
//        activity.setFixedSize(960, 640);
        if (cameraSurface == null) {
            cameraSurface = new CameraSurface(androidLauncher);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        //设置顶部,左边布局
//		params.gravity=Gravity.CENTER_HORIZONTAL|Gravity.RIGHT;
//		params.gravity=Gravity.LEFT|Gravity.RIGHT;
//        androidLauncher.addContentView(cameraSurface, params);
    }

    @Override
    public synchronized void startPreview() {
        // ...and start previewing. From now on, the camera keeps pushing
        // preview
        // images to the surface.
        if (cameraSurface != null && cameraSurface.getCamera() != null) {
            cameraSurface.getCamera().startPreview();
        }
    }

    @Override
    public synchronized void stopPreview() {
        // stop previewing.
        if (cameraSurface != null) {
            ViewParent parentView = cameraSurface.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(cameraSurface);

            }
            if (cameraSurface.getCamera() != null) {
                cameraSurface.getCamera().stopPreview();
            }
            cameraSurface = null;
        }
        androidLauncher.restoreFixedSize();
    }

    public void setCameraParametersForPicture(Camera camera) {
        // Before we take the picture - we make sure all camera parameters are
        // as we like them
        // Use max resolution and auto focus
        Camera.Parameters p = camera.getParameters();
        List<Camera.Size> supportedSizes = p.getSupportedPictureSizes();
        int maxSupportedWidth = -1;
        int maxSupportedHeight = -1;
        for (Camera.Size size : supportedSizes) {
            if (size.width > maxSupportedWidth) {
                maxSupportedWidth = size.width;
                maxSupportedHeight = size.height;
            }
        }
        p.setPictureSize(maxSupportedWidth, maxSupportedHeight);
        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters(p);
    }

    @Override
    public synchronized void onAutoFocus(boolean success, Camera camera) {
        // Focus process finished, we now have focus (or not)
        if (success) {
            if (camera != null) {
                camera.stopPreview();
                // We now have focus take the actual picture
                camera.takePicture(null, null, null, this);
            }
        }
    }

    @Override
    public void prepareCameraAsync() {
        Runnable r = new Runnable() {
            public void run() {
                prepareCamera();
            }
        };
        androidLauncher.post(r);
    }

    @Override
    public synchronized void startPreviewAsync() {
        Runnable r = new Runnable() {
            public void run() {
                startPreview();
            }
        };
        androidLauncher.post(r);
    }

    @Override
    public synchronized void stopPreviewAsync() {
        Runnable r = new Runnable() {
            public void run() {
                stopPreview();
            }
        };
        androidLauncher.post(r);
    }

    @Override
    public boolean isReady() {
        if (cameraSurface != null && cameraSurface.getCamera() != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {


    }
}