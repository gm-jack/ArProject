package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CustomActor;
import com.rtmap.game.actor.FindActor;
import com.rtmap.game.actor.LoadingActor;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.stage.FindStage;
import com.rtmap.game.stage.GameStage;
import com.rtmap.game.stage.LoadingStage;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class LoadingScreen implements Screen {

    private float deltaSum;
    private MyGame mGame;
    //    private Texture mainBg;
    private GameStage baseStage;
    private GameStage findStage;
    private GameStage loadingStage;

    private LoadingActor loadingActor;
    private final FindActor findActor;
    private BackActor backActor;
    private BeedActor beedActor;
    private Group group;
    private Timer timer;

    public LoadingScreen(MyGame game) {
        this.mGame = game;

//        mainBg = new Texture(Gdx.files.internal("main_bg.png"));

        loadingStage = new LoadingStage(new ScreenViewport());
        findStage = new FindStage(new ScreenViewport());

        loadingActor = new LoadingActor(new AssetManager());
        loadingActor.setPosition(0, 0);
        loadingActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loadingStage.addActor(loadingActor);

        group = new Group();

        findActor = new FindActor(new AssetManager());
        findActor.setPosition(0, 0);
        findActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(findActor);

        backActor = new BackActor(new AssetManager());
        backActor.setPosition(0, 0);
        backActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        group.addActor(backActor);

        beedActor = new BeedActor(new AssetManager());
        beedActor.setPosition(0, 0);
        beedActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        group.addActor(beedActor);
        findStage.addActor(group);
    }

    @Override
    public void show() {
        baseStage = loadingStage;
        initListener();
        deltaSum = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (baseStage != null)
                    baseStage.clear();
                baseStage = findStage;
                initListener();
            }
        }, 3000);
    }

    private void initListener() {
        Gdx.input.setInputProcessor(baseStage);
        if(baseStage instanceof LoadingStage){

        }else if(baseStage instanceof FindStage){
            backActor.setListener();
//            beedActor.setListener(new BeedOnClickListener() {
//                @Override
//                public void onClick() {
//                    //打开背包Stage
//                    Gdx.app.error("gdx", "打开背包");
//                }
//            });
        }
    }

    @Override
    public void render(float delta) {
        if (baseStage == null)
            return;
        deltaSum += delta;
//        if (deltaSum > 3f) {
        // 开始场景展示时间超过 3 秒, 通知 MainGame 切换场景（启动主游戏界面）
//            if (mGame != null) {
//                mGame.showGameScreen();
//                return;
//            }

//        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // 更新舞台逻辑
        baseStage.act();
        // 绘制舞台
        baseStage.draw();
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
        if (baseStage != null) {
            baseStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
//        if (mainBg != null) {
//            mainBg.dispose();
//        }
    }
}
