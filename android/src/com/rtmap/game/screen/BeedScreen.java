package com.rtmap.game.screen;

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
import com.rtmap.game.actor.BeedBackActor;
import com.rtmap.game.actor.MyBeedActor;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.stage.BeedStage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class BeedScreen implements Screen {
    private MyBeedActor myBeedActor;
    private BeedStage beedStage;
    private MyGame mGame;
    private final BeedBackActor beedBackActor;
    private final Group group;
    //    private Texture mainBg;


    public BeedScreen(MyGame game) {
        this.mGame = game;
//        mainBg = new Texture(Gdx.files.internal("main_bg.png"));
        //瞄准怪兽舞台
        beedStage = new BeedStage(new ScreenViewport());

        group = new Group();
        myBeedActor = new MyBeedActor(new AssetManager());
        myBeedActor.setPosition(0, 0);
        myBeedActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(myBeedActor);

        beedBackActor = new BeedBackActor(new AssetManager());
        group.addActor(beedBackActor);

        beedStage.addActor(group);

    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(beedStage);
        beedBackActor.setListener(new BeedOnClickListener() {
            @Override
            public void onClick() {
                Gdx.app.error("gdx", "back");
                mGame.showOldScreen();
                BeedScreen.this.dispose();
            }
        });
    }

    @Override
    public void render(float delta) {
        if (beedStage == null)
            return;


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // 更新舞台逻辑
        beedStage.act();
        // 绘制舞台
        beedStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.error("gdx", "resize");
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
        if (beedStage != null) {
            beedStage.dispose();
        }
    }
}
