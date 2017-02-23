package com.rtmap.game.screen;

import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CatActor;
import com.rtmap.game.actor.CatchActor;
import com.rtmap.game.actor.CoverActor;
import com.rtmap.game.actor.FindActor;
import com.rtmap.game.actor.LoadingActor;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.interfaces.CatchListener;
import com.rtmap.game.interfaces.CatchOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.stage.CatchStage;
import com.rtmap.game.stage.FindStage;
import com.rtmap.game.stage.GameStage;
import com.rtmap.game.stage.LoadingStage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Created by yxy on 2017/2/20.
 */
public class CatchScreen implements Screen {

    private CatActor catActor;
    private float deltaSum;
    private MyGame mGame;
    //    private Texture mainBg;

    private BackActor backActor;
    private BeedActor beedActor;
    private Group group3;
    private Timer timer;

    private CatchStage catchStage;
    private CatchActor catchActor;

    private boolean stop = true;
    private CoverActor coverActor;
    private boolean isFirst = true;

    public CatchScreen(MyGame game) {
        this.mGame = game;
        //捕捉怪兽舞台
        catchStage = new CatchStage(new ScreenViewport());

        group3 = new Group();
        catchActor = new CatchActor(new AssetManager());
        catchActor.setPosition(0, 0);
        catchActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group3.addActor(catchActor);

        backActor = new BackActor(new AssetManager());
        group3.addActor(backActor);

        beedActor = new BeedActor(new AssetManager());
        group3.addActor(beedActor);

//        coverActor = new CoverActor(new AssetManager());
//        group3.addActor(coverActor);

        catActor = new CatActor(new AssetManager());
        group3.addActor(catActor);

        catchStage.addActor(group3);
    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(catchStage);
        if (isFirst) {
            isFirst = false;
            backActor.setListener();
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    mGame.showBeedScreen(CatchScreen.this);
                }
            });
            catActor.setListener(new CatchOnClickListener() {
                @Override
                public void onClick() {
                    catchActor.setIsStop(stop);
                    stop = !stop;
                }
            });
        }
        catchActor.setCatchListener(new CatchListener() {
            @Override
            public void onSuccess() {
                Gdx.app.error("gdx", "onSuccess");
            }

            @Override
            public void onFail() {
                Gdx.app.error("gdx", "onFail");
                if (mGame != null)
                    mGame.showLoadingScreen();
            }

            @Override
            public void onNumberFail(int number) {
                Gdx.app.error("gdx", "onNumberFail");
                if (mGame != null)
                    mGame.showLoadingScreen();
            }
        });
    }

    @Override
    public void render(float delta) {
        if (catchStage == null)
            return;


        Gdx.gl.glClearColor(1, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // 更新舞台逻辑
        catchStage.act();
        // 绘制舞台
        catchStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        initListener();
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
        if (catchStage != null) {
            catchStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
