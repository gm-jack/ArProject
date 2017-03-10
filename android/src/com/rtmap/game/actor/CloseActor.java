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
public class CloseActor extends Actor {
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private TextureRegion normal;
    private boolean isShow = false;

    public CloseActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResources();
    }

    private void initResources() {
        normal = new TextureRegion((Texture) assetManager.get("open_close.png"));

        setPosition(width * 0.93f - normal.getRegionWidth(), height * 0.9f - normal.getRegionHeight());
        setSize(normal.getRegionWidth(), normal.getRegionHeight());
    }


    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
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
        if (assetManager.update()) {
            initResources();
        }
        if (isShow && normal != null)
            batch.draw(normal, width * 0.93f - normal.getRegionWidth(), height * 0.9f - normal.getRegionHeight(), normal.getRegionWidth(), normal.getRegionHeight());

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
