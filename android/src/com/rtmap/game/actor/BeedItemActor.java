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
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.ScreenUtil;

/**
 * Created by yxy on 2017/2/21.
 */
public class BeedItemActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;

    public BeedItemActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        assetManager.load("open_close.png", Texture.class);
        assetManager.finishLoading();

        normal = new TextureRegion((Texture) assetManager.get("open_close.png"));

//        setPosition(width * 0.93f - normal.getRegionWidth(), height * 0.9f - normal.getRegionHeight());
        setSize(width, 500);
    }

    public void setListener(final BackOnClickListener backOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (backOnClickListener != null) {
                    backOnClickListener.onClick();
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
        LazyBitmapFont.setFontSize(ScreenUtil.dp2px(40), com.badlogic.gdx.graphics.Color.WHITE).draw(batch, "1111111111111", 10, 10, width, Align.left, false);
//        batch.draw(normal, width * 0.93f - normal.getRegionWidth(), height * 0.9f - normal.getRegionHeight(), normal.getRegionWidth(), normal.getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
