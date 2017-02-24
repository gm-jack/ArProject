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
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CatchActor;
import com.rtmap.game.actor.FindActor;
import com.rtmap.game.actor.LoadingActor;
import com.rtmap.game.interfaces.BeedOnClickListener;
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
public class FindScreen implements Screen {

    private float deltaSum;
    private MyGame mGame;
    //    private Texture mainBg;
    private FindStage findStage;

    private final FindActor findActor;
    private BackActor backActor;
    private BeedActor beedActor;
    private Group group;
    private Timer timer;
    private boolean isFirst = true;

    public FindScreen(MyGame game, AndroidLauncher androidLauncher) {
        this.mGame = game;
//        mainBg = new Texture(Gdx.files.internal("main_bg.png"));

        //发现怪兽舞台
        findStage = new FindStage(new ScreenViewport());

        group = new Group();
        findActor = new FindActor(new AssetManager());
        findActor.setPosition(0, 0);
        findActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(findActor);

        backActor = new BackActor(new AssetManager());
        group.addActor(backActor);

        beedActor = new BeedActor(new AssetManager());
        group.addActor(beedActor);
        findStage.addActor(group);
    }

    @Override
    public void show() {
        initListener();
        deltaSum = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.app.error("gdx", "11111111111");
                        mGame.showAimScreen();

                    }
                });
            }
        }, 1000);
    }

    private void initListener() {
        Gdx.input.setInputProcessor(findStage);
        if (isFirst) {
            isFirst = false;
            backActor.setListener();
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    mGame.showBeedScreen(FindScreen.this);
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        if (findStage == null)
            return;


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // 更新舞台逻辑
        findStage.act();
        // 绘制舞台
        findStage.draw();
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
        if (findStage != null) {
            findStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
