package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by yxy on 2017/2/21.
 */
public class BackActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private TextureRegion press;
    private Batch batch;
    private boolean isDown = false;

    public BackActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        initResources();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    private void initResources() {
        assetManager.load("find_left_normal.png", Texture.class);
        assetManager.load("find_left_press.png", Texture.class);
        assetManager.finishLoading();

        normal = new TextureRegion((Texture) assetManager.get("find_left_normal.png"));
        press = new TextureRegion((Texture) assetManager.get("find_left_press.png"));
    }

    public void setListener() {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDown = false;
                Gdx.app.error("gdx", "back");
                Gdx.app.exit();
            }
        };
        addListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.batch = batch;
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        if (!isDown)
            batch.draw(normal, 0, 0, press.getRegionWidth(), press.getRegionHeight());
        else
            batch.draw(press, 0, 0, press.getRegionWidth(), press.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
