package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class AimActor extends Actor {
    private List<TextureRegion> texReArray = new ArrayList();
    private Animation<TextureRegion> animation;
    private TextureRegion[] mKeyFrames = new TextureRegion[3];
    private float stateTime;
    private int width;
    private int height;
    private AssetManager assetManager;

    public AimActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        initResources();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // 如果 region 为 null 或者 演员不可见, 则直接不绘制
        if (!isVisible()) {
            return;
        }
//        Gdx.app.error("gdx","render");
        batch.draw(texReArray.get(0), 0, 0, width, height);
        int aimWidth = width / 2 - texReArray.get(1).getRegionWidth() / 2;
        int aimHeight = height / 2 - texReArray.get(1).getRegionHeight() / 2;
        batch.draw(texReArray.get(1), aimWidth, aimHeight, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
        int degree = 360 / 12;
        batch.draw(mKeyFrames[0], width / 2 + aimWidth * 0.27f, height / 2 + aimHeight * 0.13f, width / 2, height / 2, mKeyFrames[0].getRegionWidth(), mKeyFrames[0].getRegionHeight(), getScaleX(), getScaleY(), 0);
//        batch.draw(texReArray.get(2), width / 2 - texReArray.get(2).getRegionWidth() / 2, (height / 2 - texReArray.get(1).getRegionHeight() / 2) * 1.07f, texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionWidth());
    }

    public void initResources() {
        assetManager.load("find_bg.png", Texture.class);
        assetManager.load("aim_fail.png", Texture.class);
        assetManager.load("aim_success.png", Texture.class);
        assetManager.load("aim_white.png", Texture.class);
        assetManager.load("aim_red.png", Texture.class);
        assetManager.load("aim_blue.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("find_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("aim_success.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("aim_fail.png")));

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("aim_white.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("aim_red.png"));
        mKeyFrames[2] = new TextureRegion((Texture) assetManager.get("aim_blue.png"));
    }

    @Override
    public void clear() {
        for (int i = 0; i < texReArray.size(); i++) {
            texReArray.get(i).getTexture().dispose();
        }
        for (int i = 0; i < mKeyFrames.length; i++) {
            mKeyFrames[i].getTexture().dispose();
        }
    }
}
