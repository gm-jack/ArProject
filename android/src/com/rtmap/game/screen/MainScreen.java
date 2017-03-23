package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.MainActor;
import com.rtmap.game.actor.StartActor;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.stage.MainStage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

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
    private final AssetManager assetManager;

    public MainScreen(MyGame game, AndroidLauncher androidLauncher) {
        this.mGame = game;
        this.androidLauncher = androidLauncher;

        mainStage = new MainStage(new ScreenViewport());
        assetManager = new AssetManager();
        initResources();

        group = new Group();
        mainActor = new MainActor(assetManager);
        mainActor.setPosition(0, 0);
        mainActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(mainActor);

        startActor = new StartActor(assetManager, mainActor);
        group.addActor(startActor);

        mainStage.addActor(group);
    }

    public void initResources() {
        assetManager.load("m_bg.png", Texture.class);
        assetManager.load("m_rule.png", Texture.class);
        assetManager.load("m_start.png", Texture.class);
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
        assetManager.unload("m_bg.png");
        assetManager.unload("m_rule.png");
        assetManager.unload("m_start.png");
        // 场景被销毁时释放资源
        if (mainStage != null) {
            mainStage.dispose();
        }
    }
}
