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
public class LoadingActor extends Actor {
    private final int num = 300;
    private final int radius;
    private AssetManager assetManager;
    private List<TextureRegion> texReArray = new ArrayList();
    private Animation<TextureRegion> animation;
    private TextureRegion[] mKeyFrames = new TextureRegion[3];
    private float stateTime;
    private int width;
    private int height;

    private final int startX;
    private final int startY;
    private int changeRadius = 0;
    private int changeRadius2 = 0;
    private float angle = 0;
    private float angles = 0;
    private float rotate = 0;
    private boolean isShow = false;

    public LoadingActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        initResources();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        startX = width / 2 - texReArray.get(6).getRegionWidth() / 2;
        startY = height / 2 - texReArray.get(6).getRegionHeight() / 2;
        radius = height * 2 / 5 / num;
        ;
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
//        for (int i = 0; i < 2; i++) {
//
//
//        }
        if (startX - changeRadius >= 0) {
            batch.draw(texReArray.get(6), startX - changeRadius, startY - changeRadius, texReArray.get(6).getRegionWidth() + changeRadius * 2, texReArray.get(6).getRegionHeight() + changeRadius * 2);
            changeRadius += radius;
        } else {
            changeRadius = 0;
        }
        if (changeRadius >= startX / 2 || isShow) {
            if (startX - changeRadius2 >= 0) {
                isShow = true;
                Gdx.app.error("gdx", changeRadius + "   " + width / 4);
                batch.draw(texReArray.get(6), startX - changeRadius2, startY - changeRadius2, texReArray.get(6).getRegionWidth() + changeRadius2 * 2, texReArray.get(6).getRegionHeight() + changeRadius2 * 2);
                changeRadius2 += radius;
            } else {
                isShow = false;
                changeRadius2 = 0;
            }
        }

        batch.draw(texReArray.get(4), width / 2 - texReArray.get(4).getRegionWidth(), height / 2 - texReArray.get(4).getRegionHeight(), texReArray.get(4).getRegionWidth(), texReArray.get(4).getRegionHeight(), texReArray.get(4).getRegionWidth(), texReArray.get(4).getRegionHeight(), getScaleX(), getScaleY(), rotate);
        rotate++;
        batch.draw(texReArray.get(2), width / 2 - texReArray.get(2).getRegionWidth() / 2, height / 2 - texReArray.get(2).getRegionHeight() / 2, texReArray.get(2).getRegionWidth() / 2, texReArray.get(2).getRegionHeight() / 2, texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionHeight(), getScaleX(), getScaleY(), angle);
        angle++;
        batch.draw(texReArray.get(3), width / 2 - texReArray.get(3).getRegionWidth() / 2, height / 2 - texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth() / 2, texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth(), texReArray.get(3).getRegionHeight(), getScaleX(), getScaleY(), angles);
        angles -= 2;

//        if (startX - changeRadius >= 0) {
//            if (startX - changeRadius2 >= 0) {
//                if (changeRadius >= startY / 4) {
//                    Gdx.app.error("gdx", changeRadius + "   " + width / 4);
//                    batch.draw(texReArray.get(6), startX - changeRadius2, startY - changeRadius2, texReArray.get(6).getRegionWidth() + changeRadius2 * 2, texReArray.get(6).getRegionHeight() + changeRadius2 * 2);
//                    changeRadius2 += radius;
//                }
//            } else {
//                changeRadius2 = 0;
//            }
//            batch.draw(texReArray.get(6), startX - changeRadius, startY - changeRadius, texReArray.get(6).getRegionWidth() + changeRadius * 2, texReArray.get(6).getRegionHeight() + changeRadius * 2);
//            changeRadius += radius;
//        } else {
//            changeRadius = 0;
//        }
//        batch.draw(texReArray.get(1), 0, height / 2 - texReArray.get(1).getRegionHeight() / 2, width / 2, texReArray.get(1).getRegionHeight() / 2, width, texReArray.get(1).getRegionHeight(), getScaleX(), getScaleY(), angle);
//        angle++;
        /*
         * 绘制纹理区域
         * 将演员中的 位置(position, 即 X, Y 坐标), 缩放和旋转支点(origin), 宽高尺寸, 缩放比, 旋转角度 应用到绘制中,
         * 最终 batch 会将综合结果绘制到屏幕上
         */
//        stateTime += Gdx.graphics.getDeltaTime();

//        TextureRegion textureRegion = animation.getKeyFrame(stateTime);
        // 这里要注意，我们添加的action只是改变actor的属性值，绘制的时候并没有
        // 自动给我们处理这些逻辑， 我们要做的就是取得这些值，然后自己处理
//        batch.draw(textureRegion, 10, (height / 2 - texReArray.get(1).getRegionHeight() / 2) * 1.13f, width - 10, textureRegion.getRegionHeight());

        batch.draw(texReArray.get(1), width / 2 - texReArray.get(1).getRegionWidth() / 2, height / 2 - texReArray.get(1).getRegionHeight() / 2, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
        batch.draw(texReArray.get(5), width / 2 - texReArray.get(5).getRegionWidth() / 2, height / 2 + texReArray.get(6).getRegionHeight() / 2, texReArray.get(5).getRegionWidth(), texReArray.get(5).getRegionHeight());
    }

    public void initResources() {
        assetManager.load("main_bg.png", Texture.class);
//        assetManager.load("bg_grow.png", Texture.class);
        assetManager.load("loading_center.png", Texture.class);
//        assetManager.load("grow_1.png", Texture.class);
//        assetManager.load("grow_2.png", Texture.class);
//        assetManager.load("grow_3.png", Texture.class);
        assetManager.load("loading_in.png", Texture.class);
        assetManager.load("loading_out.png", Texture.class);
        assetManager.load("loading_rotate.png", Texture.class);
        assetManager.load("loading_tip.png", Texture.class);
        assetManager.load("loading_wait.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("main_bg.png")));
//        texReArray.add(new TextureRegion((Texture) assetManager.get("bg_grow.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("loading_center.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("loading_in.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("loading_out.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("loading_rotate.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("loading_tip.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("loading_wait.png")));

//        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("grow_1.png"));
//        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("grow_2.png"));
//        mKeyFrames[2] = new TextureRegion((Texture) assetManager.get("grow_3.png"));

//        texReArray = new ArrayList<>();
//        texReArray.add(new TextureRegion(new Texture(Gdx.files.internal("main_bg.png"))));
//        texReArray.add(new TextureRegion(new Texture(Gdx.files.internal("bg_grow.png"))));
//        texReArray.add(new TextureRegion(new Texture(Gdx.files.internal("loading_center.png"))));
//
//        mKeyFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("grow_1.png")));
//        mKeyFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("grow_2.png")));
//        mKeyFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("grow_3.png")));


//        animation = new Animation(0.2f, mKeyFrames);
//        animation.setPlayMode(Animation.PlayMode.LOOP);
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
