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

import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.interfaces.DeviceCameraControl;
import com.rtmap.game.util.CameraHelper;
import com.rtmap.game.util.DisplayUtil;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;


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

//        showCenter(false);
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

    public void animationCenter(Animation.AnimationListener listener) {
        if (mCircularCameraSurfaceView.getVisibility() == View.VISIBLE) {
            float scale = (float) (Math.abs(Math.sqrt((mP.x * mP.x) + (mP.y * mP.y))) / 500f);
            ScaleAnimation scaleAnimation = new ScaleAnimation(ScaleAnimation.RELATIVE_TO_SELF, scale, ScaleAnimation.RELATIVE_TO_SELF, scale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(2000);
            scaleAnimation.setFillAfter(true);
            mCircularCameraSurfaceView.setAnimation(scaleAnimation);
            scaleAnimation.start();
            scaleAnimation.setAnimationListener(listener);
        }
    }

    @Override
    public synchronized void startPreview() {
        if (mCircularCameraSurfaceView != null) {
            int orientation = mCameraHelper.getCameraDisplayOrientation(
                    androidLauncher, 0);
            CameraHelper.CameraInfo2 cameraInfo = new CameraHelper.CameraInfo2();
            mCameraHelper.getCameraInfo(0, cameraInfo);
            boolean flipHorizontal = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
            mGPUImage.setUpCamera(CameraInterface.getInstance().getCamera(), orientation, flipHorizontal, false);
            mCircularCameraSurfaceView.startPreview();
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
}