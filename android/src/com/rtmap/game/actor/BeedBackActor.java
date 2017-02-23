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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/21.
 */
public class BeedBackActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private List<TextureRegion> beedList = new ArrayList<>();
    private Batch batch;
    private int regionHeight;

    public BeedBackActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        assetManager.load("beed_back.png", Texture.class);
        assetManager.load("beed_title.png", Texture.class);
        assetManager.finishLoading();

        beedList.add(new TextureRegion((Texture) assetManager.get("beed_back.png")));
        beedList.add(new TextureRegion((Texture) assetManager.get("beed_title.png")));

        regionHeight = beedList.get(1).getRegionHeight();
        setPosition(20, height - regionHeight * 0.8f);
        setSize(beedList.get(0).getRegionWidth(), beedList.get(0).getRegionHeight());
    }

    public void setListener(final BeedOnClickListener beedOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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
        batch.draw(beedList.get(0), 20, height - regionHeight * 0.8f, beedList.get(0).getRegionWidth(), beedList.get(0).getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
