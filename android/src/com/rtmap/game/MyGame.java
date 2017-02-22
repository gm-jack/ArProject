package com.rtmap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.screen.AimScreen;
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
    private AimScreen aimScreen;
    private CatchScreen catchScreen;

    public MyGame(AndroidLauncher androidLauncher, AndroidDeviceCameraController androidDeviceCameraController) {
        this.androidLauncher = androidLauncher;
        this.androidDeviceCameraController = androidDeviceCameraController;

        screenList = new ArrayList<>();
    }

    @Override
    public void create() {
        loadingScreen = new LoadingScreen(this,androidDeviceCameraController);
        findScreen = new FindScreen(this);
        aimScreen = new AimScreen(this);
        catchScreen = new CatchScreen(this);

        setScreen(loadingScreen);
    }

    @Override
    public void render() {
        super.render();
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
        Gdx.app.error("gdx", "11111111111");
        setScreen(findScreen);

        if (loadingScreen != null) {
            Gdx.app.error("gdx", "2222222222222222");
            // 由于 StartScreen 只有在游戏启动时展示一下, 之后都不需要展示,
            // 所以启动完 GameScreen 后手动调用 StartScreen 的 dispose() 方法销毁开始场景。
            loadingScreen.dispose();

            // 场景销毁后, 场景变量值空, 防止二次调用 dispose() 方法
            loadingScreen = null;
        }
    }

    public void showAimScreen() {
        setScreen(aimScreen);
    }

    public void showCatchScreen() {
        setScreen(catchScreen);
    }
}
