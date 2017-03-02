package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.LoadingActor;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.stage.GameStage;
import com.rtmap.game.stage.LoadingStage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class LoadingScreen extends MyScreen {

    private AndroidDeviceCameraController androidDeviceCameraController;
    private float deltaSum;
    private MyGame mGame;
    //    private Texture mainBg;
    private GameStage loadingStage;

    private LoadingActor loadingActor;
    private Timer timer;

    public LoadingScreen(MyGame game, AndroidDeviceCameraController androidDeviceCameraController) {
        this.mGame = game;
        this.androidDeviceCameraController = androidDeviceCameraController;
        //加载中舞台
        loadingStage = new LoadingStage(new ScreenViewport());

        loadingActor = new LoadingActor(new AssetManager());
        loadingActor.setPosition(0, 0);
        loadingActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loadingStage.addActor(loadingActor);
    }

    @Override
    public void show() {
        deltaSum = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (mGame != null)
                            mGame.showAimScreen(false);
                    }
                });
            }
        }, 3000);
    }


    @Override
    public void render(float delta) {
        if (loadingStage == null)
            return;
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // 更新舞台逻辑
        loadingStage.act();
        // 绘制舞台
        loadingStage.draw();
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

    @Override
    public void dispose() {
        // 场景被销毁时释放资源
        if (loadingStage != null) {
            loadingStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
