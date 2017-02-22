package com.rtmap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.screen.LoadingScreen;

/**
 * Created by yxy on 2017/2/20.
 */
public class MyGame extends Game {
    private AndroidDeviceCameraController androidDeviceCameraController;
    private AndroidLauncher androidLauncher;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;

    public MyGame(AndroidLauncher androidLauncher, AndroidDeviceCameraController androidDeviceCameraController) {
        this.androidLauncher = androidLauncher;
        this.androidDeviceCameraController = androidDeviceCameraController;
    }

    @Override
    public void create() {
        loadingScreen = new LoadingScreen(this, androidLauncher);
        gameScreen = new GameScreen();

        setScreen(loadingScreen);
    }

    @Override
    public void render() {
        if (androidDeviceCameraController != null) {
            androidDeviceCameraController.prepareCameraAsync();
        }
        if (androidDeviceCameraController != null) {
            if (androidDeviceCameraController.isReady()) {
                androidDeviceCameraController.startPreviewAsync();
            }
        }
        super.render();
    }

    @Override
    public void dispose() {
        if (loadingScreen != null) {
            loadingScreen.dispose();
            loadingScreen = null;
        }
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    public void showGameScreen() {
        setScreen(gameScreen);

        if (loadingScreen != null) {
            // 由于 StartScreen 只有在游戏启动时展示一下, 之后都不需要展示,
            // 所以启动完 GameScreen 后手动调用 StartScreen 的 dispose() 方法销毁开始场景。
            loadingScreen.dispose();

            // 场景销毁后, 场景变量值空, 防止二次调用 dispose() 方法
            loadingScreen = null;
        }
    }
}
