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
    /**
     * 状态
     */
    public static final int STATE_SUCCESS = 0;
    public static final int STATE_NORMAL = 1;
    public static final int STATE_FAIL = 2;
    public static int STATE = STATE_SUCCESS;

    private List<TextureRegion> texReArray = new ArrayList();
    private TextureRegion[] mKeyFrames = new TextureRegion[3];
    private Animation<TextureRegion> animation;

    //绘制次数
    private int number = 1;
    private int maxNumber = 12;
    private float stateTime;
    private int width;
    private int height;
    private AssetManager assetManager;
    private int startAngle = 390;
    private float degree = startAngle;
    private float delta = 0;
    private float oldDegree;
    private int angle = 30;


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

//        if (number == 11) {
//            STATE = STATE_FAIL;
//        } else if (number == 0) {
//            STATE = STATE_SUCCESS;
//        }
        batch.draw(texReArray.get(0), 0, 0, width, height);
        int aimWidth = width / 2 - texReArray.get(1).getRegionWidth() / 2;
        int aimHeight = height / 2 - texReArray.get(1).getRegionHeight() / 2;

        delta += Gdx.graphics.getDeltaTime();
        if (STATE == STATE_SUCCESS) {
            batch.draw(texReArray.get(1), aimWidth, aimHeight, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
            if (number > maxNumber) {
                STATE = STATE_FAIL;
            }
            for (int i = 0; i < number; i++) {
                if (delta < 0.5f && number == i) {
                    batch.draw(mKeyFrames[2], width / 2 + aimWidth * 0.258f, height / 2 + aimHeight * 0.138f, aimHeight * 0.11f * -1f, aimWidth * 0.248f * -1f, mKeyFrames[0].getRegionWidth(), mKeyFrames[0].getRegionHeight(), getScaleX(), getScaleY(), degree - angle * i);
                } else {
                    batch.draw(mKeyFrames[0], width / 2 + aimWidth * 0.258f, height / 2 + aimHeight * 0.138f, aimHeight * 0.11f * -1f, aimWidth * 0.248f * -1f, mKeyFrames[0].getRegionWidth(), mKeyFrames[0].getRegionHeight(), getScaleX(), getScaleY(), degree - angle * i);
                }
            }
            if (delta > 1f) {
                number++;
                delta = 0;
            }
        } else if (STATE == STATE_FAIL) {
            batch.draw(texReArray.get(2), aimWidth, aimHeight, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
            if (number <= 1) {
                STATE = STATE_SUCCESS;
            }
            for (int i = 0; i < number - 2; i++) {
                batch.draw(mKeyFrames[1], width / 2 + aimWidth * 0.258f, height / 2 + aimHeight * 0.138f, aimHeight * 0.11f * -1f, aimWidth * 0.248f * -1f, mKeyFrames[0].getRegionWidth(), mKeyFrames[0].getRegionHeight(), getScaleX(), getScaleY(), degree - angle * i);
            }
            if (delta > 1f) {
                number--;
                delta = 0;
            }
        }
        Gdx.app.error("gdx", "degree== " + degree);
//        batch.draw(texReArray.get(2), width / 2 - texReArray.get(2).getRegionWidth() / 2, (height / 2 - texReArray.get(1).getRegionHeight() / 2) * 1.07f, texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionWidth());
    }

    public void addNumber() {
        number++;
    }

    public void subNumber() {
        number--;
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

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("aim_blue.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("aim_red.png"));
        mKeyFrames[2] = new TextureRegion((Texture) assetManager.get("aim_white.png"));
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
