package jp.co.cyberagent.android.gpuimage.util;

import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraInterface {
    private static final String TAG = "CameraInterface";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;
    private final ExecutorService mService;
    private boolean isOpen = false;

    public interface CamOpenOverCallback {
        public void cameraHasOpened();
    }

    private CameraInterface() {
        mService = Executors.newFixedThreadPool(3);
    }

    public ExecutorService getService() {
        return mService;
    }

    public Camera getCamera() {
        return mCamera;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    public synchronized void doOpenCamera(final CamOpenOverCallback callback) {
        if (mCamera == null && !isOpen) {
            mCamera = Camera.open();
            if (callback != null) {
                callback.cameraHasOpened();
            }
            isOpen = true;
        }
    }

    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        if (mCamera != null) {
            initCamera(previewRate);
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void doStartPreview(SurfaceTexture surface, float previewRate) {
        if (mCamera != null) {
            initCamera(previewRate);
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            isOpen = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    public boolean isPreviewing() {
        return isPreviewing;
    }


    private void initCamera(float previewRate) {
        mParams = mCamera.getParameters();
        mParams.setPictureFormat(PixelFormat.JPEG);
//            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
//                    mParams.getSupportedPictureSizes(),previewRate, 800);
//            mParams.setPictureSize(pictureSize.width, pictureSize.height);
//        Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
//                mParams.getSupportedPreviewSizes(), previewRate, 800);
//        mParams.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setDisplayOrientation(90);
        List<String> focusModes = mParams.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        mCamera.setParameters(mParams);
        isPreviewing = true;
        mPreviwRate = previewRate;
        mParams = mCamera.getParameters();
    }
}
