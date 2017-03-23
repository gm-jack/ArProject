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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.util.ParticleGame;

import java.lang.ref.SoftReference;
import java.util.Arrays;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AndroidLauncher extends AndroidApplication {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    protected int origWidth;
    protected int origHeight;
    private AndroidDeviceCameraController androidDeviceCameraController;

    private static AndroidLauncher context;
    private static SoftReference<Context> contexts;
    private AssetManager asset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activitymain);
//        flMain = (FrameLayout) findViewById(R.id.fl_main);
//        mainTexture = (TextureView) findViewById(R.id.texture_main);
        context = this;
        contexts = new SoftReference<Context>(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
        }
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.a = 8;
        config.r = 8;
        config.g = 8;
        config.b = 8;
        androidDeviceCameraController = new AndroidDeviceCameraController(this);
        asset = new AssetManager();
        initialize(new MyGame(this, androidDeviceCameraController,asset), config);
//        initialize(new ParticleGame(), config);
//        mainTexture.setSurfaceTextureListener(MySurfaceTextureListener);
//        flMain.addView(view);
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            // force alpha channel - I'm not sure we need this as the GL surface is already using alpha channel
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        }
        // we don't want the screen to turn off during the long image saving process
        graphics.getView().setKeepScreenOn(true);
        // keep the original screen size
        origWidth = graphics.getWidth();
        origHeight = graphics.getHeight();
    }

    public void post(Runnable r) {
        handler.post(r);
    }

    public void setFixedSize(int width, int height) {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.getHolder().setFixedSize(width, height);
        }
    }

    public void restoreFixedSize() {
        if (graphics.getView() instanceof SurfaceView) {
            SurfaceView glView = (SurfaceView) graphics.getView();
            glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            glView.getHolder().setFixedSize(origWidth, origHeight);
        }
    }

    @Override
    protected void onDestroy() {
        if (androidDeviceCameraController != null) {
            Gdx.app.error("gdx", "onDestroy");
            androidDeviceCameraController.stopPreviewAsync();
        }
        super.onDestroy();
    }

    public static Context getInstance() {
        return contexts.get();
    }
}
