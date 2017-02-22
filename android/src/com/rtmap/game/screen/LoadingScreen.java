package com.rtmap.game.screen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CustomActor;
import com.rtmap.game.actor.FindActor;
import com.rtmap.game.actor.LoadingActor;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.stage.FindStage;
import com.rtmap.game.stage.GameStage;
import com.rtmap.game.stage.LoadingStage;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Created by yxy on 2017/2/20.
 */
public class LoadingScreen implements Screen {


    private AndroidLauncher context;
    private float deltaSum;
    private MyGame mGame;
    //    private Texture mainBg;
    private GameStage baseStage;
    private GameStage findStage;
    private GameStage loadingStage;

    private LoadingActor loadingActor;
    private final FindActor findActor;
    private BackActor backActor;
    private BeedActor beedActor;
    private Group group;
    private Timer timer;
    /**
     * camera相关
     */
    private CameraManager mCameraManager;
    private Handler mCameraHandle;
    private Semaphore mCameraLocker;
    private CameraDevice mCameraDevice;
    private HandlerThread mCameraThread;
    private final AimStage aimStage;
    private final AimActor aimActor;
    private Group group1;
    private Group group2;

    public LoadingScreen(MyGame game, AndroidLauncher context) {
        this.mGame = game;
        this.context = context;
//        mainBg = new Texture(Gdx.files.internal("main_bg.png"));

        //加载中舞台
        loadingStage = new LoadingStage(new ScreenViewport());
        //发现怪兽舞台
        findStage = new FindStage(new ScreenViewport());
        //瞄准怪兽舞台
        aimStage = new AimStage(new ScreenViewport());

        loadingActor = new LoadingActor(new AssetManager());
        loadingActor.setPosition(0, 0);
        loadingActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loadingStage.addActor(loadingActor);

        group1 = new Group();

        findActor = new FindActor(new AssetManager());
        findActor.setPosition(0, 0);
        findActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group1.addActor(findActor);

        backActor = new BackActor(new AssetManager());
        group1.addActor(backActor);

        beedActor = new BeedActor(new AssetManager());
        group1.addActor(beedActor);
        findStage.addActor(group1);

        group2 = new Group();
        aimActor = new AimActor(new AssetManager());
        aimActor.setPosition(0, 0);
        aimActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group2.addActor(aimActor);

        backActor = new BackActor(new AssetManager());
        group2.addActor(backActor);

        beedActor = new BeedActor(new AssetManager());
        group2.addActor(beedActor);
        aimStage.addActor(group2);
    }

    @Override
    public void show() {
        baseStage = loadingStage;
        initListener();
        deltaSum = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (baseStage != null)
                    baseStage.clear();
                baseStage = aimStage;
                initListener();
            }
        }, 3000);
    }

    private void initListener() {
        Gdx.input.setInputProcessor(baseStage);
        if (baseStage instanceof LoadingStage) {

        } else if (baseStage instanceof FindStage) {
            backActor.setListener();
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        if (baseStage == null)
            return;


        Gdx.gl.glClearColor(1, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // 更新舞台逻辑
        baseStage.act();
        // 绘制舞台
        baseStage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public void stopCamera() {
//        if (androidDeviceCameraController != null) {
//            androidDeviceCameraController.stopPreviewAsync();
//        }
    }

    @Override
    public void dispose() {
        // 场景被销毁时释放资源
        if (baseStage != null) {
            baseStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
        stopCamera();
//        if (mainBg != null) {
//            mainBg.dispose();
//        }
    }
}
