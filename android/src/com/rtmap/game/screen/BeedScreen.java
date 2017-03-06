package com.rtmap.game.screen;

import android.widget.ImageButton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.BeedBackActor;
import com.rtmap.game.actor.BeedItemActor;
import com.rtmap.game.actor.MyBeedActor;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.stage.BeedStage;
import com.rtmap.game.text.LazyBitmapFont;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class BeedScreen extends MyScreen {
    private MyBeedActor myBeedActor;
    private BeedStage beedStage;
    private MyGame mGame;
    private BeedBackActor beedBackActor;
    private Group group;
    private List<BeedItemActor> actorList;
    private ScreenViewport screenViewport;
    private Array<BeedItemActor> itemActors = new Array<>();

    public BeedScreen(MyGame game, AndroidLauncher androidLauncher) {
        this.mGame = game;
        //瞄准怪兽舞台
        screenViewport = new ScreenViewport();
        beedStage = new BeedStage(screenViewport);

        group = new Group();
        myBeedActor = new MyBeedActor(new AssetManager());
        myBeedActor.setPosition(0, 0);
        myBeedActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(myBeedActor);

        beedBackActor = new BeedBackActor(new AssetManager());
        group.addActor(beedBackActor);

        beedStage.addActor(group);
        itemActors.add(new BeedItemActor(new AssetManager()));
        itemActors.add(new BeedItemActor(new AssetManager()));
        itemActors.add(new BeedItemActor(new AssetManager()));
        itemActors.add(new BeedItemActor(new AssetManager()));

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = new LazyBitmapFont();
        listStyle.selection = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("beed_item_bg.png"))));
        actorList = new List<>(listStyle);
        actorList.setItems(itemActors);
        actorList.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - myBeedActor.getTitleHeight());
        actorList.setPosition(0, 0);
        beedStage.addActor(actorList);
    }

    @Override
    public void show() {
        if (mGame != null)
            mGame.stopCamera();
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
        actorList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.error("list", "selected index=" + actorList.getSelectedIndex());
                Gdx.app.error("list", "setected=" + actorList.getSelected());
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void render(float delta) {
        if (beedStage == null)
            return;

        // 更新舞台逻辑
        beedStage.act();
        // 绘制舞台
        beedStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        screenViewport.update(width, height);
        Gdx.app.error("list", "resize");
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
