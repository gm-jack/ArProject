package com.rtmap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.screen.AimScreen;
import com.rtmap.game.screen.BeedScreen;
import com.rtmap.game.screen.CatchScreen;
import com.rtmap.game.screen.FindScreen;
import com.rtmap.game.screen.LoadingScreen;

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
    private FindScreen findScreen;
    private Screen oldScreen;
    private CatchScreen catchScreen;
    private AimScreen aimScreen;

    /**
     * 设置相机模式
     */
    private int normal_Mode = 0;
    private int prepare_Mode = 1;
    private int preview_Mode = 2;

    private int mode = normal_Mode;

    public MyGame(AndroidLauncher androidLauncher, AndroidDeviceCameraController androidDeviceCameraController) {
        this.androidLauncher = androidLauncher;
        this.androidDeviceCameraController = androidDeviceCameraController;

        screenList = new ArrayList<>();
    }

    @Override
    public void create() {
        loadingScreen = new LoadingScreen(this, androidDeviceCameraController);
        findScreen = new FindScreen(this, androidLauncher);

        setScreen(loadingScreen);
    }

    @Override
    public void render() {
        Gdx.gl20.glHint(GL20.GL_GENERATE_MIPMAP_HINT, GL20.GL_NICEST);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        Gdx.app.error("gdx", "render");
        if (mode == normal_Mode) {
            Gdx.app.error("gdx","normal_Mode");
            if (androidDeviceCameraController != null) {
                androidDeviceCameraController.prepareCameraAsync();
                mode = prepare_Mode;
            }

        } else if (mode == prepare_Mode) {
            Gdx.app.error("gdx","prepare_Mode");
            if (androidDeviceCameraController != null)
                if (androidDeviceCameraController.isReady()) {
                    androidDeviceCameraController.startPreviewAsync();
                    mode = preview_Mode;
                }
        }
        super.render();
    }

    @Override
    public void pause() {
        if (androidDeviceCameraController != null) {
            Gdx.app.error("gdx","pause");
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

    public void showScreen() {
        setScreen(findScreen);

//        if (loadingScreen != null) {
//            Gdx.app.error("gdx", "2222222222222222");
//            // 由于 StartScreen 只有在游戏启动时展示一下, 之后都不需要展示,
//            // 所以启动完 GameScreen 后手动调用 StartScreen 的 dispose() 方法销毁开始场景。
//            loadingScreen.dispose();
//
//            // 场景销毁后, 场景变量值空, 防止二次调用 dispose() 方法
//            loadingScreen = null;
//        }
    }

//    public Screen getOldScreen() {
//        return oldScreen;
//    }

    public void showAimScreen() {
        aimScreen = new AimScreen(this, androidLauncher);
        setScreen(aimScreen);
    }

    public void showCatchScreen() {
        catchScreen = new CatchScreen(this, androidLauncher);
        setScreen(catchScreen);
    }

    public void showOldScreen() {
        if (oldScreen == null)
            return;
        if (oldScreen instanceof FindScreen) {
            setScreen(findScreen);
        } else if (oldScreen instanceof AimScreen) {
            setScreen(aimScreen);
        } else if (oldScreen instanceof CatchScreen) {
            setScreen(catchScreen);
        }
    }

    public void showBeedScreen(Screen oldScreen) {
        this.oldScreen = oldScreen;
        setScreen(new BeedScreen(this, androidLauncher));
    }

    public void showLoadingScreen() {
        setScreen(loadingScreen);
    }
}
