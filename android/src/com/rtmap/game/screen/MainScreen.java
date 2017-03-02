package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.MainActor;
import com.rtmap.game.actor.StartActor;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.stage.MainStage;

/**
 * Created by yxy on 2017/3/2.
 */
public class MainScreen extends MyScreen {

    private StartActor startActor;
    private MyGame mGame;
    private AndroidLauncher androidLauncher;
    private MainStage mainStage;
    private Group group;
    private MainActor mainActor;

    public MainScreen(MyGame game, AndroidLauncher androidLauncher) {
        this.mGame = game;
        this.androidLauncher = androidLauncher;

        mainStage = new MainStage(new ScreenViewport());

        group = new Group();
        mainActor = new MainActor(new AssetManager());
        mainActor.setPosition(0, 0);
        mainActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(mainActor);

        startActor = new StartActor(new AssetManager());
        group.addActor(startActor);

        mainStage.addActor(group);
    }

    @Override
    public void show() {
        initListener();
    }

    private void initListener() {
        Gdx.input.setInputProcessor(mainStage);
        startActor.setListener(new StartOnClickListener() {
            @Override
            public void onClick() {
                if (mGame != null)
                    mGame.showLoadingScreen();
            }
        });
    }

    @Override
    public void render(float delta) {
        if (mainStage == null)
            return;

        // 更新舞台逻辑
        mainStage.act();
        // 绘制舞台
        mainStage.draw();
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
        if (mainStage != null) {
            mainStage.dispose();
        }
    }
}
