package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.interfaces.CatchOnClickListener;

/**
 * Created by yxy on 2017/2/21.
 */
public class CatActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private TextureRegion press;
    private Batch batch;
    private boolean isDown = false;

    public CatActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        assetManager.load("catch_button_normal.png", Texture.class);
        assetManager.load("catch_button_press.png", Texture.class);
        assetManager.finishLoading();

        normal = new TextureRegion((Texture) assetManager.get("catch_button_normal.png"));
        press = new TextureRegion((Texture) assetManager.get("catch_button_press.png"));

        setPosition(width / 2, height * 0.15f);
        setSize(normal.getRegionWidth(), normal.getRegionHeight());
    }

    public void setListener(final CatchOnClickListener catchOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isDown = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDown = false;
                if (catchOnClickListener != null) {
                    catchOnClickListener.onClick();
                }
            }
        };
        addListener(listener);
    }

    public void removeListener() {
        removeListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        if (!isDown)
            batch.draw(normal, width / 2 - normal.getRegionWidth() / 2, height * 0.15f, normal.getRegionWidth(), normal.getRegionHeight());
        else
            batch.draw(press, width / 2 - press.getRegionWidth() / 2, height * 0.15f, press.getRegionWidth(), press.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
