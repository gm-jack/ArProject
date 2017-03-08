package com.rtmap.game.actor;

import android.graphics.Color;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedItemOnClickListener;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/21.
 */
public class BeedItemActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private List<TextureRegion> normal = new ArrayList<>();
    private float scale;
    private float realHeight;
    private boolean isUse = false;
    private int position;

    public BeedItemActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        assetManager.load("beed_item_bg.png", Texture.class);
        assetManager.load("beed_item_nouse.png", Texture.class);
        assetManager.load("beed_item_use.png", Texture.class);
        assetManager.load("beed_item_line.png", Texture.class);
        assetManager.finishLoading();

        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_bg.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_nouse.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_use.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_line.png")));

        scale = (float) width / (float) normal.get(0).getRegionWidth();
        realHeight = normal.get(0).getRegionHeight() * scale;
    }

    public void setListener(final BeedItemOnClickListener beedItemOnClickListener, int item) {
        this.position = item;
        listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (beedItemOnClickListener != null) {
                    beedItemOnClickListener.onClick(BeedItemActor.this, position);
                }
            }
        };
        addListener(listener);
    }

    public void setIsUse(boolean isUse) {
        this.isUse = isUse;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        batch.draw(normal.get(0), 0, getY(), getOriginX(), getOriginY(), width, realHeight, getScaleX(), getScaleY(), getRotation());
        if (isUse)
            batch.draw(normal.get(2), width - normal.get(2).getRegionWidth() - 100, getY() + realHeight * 0.3f / 2 - normal.get(2).getRegionHeight() / 2, getOriginX(), getOriginY(), normal.get(2).getRegionWidth(), normal.get(2).getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        else
            batch.draw(normal.get(1), width - normal.get(1).getRegionWidth() - 100, getY() + realHeight * 0.3f / 2 - normal.get(1).getRegionHeight() / 2, getOriginX(), getOriginY(), normal.get(1).getRegionWidth(), normal.get(1).getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        batch.draw(normal.get(3), 0, getY() + realHeight * 0.3f, getOriginX(), getOriginY(), width, normal.get(3).getRegionHeight(), getScaleX(), getScaleY(), getRotation());

        LazyBitmapFont.setFontSize(ScreenUtil.dp2px(13), com.badlogic.gdx.graphics.Color.WHITE).draw(batch, "有效期限: 2016.09.30-2017.06.30", width * 0.13f, getY() + realHeight * 0.3f / 2 + ScreenUtil.dp2px(13 ) / 2, width, Align.left, false);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public float getRealHeight() {
        return realHeight;
    }
}
