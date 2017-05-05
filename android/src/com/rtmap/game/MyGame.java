package com.rtmap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.screen.AimScreen;
import com.rtmap.game.screen.BeedScreen;
import com.rtmap.game.screen.CatchScreen;
import com.rtmap.game.screen.LoadingScreen;
import com.rtmap.game.screen.MainScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class MyGame extends Game {

    private AndroidDeviceCameraController androidDeviceCameraController;
    private AndroidLauncher androidLauncher;
    private List<Screen> screenList;
    private LoadingScreen loadingScreen;
    private Screen oldScreen;
    private CatchScreen catchScreen;

    /**
     * 设置相机模式
     */
    public int normal_Mode = 0;
    public int prepare_Mode = 1;
    public int preview_Mode = 2;

    private int mode = normal_Mode;
    public AssetManager asset;
    private MainScreen mainScreen;
    private AimScreen aimScreen;
    private ScreenViewport mViewport;
    private boolean cameraShow = false;

    public MyGame(AndroidLauncher androidLauncher, AndroidDeviceCameraController androidDeviceCameraController, AssetManager asset) {
        this.androidLauncher = androidLauncher;
        this.androidDeviceCameraController = androidDeviceCameraController;
        this.asset = asset;
        screenList = new ArrayList<>();
    }

    @Override
    public void create() {
        mViewport = new ScreenViewport();
        mainScreen = new MainScreen(this, androidLauncher, mViewport);
        loadingScreen = new LoadingScreen(this, mViewport, androidDeviceCameraController);

        setScreen(mainScreen);
    }

    @Override
    public void render() {
        Gdx.gl20.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!(getScreen() instanceof MainScreen || getScreen() instanceof BeedScreen)) {
            if (mode == normal_Mode) {
                Gdx.app.error("gdx", "normal_Mode");
                if (androidDeviceCameraController != null) {
                    androidDeviceCameraController.prepareCameraAsync(cameraShow);
                    mode = prepare_Mode;
                }
            } else if (mode == prepare_Mode) {
                Gdx.app.error("gdx", "prepare_Mode");
                if (androidDeviceCameraController != null)
                    if (androidDeviceCameraController.isReady()) {
                        androidDeviceCameraController.startPreviewAsync();
                        mode = preview_Mode;
                    }
            }
        }
        super.render();
    }

    @Override
    public void pause() {
        super.pause();
        stopCamera(cameraShow);
    }

    public void stopCamera(boolean cameraShow) {
        this.cameraShow = cameraShow;
        if (androidDeviceCameraController != null) {
            androidDeviceCameraController.stoPreviewAsync();
            mode = prepare_Mode;
        }
    }

    @Override
    public void dispose() {
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);
            if (screen != null) {
                screen.dispose();
                screenList.remove(i);
            }
        }
    }

    public void showCatchScreen() {
        catchScreen = new CatchScreen(this, androidLauncher, mViewport);
        setScreen(catchScreen);
    }

    public void showOldScreen() {
        if (oldScreen == null)
            return;
        if (oldScreen instanceof AimScreen)
            setScreen(aimScreen);
        if (oldScreen instanceof CatchScreen)
            setScreen(catchScreen);
    }

    public void showBeedScreen(Screen oldScreen) {
        this.oldScreen = oldScreen;
        setScreen(new BeedScreen(this, androidLauncher, mViewport));
    }

    public void showLoadingScreen() {
        mode = normal_Mode;
        setScreen(loadingScreen);
    }


    public void showAimScreen(boolean fail) {
        aimScreen = new AimScreen(this, androidLauncher, mViewport);
        setScreen(aimScreen);
        aimScreen.setIsFail(fail);
    }
}
