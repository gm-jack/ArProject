package com.rtmap.game.camera;

import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.badlogic.gdx.Gdx;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.interfaces.DeviceCameraControl;
import com.rtmap.game.util.CameraHelper;
import com.rtmap.game.util.DisplayUtil;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.util.CameraInterface;


public class AndroidDeviceCameraController implements DeviceCameraControl,
        Camera.PictureCallback, Camera.AutoFocusCallback {

    private CameraHelper mCameraHelper;
    //    private CameraLoader mCamera;
    private AndroidLauncher androidLauncher;
    private GPUImage mGPUImage;
    private GLSurfaceView mGlSurfaceView;
    private CircularCameraSurfaceView mCircularCameraSurfaceView;
    private FrameLayout.LayoutParams mParams;
    private Point mP;

    public AndroidDeviceCameraController(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
        mCameraHelper = new CameraHelper(androidLauncher);
    }

    @Override
    public synchronized void prepareCamera() {

        CameraInterface.getInstance().doOpenCamera(null);

        FrameLayout layout = new FrameLayout(androidLauncher);
        layout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mCircularCameraSurfaceView = new CircularCameraSurfaceView(androidLauncher);
        mCircularCameraSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        mGlSurfaceView = new GLSurfaceView(androidLauncher);
        layout.addView(mGlSurfaceView);
        layout.addView(mCircularCameraSurfaceView);

        androidLauncher.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mGPUImage = new GPUImage(androidLauncher);
        mGPUImage.setGLSurfaceView(mGlSurfaceView);

//        mCamera = new CameraLoader(androidLauncher, mCameraHelper, mGPUImage);
        initViewParams();
    }

    private void initViewParams() {
        mParams = (FrameLayout.LayoutParams) mCircularCameraSurfaceView.getLayoutParams();
        mP = DisplayUtil.getScreenMetrics(androidLauncher);
        mParams.gravity = Gravity.CENTER;
        mParams.width = 500;
        mParams.height = 500;
        mCircularCameraSurfaceView.setLayoutParams(mParams);
    }

    public void resumeCircleCamera() {
        if (mCircularCameraSurfaceView != null) {
            mCircularCameraSurfaceView.bringToFront();
        }
        if (mGlSurfaceView != null) {
            mGlSurfaceView.onResume();
        }
    }

    public void pauseCircleCamera() {
        if (mCircularCameraSurfaceView != null) {
            mCircularCameraSurfaceView.onPause();
        }
        if (mGlSurfaceView != null) {
            mGlSurfaceView.onPause();
        }
    }

    public void showCenter(boolean show) {
        if (mCircularCameraSurfaceView != null)
            mCircularCameraSurfaceView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void endAnimation() {

        mGPUImage.setFilter(new GPUImageGrayscaleFilter());
        showCenter(false);
    }

    public void animationCenter(final Animation.AnimationListener listener) {
        androidLauncher.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showCenter(true);
                if (mCircularCameraSurfaceView.getVisibility() == View.VISIBLE) {
                    mCircularCameraSurfaceView.startPreview();
                    Gdx.app.error("camera", "animation");
                    float scale = (float) (Math.abs(Math.sqrt((mP.x * mP.x) + (mP.y * mP.y))) / 500f);
                    ScaleAnimation scaleAnimation = new ScaleAnimation(ScaleAnimation.RELATIVE_TO_SELF, scale, ScaleAnimation.RELATIVE_TO_SELF, scale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(2000);
                    scaleAnimation.setFillAfter(true);
                    mCircularCameraSurfaceView.setAnimation(scaleAnimation);
                    scaleAnimation.setAnimationListener(listener);
                    scaleAnimation.start();
                }
            }
        });
    }


    @Override
    public synchronized void startPreview() {

        if (mCircularCameraSurfaceView != null) {
//            CameraInterface.getInstance().doOpenCamera(null);
            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    androidLauncher, 0);
            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(0, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(CameraInterface.getInstance().getCamera(), orientation, flipHorizontal, false);
            mCircularCameraSurfaceView.startPreview();
            showCenter(false);
        }
//        if (mCamera != null)
//            mCamera.onResume();
//        mGPUImage.setFilter(new GPUImageGrayscaleFilter());
    }

    @Override
    public synchronized void stopPreview() {
        if (mGlSurfaceView != null && mCircularCameraSurfaceView != null) {
            ViewParent parentView = mGlSurfaceView.getParent();
            if (parentView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parentView;
                viewGroup.removeView(mGlSurfaceView);
                viewGroup.removeView(mCircularCameraSurfaceView);
            }
            CameraInterface.getInstance().doStopCamera();
//            mCamera.onPause();
            mGPUImage = null;
            androidLauncher.restoreFixedSize();
        }
    }

    public void setCameraParametersForPicture(Camera camera) {
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

        public void switchCamera(int id) {
            releaseCamera();
            setUpCamera(id);
        }

        private void setUpCamera(final int id) {
            mCameraInstance = CameraInterface.getInstance().getCamera();
//            Camera.Parameters parameters = mCameraInstance.getParameters();
//            // TODO adjust by getting supportedPreviewSizes and then choosing
//            // the best one for screen size (best fill screen)
//
////            parameters.setPreviewFormat(ImageFormat.NV21);
////            parameters.setPictureFormat(ImageFormat.NV21);
//            if (parameters.getSupportedFocusModes().contains(
//                    Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//            }
//            mCameraInstance.setParameters(parameters);

            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    androidLauncher, mCurrentCameraId);
            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(mCurrentCameraId, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(mCameraInstance, orientation, flipHorizontal, false);
        }

        public Camera getCameraInstance() {
            return mCameraInstance;
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