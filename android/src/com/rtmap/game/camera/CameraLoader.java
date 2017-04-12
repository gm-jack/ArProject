package com.rtmap.game.camera;

import android.hardware.Camera;

import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.util.CameraHelper;

import jp.co.cyberagent.android.gpuimage.GPUImage;

/**
 * Created by yxy
 * on 2017/4/12.
 */

public class CameraLoader {

    private AndroidLauncher context;
    private CameraHelper helper;
    private GPUImage image;
    private int mCurrentCameraId = 0;
    private Camera mCameraInstance;

    public CameraLoader(AndroidLauncher launcher, CameraHelper helper, GPUImage image) {
        this.context = launcher;
        this.helper = helper;
        this.image = image;
    }

    public void onResume() {
        setUpCamera(mCurrentCameraId);
    }

    public void onPause() {
        releaseCamera();
    }

    public void switchCamera() {
        releaseCamera();
        mCurrentCameraId = (mCurrentCameraId + 1) % helper.getNumberOfCameras();
        setUpCamera(mCurrentCameraId);
    }

    private void setUpCamera(final int id) {
        mCameraInstance = getCameraInstance(id);
        Camera.Parameters parameters = mCameraInstance.getParameters();
        // TODO adjust by getting supportedPreviewSizes and then choosing
        // the best one for screen size (best fill screen)
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCameraInstance.setParameters(parameters);

        int orientation = helper.getCameraDisplayOrientation(
                context, mCurrentCameraId);
        CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
        helper.getCameraInfo(mCurrentCameraId, cameraInfo);
        boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
        image.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
        mCameraInstance.startPreview();
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    private Camera getCameraInstance(final int id) {
        Camera c = null;
        try {
            c = helper.openCamera(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    private void stopCamera() {
        mCameraInstance.stopPreview();
    }

    private void releaseCamera() {
        if (mCameraInstance != null) {
            mCameraInstance.setPreviewCallback(null);
            mCameraInstance.release();
            mCameraInstance = null;
        }
    }
}
