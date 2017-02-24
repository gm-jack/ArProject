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
    private TextureRegion openNormal;
    private TextureRegion openPress;
    //判断是否按下
    private boolean isDown = false;
    //判断按钮当前状态
    private boolean isCatch = true;

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
        assetManager.load("success_open_normal.png", Texture.class);
        assetManager.load("success_open_press.png", Texture.class);
        assetManager.finishLoading();

        normal = new TextureRegion((Texture) assetManager.get("catch_button_normal.png"));
        press = new TextureRegion((Texture) assetManager.get("catch_button_press.png"));
        openNormal = new TextureRegion((Texture) assetManager.get("success_open_normal.png"));
        openPress = new TextureRegion((Texture) assetManager.get("success_open_press.png"));

        setPosition(width / 2, height * 0.15f);
        setSize(normal.getRegionWidth(), normal.getRegionHeight());
    }

    /**
     * true:捕捉按钮
     * false:打开按钮
     *
     * @param isCatch
     */
    public void setIsCatch(boolean isCatch) {
        this.isCatch = isCatch;
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
                    if (isCatch)
                        catchOnClickListener.onCatchClick();
                    else
                        catchOnClickListener.onSuccessClick();
                    removeListener();
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
        if (!isDown) {
            if (isCatch)
                batch.draw(press, width / 2 - press.getRegionWidth() / 2, height * 0.15f, press.getRegionWidth(), press.getRegionHeight());
            else
                batch.draw(openNormal, width / 2 - press.getRegionWidth() / 2, height * 0.15f, press.getRegionWidth(), press.getRegionHeight());
        } else {
            if (isCatch)
                batch.draw(normal, width / 2 - normal.getRegionWidth() / 2, height * 0.15f, normal.getRegionWidth(), normal.getRegionHeight());
            else
                batch.draw(openPress, width / 2 - normal.getRegionWidth() / 2, height * 0.15f, normal.getRegionWidth(), normal.getRegionHeight());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
