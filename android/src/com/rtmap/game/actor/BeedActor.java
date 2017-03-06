package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.rtmap.game.interfaces.BeedOnClickListener;

/**
 * Created by yxy on 2017/2/21.
 */
public class BeedActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private TextureRegion press;
    private boolean isDown = false;

    public BeedActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        assetManager.load("find_right_normal.png", Texture.class);
        assetManager.load("find_right_press.png", Texture.class);
        assetManager.finishLoading();

        normal = new TextureRegion((Texture) assetManager.get("find_right_normal.png"));
        press = new TextureRegion((Texture) assetManager.get("find_right_press.png"));

        setPosition(width - normal.getRegionWidth(), 0);
        setSize(normal.getRegionWidth(), normal.getRegionHeight());
    }

    public void setListener(final BeedOnClickListener beedOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDown = false;
                if (beedOnClickListener != null) {
                    beedOnClickListener.onClick();
                }
            }
        };
        addListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        if (!isDown)
            batch.draw(press, width - press.getRegionWidth(), 0, press.getRegionWidth(), press.getRegionHeight());
        else
            batch.draw(normal, width - normal.getRegionWidth(), 0, normal.getRegionWidth(), normal.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
