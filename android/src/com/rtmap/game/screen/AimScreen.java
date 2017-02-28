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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CatchActor;
import com.rtmap.game.actor.FindActor;
import com.rtmap.game.actor.LoadingActor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.BackOnClickListener;
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
public class AimScreen extends MyScreen {

    private MyGame mGame;
    //    private Texture mainBg;

    private BackActor backActor;
    private BeedActor beedActor;
    private Group group2;
    private Timer timer;
    private AimStage aimStage;
    private AimActor aimActor;
    private boolean isFirst = true;


    public AimScreen(MyGame game, AndroidLauncher androidLauncher) {
        this.mGame = game;

        //瞄准怪兽舞台
        aimStage = new AimStage(new ScreenViewport());

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
        initListener();
    }

    private void initListener() {
        Gdx.input.setInputProcessor(aimStage);
        if (isFirst) {
            isFirst = false;
            backActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    Gdx.app.error("gdx", "退出");
                    mGame.showCatchScreen();
                }
            });
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    mGame.showBeedScreen(AimScreen.this);
                }
            });
            aimActor.setAimListener(new AimListener() {
                @Override
                public void aimSuccess() {
                    if (mGame != null)
                        mGame.showCatchScreen();
                }

                @Override
                public void aimFail() {
                    if (mGame != null)
                        mGame.showScreen();
                }
            });
        }
    }

    @Override
    public void render(float delta) {
        if (aimStage == null)
            return;


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // 更新舞台逻辑
        aimStage.act();
        // 绘制舞台
        aimStage.draw();
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
        if (aimStage != null) {
            aimStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
