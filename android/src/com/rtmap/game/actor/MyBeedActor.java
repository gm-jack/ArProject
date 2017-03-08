package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
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
public class MyBeedActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private List<TextureRegion> beedList = new ArrayList<>();
    private float scale = 1;

    public MyBeedActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        assetManager.load("beed_bg.png", Texture.class);
        assetManager.load("beed_title.png", Texture.class);
        assetManager.finishLoading();

        beedList.add(new TextureRegion((Texture) assetManager.get("beed_bg.png")));
        beedList.add(new TextureRegion((Texture) assetManager.get("beed_title.png")));

        scale = (float) width / (float)beedList.get(1).getRegionWidth();
    }

    public float getTitleHeight() {
        float height = 0;
        if (null != beedList.get(1))
            height = beedList.get(1).getRegionHeight() * scale;
        return height;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(beedList.get(0), 0, 0, width, height);
        batch.draw(beedList.get(1), 0, height - beedList.get(1).getRegionHeight() * scale, width, beedList.get(1).getRegionHeight() * scale);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        for (int i = 0; i < beedList.size(); i++) {
            beedList.get(i).getTexture().dispose();
        }
    }
}
