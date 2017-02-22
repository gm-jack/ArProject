package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class CatchActor extends Actor {
    private List<TextureRegion> texReArray = new ArrayList();
    private TextureRegion[] mKeyFrames = new TextureRegion[3];
    private Animation<TextureRegion> animation;

    private int width;
    private int height;
    private AssetManager assetManager;


    public CatchActor(AssetManager assetManager) {
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

        batch.draw(texReArray.get(0), 0, 0, width, height);
        int aimWidth = width / 2 - texReArray.get(1).getRegionWidth() / 2;
        int aimHeight = height / 2 - texReArray.get(1).getRegionHeight() / 2;
        batch.draw(texReArray.get(1), aimWidth, aimHeight, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
        Affine2 affine2 = new Affine2();
        affine2.setToScaling(4, 4);
        int scale = 255 / texReArray.get(2).getRegionHeight();
        Gdx.app.error("gdx", scale + "");
//        batch.draw(texReArray.get(2), width / 2 - 255, height / 2 - 255, getOriginX(), getOriginY(), texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionHeight(), scale, scale, getRotation());
        batch.draw(texReArray.get(2), 0, 0, getOriginX(), getOriginY(), texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionHeight(), scale, scale, getRotation());
    }

    public void initResources() {
        assetManager.load("find_bg.png", Texture.class);
        assetManager.load("catch_center.png", Texture.class);
        assetManager.load("catch_circle.png", Texture.class);
        assetManager.load("catch_button_normal.png", Texture.class);
        assetManager.load("catch_button_press.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("find_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_center.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_circle.png")));

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("catch_button_normal.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("catch_button_press.png"));
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
