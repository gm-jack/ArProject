package com.rtmap.game.camera;

import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.interfaces.DeviceCameraControl;
import com.rtmap.game.util.CameraHelper;
import com.rtmap.game.view.ArGLSurfaceView;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;


public class AndroidDeviceCameraController implements DeviceCameraControl,
        Camera.PictureCallback, Camera.AutoFocusCallback {

    private final CameraHelper mCameraHelper;
    private final CameraLoader mCamera;
    private AndroidLauncher androidLauncher;
    private GPUImage mGPUImage;
    private GLSurfaceView mGlSurfaceView;
    private FrameLayout mLayout;
    private GPUImage mGpuImage;
    private ArGLSurfaceView mSurfaceView;

    public AndroidDeviceCameraController(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;

        mCameraHelper = new CameraHelper(androidLauncher);
        mCamera = new CameraLoader();

    }

    @Override
    public synchronized void prepareCamera() {
//        mLayout = new FrameLayout(androidLauncher);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        mLayout.setLayoutParams(params);

        mGlSurfaceView = new GLSurfaceView(androidLauncher);
        mLayout.addView(mGlSurfaceView, 0);

//        mSurfaceView = new ArGLSurfaceView(androidLauncher);
//        mLayout.addView(mSurfaceView, 1);

        androidLauncher.addContentView(mGlSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        mGpuImage = new GPUImage(androidLauncher);
//        mGpuImage.setGLSurfaceView(mSurfaceView);
        mGPUImage = new GPUImage(androidLauncher);
        mGPUImage.setGLSurfaceView(mGlSurfaceView);


    }

    @Override
    public synchronized void startPreview() {
        // ...and start previewing. From now on, the camera keeps pushing
        // preview
        // images to the surface.

        if (mCamera != null)
            mCamera.onResume();
        mGPUImage.setFilter(new GPUImageGrayscaleFilter());
    }

    //    public void g
    @Override
    public synchronized void stopPreview() {
        if (mGlSurfaceView != null) {
            ViewParent parentView = mGlSurfaceView.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(mGlSurfaceView);
//                viewGroup.removeView(mSurfaceView);
            }
            mCamera.onPause();
            mGPUImage = null;
//            mGpuImage = null;
            androidLauncher.restoreFixedSize();
        }
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
        if (mGlSurfaceView != null) {
            return true;
        }
        return false;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
    }

    private class CameraLoader {

        private int mCurrentCameraId = 0;
        private Camera mCameraInstance;

        public void onResume() {
            setUpCamera(mCurrentCameraId);
        }

        public void onPause() {
            releaseCamera();
        }

        public void switchCamera() {
            releaseCamera();
            mCurrentCameraId = (mCurrentCameraId + 1) % mCameraHelper.getNumberOfCameras();
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

            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    androidLauncher, mCurrentCameraId);
            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
        }

        /**
         * A safe way to get an instance of the Camera object.
         */
        private Camera getCameraInstance(final int id) {
            Camera c = null;
            try {
                c = mCameraHelper.openCamera(id);
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
}