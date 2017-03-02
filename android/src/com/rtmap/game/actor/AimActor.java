package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rtmap.game.interfaces.AimListener;

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
    private List<TextureRegion> findReArray = new ArrayList();
    private TextureRegion[] mKeyFrames = new TextureRegion[3];

    //绘制次数
    private static int number = 1;
    private int maxNumber = 12;
    private int width;
    private int height;
    private AssetManager assetManager;
    private int startAngle = 390;
    private float degree = startAngle;
    private float delta = 0;
    private int angle = 30;
    private AimListener aimListener;
    //是否发现模型，更改状态
    private boolean isFind = false;
    //模型进入视角内提示
    private boolean isTip = false;
    //初始控制
    private boolean isOne = true;
    //判断是否为失败返回场景
    private boolean isFail = false;


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

    public void setAimListener(AimListener aimListener) {
        this.aimListener = aimListener;
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
        if (isFind) {
            int aimWidth = width / 2 - texReArray.get(1).getRegionWidth() / 2;
            int aimHeight = height / 2 - texReArray.get(1).getRegionHeight() / 2;

            if (STATE == STATE_SUCCESS) {
                if (number == maxNumber) {
                    if (aimListener != null) {
                        aimListener.aimSuccess();
                    }
                }
                batch.draw(texReArray.get(1), aimWidth, aimHeight, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
                for (int i = 0; i < number; i++) {
                    if (delta < 0.5f && (number - 1) == i) {
                        Gdx.app.error("gdx", "白块111111111111111111");
                        batch.draw(mKeyFrames[2], width / 2 + aimWidth * 0.258f, height / 2 + aimHeight * 0.128f, aimHeight * 0.11f * -1f, aimWidth * 0.248f * -1f, mKeyFrames[0].getRegionWidth(), mKeyFrames[0].getRegionHeight(), getScaleX(), getScaleY(), degree - angle * i);
                    } else {
                        batch.draw(mKeyFrames[0], width / 2 + aimWidth * 0.258f, height / 2 + aimHeight * 0.128f, aimHeight * 0.11f * -1f, aimWidth * 0.248f * -1f, mKeyFrames[0].getRegionWidth(), mKeyFrames[0].getRegionHeight(), getScaleX(), getScaleY(), degree - angle * i);
                    }
                }
                if (delta > 1f) {
                    delta = 0;
                }
            } else if (STATE == STATE_FAIL) {
                batch.draw(texReArray.get(2), aimWidth, aimHeight, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
                for (int i = 0; i < number; i++) {
                    batch.draw(mKeyFrames[1], width / 2 + aimWidth * 0.258f, height / 2 + aimHeight * 0.128f, aimHeight * 0.11f * -1f, aimWidth * 0.248f * -1f, mKeyFrames[0].getRegionWidth(), mKeyFrames[0].getRegionHeight(), getScaleX(), getScaleY(), degree - angle * i);
                }
                if (delta > 1f) {
                    delta = 0;
                }
            }
        } else {
            batch.draw(findReArray.get(0), width / 2 - findReArray.get(0).getRegionWidth() / 2, height / 2 - findReArray.get(0).getRegionHeight() / 2, findReArray.get(0).getRegionWidth(), findReArray.get(0).getRegionHeight());
            if (isTip) {
                batch.draw(findReArray.get(2), width / 2 - findReArray.get(2).getRegionWidth() / 2, height / 2 - findReArray.get(2).getRegionHeight() / 2, findReArray.get(2).getRegionWidth(), findReArray.get(2).getRegionHeight());
                batch.draw(findReArray.get(3), width / 2 - findReArray.get(3).getRegionWidth() / 2, height / 2 - findReArray.get(3).getRegionHeight() / 2 + findReArray.get(3).getRegionHeight() / 5, findReArray.get(3).getRegionWidth(), findReArray.get(3).getRegionHeight());
            }
        }
    }

    public void setIsFind(boolean isFind) {
        this.isFind = isFind;
    }

    public void setIsTip(boolean isTip) {
        this.isTip = isTip;
    }

    public void setIsFail(boolean isFail) {
        this.isFail = isFail;
        if (isFail) {
            number = maxNumber;
            STATE = STATE_FAIL;
            setIsFind(true);
        }
    }

    public void addNumber() {
        STATE = STATE_SUCCESS;
        setIsFind(true);
        if (isOne) {
            delta = 0;
            isOne = false;
        }
        delta += Gdx.graphics.getDeltaTime();
        if (delta > 1f)
            number++;
    }

    public void subNumber() {
        if (number == 0) {
            if (aimListener != null) {
                aimListener.aimFail();
            }
            return;
        }
        STATE = STATE_FAIL;
        delta += Gdx.graphics.getDeltaTime();
        if (number > 0 && delta > 1f)
            number--;
    }

    public void initResources() {
        assetManager.load("find_bg.png", Texture.class);
        assetManager.load("aim_fail.png", Texture.class);
        assetManager.load("aim_success.png", Texture.class);
        assetManager.load("aim_white.png", Texture.class);
        assetManager.load("aim_red.png", Texture.class);
        assetManager.load("aim_blue.png", Texture.class);

        assetManager.load("find_center.png", Texture.class);
        assetManager.load("find_tip.png", Texture.class);
        assetManager.load("find_text.png", Texture.class);
        assetManager.load("find_location.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("find_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("aim_success.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("aim_fail.png")));

        findReArray = new ArrayList<>();
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_center.png")));
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_location.png")));
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_tip.png")));
        findReArray.add(new TextureRegion((Texture) assetManager.get("find_text.png")));

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
