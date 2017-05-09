package com.rtmap.game;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.rtmap.game.camera.AndroidDeviceCameraController;

import java.lang.ref.SoftReference;


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

        context = this;
        contexts = new SoftReference<Context>(context);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.a = 8;
        config.r = 8;
        config.g = 8;
        config.b = 8;
        androidDeviceCameraController = new AndroidDeviceCameraController(this);
        asset = new AssetManager();
        initialize(new MyGame(this, androidDeviceCameraController, asset), config);

        //设置日志等级
//        Gdx.app.setLogLevel(LOG_NONE);

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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (androidDeviceCameraController != null) {
            androidDeviceCameraController.stopPreviewAsync();
        }
        super.onPause();

    }

    @Override
    protected void onStop() {
        if (asset != null)
            asset.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (androidDeviceCameraController != null) {
            androidDeviceCameraController.stopPreviewAsync();
        }
        super.onDestroy();
    }

    public static Context getInstance() {
        return contexts.get();
    }
}
