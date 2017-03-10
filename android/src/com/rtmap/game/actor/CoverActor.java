package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.rtmap.game.interfaces.CatchOnClickListener;

/**
 * Created by yxy on 2017/2/21.
 */
public class CoverActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private TextureRegion normal;
    private Batch batch;
    private boolean isFirst = false;
    private TextureRegion tip;
    private TextureRegion catchs;

    public CoverActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public void setIsFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    private void initResources() {

        normal = new TextureRegion((Texture) assetManager.get("catch_cover.png"));
        tip = new TextureRegion((Texture) assetManager.get("find_tip.png"));
        catchs = new TextureRegion((Texture) assetManager.get("catch_catch.png"));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        if (assetManager.update()) {
            initResources();
        }
        if (isFirst&&normal!=null&&tip!=null&&catchs!=null) {
            batch.draw(normal, 0, 0, width, height);
            batch.draw(tip, width / 2 - tip.getRegionWidth() / 2, height / 2 - tip.getRegionHeight() / 2, tip.getRegionWidth(), tip.getRegionHeight());
            batch.draw(catchs, width / 2 - catchs.getRegionWidth() / 2, height / 2 - tip.getRegionHeight() / 2 + tip.getRegionHeight() / 5, catchs.getRegionWidth(), catchs.getRegionHeight());
        }
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
        if (tip != null)
            tip.getTexture().dispose();
        if (catchs != null)
            catchs.getTexture().dispose();
    }
}
