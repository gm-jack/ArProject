package com.rtmap.game;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.rtmap.game.camera.AndroidDeviceCameraController;

import java.util.Arrays;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AndroidLauncher extends AndroidApplication {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    protected int origWidth;
    protected int origHeight;
    private TextureView mainTexture;
    private FrameLayout flMain;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession cameraCaptureSessions;
    private TextureView.SurfaceTextureListener MySurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[0];
            //指定相机的参数
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            //获取所支持尺寸中最大的那个
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // 检查权限
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            //打开摄像头，在stateCallback回调接口中获取CameraDevice
            manager.openCamera(cameraId, DeviceStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback DeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            //当摄像头打开后回调此方法
            mCameraDevice = camera;
            //开启相机预览
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

        }

        @Override
        public void onError(CameraDevice camera, int error) {

        }
    };

    protected void createCameraPreview() {
        try {
            SurfaceTexture texture = mainTexture.getSurfaceTexture();
            assert texture != null;
            //设置默认缓冲区的尺寸和相机所支持的最大尺寸一样
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            //开启预览所需要的输出Surface
            Surface surface = new Surface(texture);
            //创建一个带有输出Surface的CaptureRequest.Builder
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            //创建预览所需要的CameraCaptureSession
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    //若摄像头已关闭，则退出
                    if (null == mCameraDevice) {
                        return;
                    }
                    // 当CameraCaptureSession已准备好，开启预览
                    cameraCaptureSessions = cameraCaptureSession;
                    //配置预览时使用3A模式(自动曝光、自动对焦、自动白平衡)
                    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    try {
                        //最后显示相机预览界面，captureRequestBuilder.build()用于生成CaptureRequest
                        cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, mBackgroundHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String cameraId;
    private Size imageDimension;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    @Override
    protected void onResume() {
        super.onResume();
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitymain);
//        flMain = (FrameLayout) findViewById(R.id.fl_main);
//        mainTexture = (TextureView) findViewById(R.id.texture_main);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.a = 8;
        config.r = 8;
        config.g = 8;
        config.b = 8;
        AndroidDeviceCameraController androidDeviceCameraController = new AndroidDeviceCameraController(this);
        initialize(new MyGame(this, androidDeviceCameraController), config);

        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            // force alpha channel - I'm not sure we need this as the GL surface
            // is already using alpha channel
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
//        View view = initializeForView(new MyGame(this, androidDeviceCameraController), config);

        // keep the original screen size
//        origWidth = graphics.getWidth();
//        origHeight = graphics.getHeight();
//        flMain.addView(view);
//
//        mainTexture.setSurfaceTextureListener(MySurfaceTextureListener);
    }
    public void post(Runnable r) {
        handler.post(r);
    }

}
