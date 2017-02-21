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
public class FindActor extends Actor {
    private List<TextureRegion> texReArray = new ArrayList();
    private Animation<TextureRegion> animation;
    private TextureRegion[] mKeyFrames = new TextureRegion[3];
    private float stateTime;
    private int width;
    private int height;
    private AssetManager assetManager;

    public FindActor(AssetManager assetManager) {
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
        batch.draw(texReArray.get(1), width / 2 - texReArray.get(1).getRegionWidth() / 2, height / 2 - texReArray.get(1).getRegionHeight() / 2, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
//        batch.draw(texReArray.get(2), width / 2 - texReArray.get(2).getRegionWidth() / 2, (height / 2 - texReArray.get(1).getRegionHeight() / 2) * 1.07f, texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionWidth());
    }

    public void initResources() {
        assetManager.load("find_bg.png", Texture.class);
        assetManager.load("find_center.png", Texture.class);
        assetManager.load("find_tip.png", Texture.class);
        assetManager.load("find_text.png", Texture.class);
        assetManager.load("find_location.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("find_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("find_center.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("find_location.png")));

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("find_tip.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("find_text.png"));
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
