package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.rtmap.game.interfaces.BackOnClickListener;

/**
 * Created by yxy on 2017/2/21.
 */
public class AgainActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;

    public AgainActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        assetManager.load("open_again.png", Texture.class);
        assetManager.finishLoading();

        normal = new TextureRegion((Texture) assetManager.get("open_again.png"));

        setPosition(width / 2 - normal.getRegionWidth() / 2, height * 0.33f);
        setSize(normal.getRegionWidth(), normal.getRegionHeight());
    }

    public void setListener(final AgainOnClickListener againOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (againOnClickListener != null) {
                    againOnClickListener.againClick();
//                    Gdx.app.exit();
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
        batch.draw(normal, width / 2 - normal.getRegionWidth() / 2, height * 0.33f, normal.getRegionWidth(), normal.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public interface AgainOnClickListener{
        void againClick();
    }
}
