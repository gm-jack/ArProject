package com.rtmap.game.screen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageButton;
import android.widget.ListView;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.BeedBackActor;
import com.rtmap.game.actor.BeedItemActor;
import com.rtmap.game.actor.MyBeedActor;
import com.rtmap.game.interfaces.BeedItemOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.scrollpane.BeedScrollPane;
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
    private ScreenViewport screenViewport;
    private Array<BeedItemActor> itemActors = new Array<>();
    private BeedScrollPane beedScrollPane;
    private final Table table;

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

        beedBackActor = new BeedBackActor(new AssetManager(), myBeedActor);
        group.addActor(beedBackActor);

        initData();
        table = new Table();
        table.align(Align.top);
        if (itemActors.size > 0) {
            for (int i = 0; i < itemActors.size; i++) {
                BeedItemActor beedItemActor = itemActors.get(i);
                table.add(beedItemActor).width(Gdx.graphics.getWidth()).height(beedItemActor.getRealHeight());
//                table.add(new Image(new Texture(Gdx.files.internal("beed_item_bg.png")))).width(Gdx.graphics.getWidth()).height(itemActors.get(i).getRealHeight());
                table.row();
            }
        }

        beedScrollPane = new BeedScrollPane(table);
        beedScrollPane.setScrollingDisabled(true, false);//设置是否可上下、左右移动..这里设置了横向可移动、纵向不可移动
        beedScrollPane.setSmoothScrolling(true);
//        beedScrollPane.setFlickScroll(false);
        beedScrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - myBeedActor.getTitleHeight());
//        beedScrollPane.setPosition(Utils.xAxisCenter(getWidth()), Utils.yAxisCenter(getHeight()) - 20);
        beedScrollPane.layout();
        group.addActor(beedScrollPane);

        beedStage.addActor(group);
    }

    private void initData() {
        itemActors.clear();

        itemActors.add(new BeedItemActor(new AssetManager()));
        itemActors.add(new BeedItemActor(new AssetManager()));
        itemActors.add(new BeedItemActor(new AssetManager()));
    }

    @Override
    public void show() {
        if (mGame != null)
            mGame.stopCamera();
    }

    private void initListener() {
        Gdx.input.setInputProcessor(beedStage);
        if (beedBackActor != null)
            beedBackActor.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.error("gdx", "back");
                    mGame.showOldScreen();
                    BeedScreen.this.dispose();
                    super.clicked(event, x, y);
                }
            });
        if (beedScrollPane != null) {
            beedScrollPane.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Gdx.app.error("list", "touchDown");
                    event.stop();
                    return true;
                }
            });
        }
        for (int i = 0; i < itemActors.size; i++) {
            BeedItemActor beedItemActor = itemActors.get(i);
            beedItemActor.setListener(new BeedItemOnClickListener() {
                @Override
                public void onClick(BeedItemActor actor, int item) {
                    Gdx.app.error("list", "onClick   item ==" + item);
                }
            }, i);
        }
    }

    @Override
    public void render(float delta) {
        if (beedStage == null)
            return;

//        Gdx.gl20.glClearColor(0, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // 更新舞台逻辑
        beedStage.act();
        // 绘制舞台
        beedStage.draw();
    }

    @Override
    public void resize(int width, int height) {
//        screenViewport.update(width, height);
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
