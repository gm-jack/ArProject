package com.rtmap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
    //    private FindScreen findScreen;
    private Screen oldScreen;
    private CatchScreen catchScreen;
//    private AimScreen aimScreen;

    /**
     * 设置相机模式
     */
    private int normal_Mode = 0;
    private int prepare_Mode = 1;
    private int preview_Mode = 2;

    private int mode = normal_Mode;
    private AssetManager asset;
    private MainScreen mainScreen;

    public MyGame(AndroidLauncher androidLauncher, AndroidDeviceCameraController androidDeviceCameraController) {
        this.androidLauncher = androidLauncher;
        this.androidDeviceCameraController = androidDeviceCameraController;

        screenList = new ArrayList<>();
    }

    @Override
    public void create() {
        asset = new AssetManager();
        mainScreen = new MainScreen(this, androidLauncher);
        loadingScreen = new LoadingScreen(this, asset);

        setScreen(mainScreen);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (!(getScreen() instanceof MainScreen)) {
            if (mode == normal_Mode) {
                Gdx.app.error("gdx", "normal_Mode");
                if (androidDeviceCameraController != null) {
                    androidDeviceCameraController.prepareCameraAsync();
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
        if (androidDeviceCameraController != null) {
            Gdx.app.error("gdx", "pause");
            androidDeviceCameraController.stopPreviewAsync();
            mode = normal_Mode;
        }
        super.pause();
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

    public void showCatchScreen(boolean isStage, boolean fail) {
        catchScreen = new CatchScreen(this, androidLauncher, isStage, fail);
        setScreen(catchScreen);
    }

    public void showOldScreen() {
        if (oldScreen == null)
            return;
        if(oldScreen instanceof CatchScreen)
        setScreen(oldScreen);
    }

    public void showBeedScreen(Screen oldScreen) {
        this.oldScreen = oldScreen;
        setScreen(new BeedScreen(this, androidLauncher));
    }

    public void showLoadingScreen() {
        setScreen(loadingScreen);
    }
}
