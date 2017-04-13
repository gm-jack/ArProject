package com.rtmap.game.actor;


import android.view.animation.Animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.rtmap.game.MyGame;
import com.rtmap.game.camera.AndroidDeviceCameraController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class LoadingActor extends Actor {
    private final int num = 100;
    private final int nums = 5;
    private final int numbers = 10;
    private final int number = 20;
    private final int radius;
    private final AndroidDeviceCameraController cameraController;
    private final MyGame mGame;
    private AssetManager asset;
    private List<TextureRegion> texReArray = new ArrayList();
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
    private float sqrt;

    //半径变化值
    private float changeOutRadiu = 0;
    private float changeInRadiu = 0;
    private float changeCenterRadiu = 0;
    //初始动画
    private boolean isAnimation;
    private boolean isFirst = true;
    private boolean isEndAnimation = false;
    private boolean isLodingShow = true;
    private boolean isAnimationFirst = true;

    public LoadingActor(AssetManager assetManager, AndroidDeviceCameraController cameraController, MyGame game) {
        super();
        this.asset = assetManager;
        this.cameraController = cameraController;
        this.mGame = game;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        startX = width / 2;
        startY = height / 2;

        radius = height * 2 / 5 / num;
        sqrt = (float) Math.sqrt(height / 2 * height / 2 + width / 2 * width / 2);
        isAnimation = true;
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
        if (asset.update() && isFirst) {
            initResources();
            isFirst = false;
        }
        if (texReArray.size() <= 0) return;
        batch.draw(texReArray.get(0), 0, 0, width, height);
        if (isLodingShow) {
            if (isEndAnimation) {
                //            Gdx.app.error("loading", "changeOutRadiu >= texReArray.get(3).getRegionHeight()  " + (changeOutRadiu >= texReArray.get(3).getRegionHeight()));
                if (startX - changeOutRadiu >= -startX / 4) {
                    batch.draw(texReArray.get(3), startX - changeOutRadiu, startY - changeOutRadiu, changeOutRadiu * 2, changeOutRadiu * 2);
                    changeOutRadiu += texReArray.get(3).getRegionHeight() / 8;
                }
                if (startX - changeOutRadiu >= -startX / 4) {
                    batch.draw(texReArray.get(2), startX - changeInRadiu, startY - changeInRadiu, changeInRadiu * 2, changeInRadiu * 2);
                    changeInRadiu += texReArray.get(2).getRegionHeight() / 8;
                }
                batch.draw(texReArray.get(1), startX - changeCenterRadiu, startY - changeCenterRadiu, changeCenterRadiu * 2, changeCenterRadiu * 2);
                if (changeCenterRadiu > 0) {
                    changeCenterRadiu -= texReArray.get(1).getRegionHeight() / 2 / number;
                }
                if (isAnimationFirst) {
                    isAnimationFirst = false;
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Gdx.app.error("camera", "render()");
                            cameraController.animationCenter(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    cameraController.endAnimation();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
//                            cameraController.showImage(true);
//                            cameraController.pictureCamera();
//                            cameraController.setAnimationEnd(new AnimationEnd() {
//                                @Override
//                                public void end() {
//                                    Gdx.app.postRunnable(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            if (mGame != null) {
//                                                mGame.showAimScreen(false);
//                                            }
//                                        }
//                                    });
//                                }
//                            });
                        }
                    });
                }
            } else if (isAnimation) {
                batch.draw(texReArray.get(3), startX - changeOutRadiu, startY - changeOutRadiu, changeOutRadiu * 2, changeOutRadiu * 2);
                //            Gdx.app.error("loading", "changeOutRadiu >= texReArray.get(3).getRegionHeight()  " + (changeOutRadiu >= texReArray.get(3).getRegionHeight()));
                if (changeOutRadiu > texReArray.get(3).getRegionHeight() / 2) {
                    changeOutRadiu -= texReArray.get(3).getRegionHeight() / 2 / numbers;
                } else {
                    isAnimation = false;
                }
                batch.draw(texReArray.get(2), startX - changeInRadiu, startY - changeInRadiu, changeInRadiu * 2, changeInRadiu * 2);
                if (changeInRadiu > texReArray.get(2).getRegionHeight() / 2) {
                    changeInRadiu -= texReArray.get(2).getRegionHeight() / 2 / nums;
                }
                batch.draw(texReArray.get(1), startX - changeCenterRadiu, startY - changeCenterRadiu, changeCenterRadiu * 2, changeCenterRadiu * 2);
                if (changeCenterRadiu < texReArray.get(1).getRegionHeight() / 2) {
                    changeCenterRadiu += texReArray.get(1).getRegionHeight() / 2 / number;
                }
            } else {
                if (startX - changeRadius >= -startX / 4) {
                    batch.draw(texReArray.get(6), startX - changeRadius, startY - changeRadius, changeRadius * 2, changeRadius * 2);
                    changeRadius += radius;
                } else {
                    changeRadius = 0;
                }
                if (changeRadius >= startX / 2 || isShow) {
                    if (startX - changeRadius2 >= -startX / 4) {
                        isShow = true;
                        batch.draw(texReArray.get(6), startX - changeRadius2, startY - changeRadius2, changeRadius2 * 2, changeRadius2 * 2);
                        changeRadius2 += radius;
                    } else {
                        isShow = false;
                        changeRadius2 = 0;
                    }
                }
                batch.draw(texReArray.get(4), width / 2 - texReArray.get(4).getRegionWidth(), height / 2, texReArray.get(4).getRegionWidth(), 0, texReArray.get(4).getRegionWidth(), sqrt, getScaleX(), getScaleY(), rotate);
                rotate -= 3;
                batch.draw(texReArray.get(2), width / 2 - texReArray.get(2).getRegionWidth() / 2, height / 2 - texReArray.get(2).getRegionHeight() / 2, texReArray.get(2).getRegionWidth() / 2, texReArray.get(2).getRegionHeight() / 2, texReArray.get(2).getRegionWidth(), texReArray.get(2).getRegionHeight(), getScaleX(), getScaleY(), angle);
                angle++;
                batch.draw(texReArray.get(3), width / 2 - texReArray.get(3).getRegionWidth() / 2, height / 2 - texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth() / 2, texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth(), texReArray.get(3).getRegionHeight(), getScaleX(), getScaleY(), angles);
                angles -= 2;
                batch.draw(texReArray.get(1), width / 2 - texReArray.get(1).getRegionWidth() / 2, height / 2 - texReArray.get(1).getRegionHeight() / 2, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());
                batch.draw(texReArray.get(5), width / 2 - texReArray.get(5).getRegionWidth() / 2, height / 2 + texReArray.get(6).getRegionHeight() / 2, texReArray.get(5).getRegionWidth(), texReArray.get(5).getRegionHeight());
            }
        }

    }

    public void initResources() {
        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) asset.get("main_bg.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("loading_center.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("loading_in.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("loading_out.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("loading_rotate.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("loading_tip.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("loading_wait.png")));

        changeOutRadiu = texReArray.get(3).getRegionHeight() * 2;
        changeInRadiu = texReArray.get(2).getRegionHeight() * 2;
    }

    public void setIsEndAnimation(boolean isEndAnimation) {
        this.isEndAnimation = isEndAnimation;
    }

    public boolean isEndAnimation() {
        return isEndAnimation;
    }

    public float getTime() {
        return number * Gdx.graphics.getDeltaTime();
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    @Override
    public void clear() {
        for (int i = 0; i < texReArray.size(); i++) {
            texReArray.get(i).getTexture().dispose();
        }
    }
}
