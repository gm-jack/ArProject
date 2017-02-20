package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.CustomActor;
import com.rtmap.game.actor.LoadingActor;

/**
 * Created by yxy on 2017/2/20.
 */
public class LoadingScreen implements Screen {

    private MyGame mGame;
//    private Texture mainBg;
    private Stage stage;
    private LoadingActor ac_mainBg;
    private float deltaSum;

    public LoadingScreen(MyGame game) {
        this.mGame = game;

//        mainBg = new Texture(Gdx.files.internal("main_bg.png"));

        stage = new Stage(new ScreenViewport());

        ac_mainBg = new LoadingActor(new AssetManager());
        ac_mainBg.setPosition(0, 0);
        ac_mainBg.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(ac_mainBg);
    }

    @Override
    public void show() {
        deltaSum = 0;
    }

    @Override
    public void render(float delta) {
        deltaSum += delta;
        if (deltaSum > 3f) {
            // 开始场景展示时间超过 3 秒, 通知 MainGame 切换场景（启动主游戏界面）
            if (mGame != null) {
//                mGame.showGameScreen();
                return;
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 更新舞台逻辑
        stage.act();
        // 绘制舞台
        stage.draw();
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
        if (stage != null) {
            stage.dispose();
        }
//        if (mainBg != null) {
//            mainBg.dispose();
//        }
    }
}
