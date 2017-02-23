package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.rtmap.game.interfaces.CatchListener;

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
    private int num = 200;
    private float changeRadiu = 0;
    private float changeX = 0;
    private float changeY = 0;
    private boolean isComplete = true;
    private int radius;
    private boolean isBig = true;
    private boolean isStop = false;
    public int catchNumber = 0;
    private CatchListener catchListener;
    private boolean isFirst = true;


    public CatchActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        initResources();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        changeX = width / 2;
        changeY = height / 2;
        radius = height * 2 / 5 / num;
    }

    public void setCatchListener(CatchListener catchListener) {
        this.catchListener = catchListener;
    }

    public void removeListener() {
        this.catchListener = null;
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
        //有中心点到（-10，-10）的距离的平方/2开根号求得宽高

        int regionHeight = texReArray.get(1).getRegionWidth();
        int minRadius = regionHeight * 3 / 10;
        int maxRadius = regionHeight * 12 / 25;
        if (isBig) {
            batch.draw(texReArray.get(2), changeX - changeRadiu, changeY - changeRadiu, changeRadiu * 2, changeRadiu * 2);
//            Gdx.app.error("gdx", "changeX=" + (changeX) + "  changeY=" + (changeY) + "   changeRadiu=" + changeRadiu + "   regionHeight== " + regionHeight);
            if (!isStop) {
                changeRadiu += radius;
            } else {
                if (changeRadiu > minRadius && changeRadiu < maxRadius) {

                    batch.draw(texReArray.get(3), width / 2 - texReArray.get(3).getRegionWidth() / 2, height / 2 - texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth(), texReArray.get(3).getRegionHeight());
                    if (catchListener != null && isFirst) {
                        catchListener.onSuccess();
                        isFirst = false;
                    }

//                    Gdx.app.error("gdx", "changeRadiu== " + changeRadiu + " ==" + minRadius + " == " + maxRadius);

                } else {

                    batch.draw(texReArray.get(4), width / 2 - texReArray.get(4).getRegionWidth() / 2, height / 2 - texReArray.get(4).getRegionHeight() / 2, texReArray.get(4).getRegionWidth(), texReArray.get(4).getRegionHeight());
                    if (catchListener != null && isFirst) {
                        isFirst = false;
                        catchListener.onFail();
                    }
//                    Gdx.app.error("gdx", "changeRadiu== " + changeRadiu + " ==" + minRadius + " == " + maxRadius);

                }
            }
            if (changeRadiu >= height * 2 / 5) {
                isBig = false;
                if (catchNumber == 5) {
                    if (catchListener != null && isFirst) {
                        isFirst = false;
                        catchListener.onNumberFail(catchNumber);
                    }
                }
            }

        } else {
            batch.draw(texReArray.get(2), changeX - changeRadiu, changeY - changeRadiu, changeRadiu * 2, changeRadiu * 2);
//            Gdx.app.error("gdx", "changeX=" + (changeX) + "  changeY=" + (changeY) + "   changeRadiu=" + changeRadiu + "   regionHeight== " + regionHeight);
            if (changeRadiu <= 0) {
                isBig = true;
                catchNumber++;
            }
            if (!isStop) {
                changeRadiu -= radius;
            } else {
                if (changeRadiu > minRadius && changeRadiu < maxRadius) {
                    batch.draw(texReArray.get(3), width / 2 - texReArray.get(3).getRegionWidth() / 2, height / 2 - texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth(), texReArray.get(3).getRegionHeight());
                    if (catchListener != null && isFirst) {
                        isFirst = false;
                        catchListener.onSuccess();
                    }
                } else {

                    batch.draw(texReArray.get(4), width / 2 - texReArray.get(4).getRegionWidth() / 2, height / 2 - texReArray.get(4).getRegionHeight() / 2, texReArray.get(4).getRegionWidth(), texReArray.get(4).getRegionHeight());
                    if (catchListener != null && isFirst) {
                        isFirst = false;
                        catchListener.onFail();
                    }
//                    Gdx.app.error("gdx", "changeRadiu== " + changeRadiu + " ==" + minRadius + " == " + maxRadius);

                }
            }
        }
//        Affine2 affine2 = new Affine2();
//        affine2.setToScaling(4, 4);
//        int scale = 255 / texReArray.get(2).getRegionHeight();
//        Gdx.app.error("gdx", scale + "");
////        batch.draw(texReArray.get(2), width / 2 - 255, height / 2 - 255, getOriginX(), getOriginY(), texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionHeight(), scale, scale, getRotation());
//        batch.draw(texReArray.get(2), 0, 0, getOriginX(), getOriginY(), texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionHeight(), scale, scale, getRotation());
    }

    public void setIsStop(boolean isStop) {
        this.isStop = isStop;
    }

    public void initResources() {
        assetManager.load("catch_bg.png", Texture.class);
        assetManager.load("catch_center.png", Texture.class);
        assetManager.load("catch_circle.png", Texture.class);
        assetManager.load("catch_good.png", Texture.class);
        assetManager.load("catch_miss.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_center.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_circle.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_good.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_miss.png")));
    }

    public void reset() {
        catchNumber = 0;
        changeRadiu = 0;
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
