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
    private AssetManager assetManager;
    private List<TextureRegion> texReArray = new ArrayList();
    private Animation<TextureRegion> animation;
    private TextureRegion[] mKeyFrames = new TextureRegion[3];
    private float stateTime;
    private int width;
    private int height;

    public LoadingActor(AssetManager assetManager) {
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
        batch.draw(texReArray.get(1), 0, height / 2 - texReArray.get(1).getRegionHeight() / 2, width, texReArray.get(1).getRegionHeight());

        /*
         * 绘制纹理区域
         * 将演员中的 位置(position, 即 X, Y 坐标), 缩放和旋转支点(origin), 宽高尺寸, 缩放比, 旋转角度 应用到绘制中,
         * 最终 batch 会将综合结果绘制到屏幕上
         */
        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion textureRegion = animation.getKeyFrame(stateTime);
        // 这里要注意，我们添加的action只是改变actor的属性值，绘制的时候并没有
        // 自动给我们处理这些逻辑， 我们要做的就是取得这些值，然后自己处理
        batch.draw(textureRegion, 10, (height / 2 - texReArray.get(1).getRegionHeight() / 2) * 1.13f, width - 10, textureRegion.getRegionHeight());

        batch.draw(texReArray.get(2), width / 2 - texReArray.get(2).getRegionWidth() / 2, (height / 2 - texReArray.get(1).getRegionHeight() / 2) * 1.07f, texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionWidth());
    }

    public void initResources() {
        assetManager.load("main_bg.png", Texture.class);
        assetManager.load("bg_grow.png", Texture.class);
        assetManager.load("loading_center.png", Texture.class);
        assetManager.load("grow_1.png", Texture.class);
        assetManager.load("grow_2.png", Texture.class);
        assetManager.load("grow_3.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("main_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("bg_grow.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("loading_center.png")));

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("grow_1.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("grow_2.png"));
        mKeyFrames[2] = new TextureRegion((Texture) assetManager.get("grow_3.png"));

//        texReArray = new ArrayList<>();
//        texReArray.add(new TextureRegion(new Texture(Gdx.files.internal("main_bg.png"))));
//        texReArray.add(new TextureRegion(new Texture(Gdx.files.internal("bg_grow.png"))));
//        texReArray.add(new TextureRegion(new Texture(Gdx.files.internal("loading_center.png"))));
//
//        mKeyFrames[0] = new TextureRegion(new Texture(Gdx.files.internal("grow_1.png")));
//        mKeyFrames[1] = new TextureRegion(new Texture(Gdx.files.internal("grow_2.png")));
//        mKeyFrames[2] = new TextureRegion(new Texture(Gdx.files.internal("grow_3.png")));


        animation = new Animation(0.2f, mKeyFrames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
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
