package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Interpolation;
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

    private float deltaSum;
    private MyGame mGame;
    private GameStage loadingStage;
    private LoadingActor loadingActor;
    private Timer timer;
    private AssetManager asset;
    private boolean isTimerOk = false;

    public LoadingScreen(MyGame game, AssetManager assetManager) {
        this.mGame = game;
        this.asset = assetManager;
        initAssets();
    }

    public void initAssets() {
        asset.load("main_bg.png", Texture.class);
        asset.load("loading_center.png", Texture.class);
        asset.load("loading_in.png", Texture.class);
        asset.load("loading_out.png", Texture.class);
        asset.load("loading_rotate.png", Texture.class);
        asset.load("loading_tip.png", Texture.class);
        asset.load("loading_wait.png", Texture.class);
        asset.load("data/ship.g3db", Model.class);
        asset.finishLoading();
    }

    @Override
    public void show() {
        isTimerOk = false;
        loadingStage = new LoadingStage(new ScreenViewport());
//
        loadingActor = new LoadingActor(asset);
        loadingActor.setPosition(0, 0);
        loadingActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loadingStage.addActor(loadingActor);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        isTimerOk = true;
                    }

                });
            }
        }, 3000);
    }

    private float percent;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (asset.update() && isTimerOk) {
            mGame.showAimScreen(false);
            return;
        }
        percent = Interpolation.linear.apply(percent, asset.getProgress(), 0.1f);
        Gdx.app.log("percent", "percent---->" + percent);
        // 更新舞台逻辑
        loadingStage.act();
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
        asset.unload("main_bg.png");
        asset.unload("loading_center.png");
        asset.unload("loading_in.png");
        asset.unload("loading_out.png");
        asset.unload("loading_rotate.png");
        asset.unload("loading_tip.png");
        asset.unload("loading_wait.png");
        if (timer != null) {
            timer.cancel();
        }
    }
}
