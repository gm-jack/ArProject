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
import com.rtmap.game.interfaces.StartOnClickListener;

import java.util.ArrayList;

/**
 * Created by yxy on 2017/2/21.
 */
public class StartActor extends Actor {
    private MainActor mainActor;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private float regionWidth;
    private float regionHeight;

    public StartActor(AssetManager assetManager, MainActor mainActor) {
        super();
        this.mainActor = mainActor;
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public void setListener(final StartOnClickListener startOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (startOnClickListener != null) {
                    startOnClickListener.onClick();
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
        if (assetManager.update()) {
            initResouces();
        }
        if (assetManager.isLoaded("m_start.png"))
            batch.draw(normal, width / 2 - regionWidth / 2, height * 0.08f, regionWidth, regionHeight);
    }

    private void initResouces() {
        normal = new TextureRegion((Texture) assetManager.get("m_start.png"));
        regionWidth = normal.getRegionWidth() * mainActor.getScale();
        regionHeight = normal.getRegionHeight() * mainActor.getScale();
        setPosition(width / 2 - regionWidth / 2, height * 0.08f);
        setSize(regionWidth, regionHeight);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        if (normal != null)
            normal.getTexture().dispose();
    }
}
