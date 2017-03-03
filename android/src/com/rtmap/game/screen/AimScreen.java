package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MagicCamera;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.util.SPUtil;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class AimScreen extends MyScreen {
    private AndroidLauncher androidLauncher;
    private MyGame mGame;

    private BackActor backActor;
    private BeedActor beedActor;
    private boolean fail;
    private Group group2;
    private Timer timer;
    private AimStage aimStage;
    private AimActor aimActor;
    private boolean isFirst = true;
    private boolean isAim = false;

    public AimScreen(MyGame game, AndroidLauncher androidLauncher) {
        super();
        this.mGame = game;
        this.androidLauncher = androidLauncher;

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

    }

    private void initListener() {
        Gdx.input.setInputProcessor(aimStage);
        if (isFirst) {
            isFirst = false;
            backActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    Gdx.app.error("gdx", "退出");
                    if (mGame != null)
                        mGame.showCatchScreen();
                }
            });
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    if (mGame != null)
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
                    aimActor.setIsFind(false);
                }
            });
        }
    }

    @Override
    public void addNumber() {
        if (aimActor != null)
            aimActor.addNumber();
    }

    @Override
    public void subNumber() {
        if (aimActor != null)
            aimActor.subNumber();
    }

    @Override
    public void render(float delta) {
        if (aimStage == null)
            return;
        super.render(delta);
        // 更新舞台逻辑
        aimStage.act();
        // 绘制舞台
        aimStage.draw();
    }

    public void setIsFail(boolean fail) {
        this.fail = fail;
        if (aimActor != null)
            aimActor.setIsFail(fail);
    }

    @Override
    public void resize(int width, int height) {
        setStopCamera(false);
        setStopRerder(false);
        isAim = (boolean) SPUtil.get(androidLauncher, "first_find", true);
        if (isAim) {
            aimActor.setIsTip(true);
            SPUtil.put(androidLauncher, "first_find", false);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    aimActor.setIsTip(false);
                }
            }, 1000);
        }
        initListener();
        super.resize(width, height);
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
