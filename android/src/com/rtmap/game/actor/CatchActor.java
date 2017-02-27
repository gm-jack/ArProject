package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.rtmap.game.interfaces.CatchListener;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.FontUtil;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class CatchActor extends Actor {
    /**
     * 资源文件管理
     */
    private AssetManager assetManager;
    private List<TextureRegion> texReArray = new ArrayList();
    private List<TextureRegion> successTexRe = new ArrayList();
    private List<TextureRegion> openTexRe = new ArrayList();
    private TextureRegion[] mKeyFrames = new TextureRegion[4];
    /**
     * 显示范围宽高
     */
    private int width;
    private int height;

    private float changeX = 0;
    private float changeY = 0;
    private int radius;
    //捕捉监听
    private CatchListener catchListener;
    //红圈半径变化值
    private float changeRadiu = 1;
    //控制红圈的动画速率
    private int num = 200;
    //控制红圈缩放次数
    public int catchNumber = 0;
    //控制红圈的放大和缩小
    private boolean isBig = true;
    //控制红圈是否暂停缩放
    private boolean isStop = false;
    //控制监听触发次数=1
    private boolean isFirst = true;
    //控制用户是否第一次进入
    private boolean first = false;
    //控制用户捕捉失败显示按任意键返回的提示
    private boolean fail = false;
    //控制捕捉提示
    private boolean isCatch = true;
    //是否显示捕捉提示
    private boolean isCatchTip = false;
    //是否成功捕捉
    private boolean isSuccess = false;
    //是否开启宝物
    private boolean isOpen = false;
    //是否中奖
    private boolean isWin = false;
    private LazyBitmapFont lazyBitmapFont;


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
        if (!fail) {
            int aimWidth = width / 2 - texReArray.get(1).getRegionWidth() / 2;
            int aimHeight = height / 2 - texReArray.get(1).getRegionHeight() / 2;
            batch.draw(texReArray.get(1), aimWidth, aimHeight, texReArray.get(1).getRegionWidth(), texReArray.get(1).getRegionHeight());

            int regionHeight = texReArray.get(1).getRegionWidth();
            int minRadius = regionHeight * 3 / 10;
            int maxRadius = regionHeight * 12 / 25;
            if (isBig) {
                batch.draw(texReArray.get(2), changeX - changeRadiu, changeY - changeRadiu, changeRadiu * 2, changeRadiu * 2);
                //            Gdx.app.error("gdx", "changeX=" + (changeX) + "  changeY=" + (changeY) + "   changeRadiu=" + changeRadiu + "   regionHeight== " + regionHeight);
                if (!isStop) {
                    if (changeRadiu >= ((minRadius + maxRadius) / 2 - 3) && changeRadiu <= ((minRadius + maxRadius) / 2 + 3) && first) {
                        batch.draw(texReArray.get(3), width / 2 - texReArray.get(3).getRegionWidth() / 2, height / 2 - texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth(), texReArray.get(3).getRegionHeight());
                        if (catchListener != null && first) {
                            catchListener.onFirst();
                        }
                    } else
                        changeRadiu += radius;
                } else if (!isCatch) {
                    //在开始捕捉界面之后运行
                    if (!isCatchTip) {
                        if (changeRadiu > minRadius && changeRadiu < maxRadius) {

                            batch.draw(texReArray.get(3), width / 2 - texReArray.get(3).getRegionWidth() / 2, height / 2 - texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth(), texReArray.get(3).getRegionHeight());
                            if (catchListener != null && isFirst) {
                                isFirst = false;
                                catchListener.onSuccess();
                            }
                            //Gdx.app.error("gdx", "changeRadiu== " + changeRadiu + " ==" + minRadius + " == " + maxRadius);
                        } else {
                            batch.draw(texReArray.get(4), width / 2 - texReArray.get(4).getRegionWidth() / 2, height / 2 - texReArray.get(4).getRegionHeight() / 2, texReArray.get(4).getRegionWidth(), texReArray.get(4).getRegionHeight());
                            if (catchListener != null && isFirst) {
                                isFirst = false;
                                catchListener.onFail();
                            }
                            //Gdx.app.error("gdx", "changeRadiu== " + changeRadiu + " ==" + minRadius + " == " + maxRadius);
                        }
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
                //Gdx.app.error("gdx", "changeX=" + (changeX) + "  changeY=" + (changeY) + "   changeRadiu=" + changeRadiu + "   regionHeight== " + regionHeight);
                if (changeRadiu <= 0) {
                    isBig = true;
                    catchNumber++;
                }
                if (!isStop) {
                    //                if (changeRadiu == (minRadius + maxRadius) / 2 && first) {
                    //                    batch.draw(texReArray.get(3), width / 2 - texReArray.get(3).getRegionWidth() / 2, height / 2 - texReArray.get(3).getRegionHeight() / 2, texReArray.get(3).getRegionWidth(), texReArray.get(3).getRegionHeight());
                    //                    if (catchListener != null && isFirst) {
                    //                        catchListener.onFirst();
                    //                    }
                    //                } else
                    changeRadiu -= radius;
                } else if (!isCatch) {
                    if (!isCatchTip) {
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
            }

        } else {
            if (isSuccess) {
                if (isOpen) {
                    batch.draw(openTexRe.get(0), 0.07f * width, height * 0.11f, width * 0.86f, height * 0.79f);
                    if (isWin) {
                        batch.draw(openTexRe.get(1), width / 2 - openTexRe.get(1).getRegionWidth() / 2, height * 0.68f, openTexRe.get(1).getRegionWidth(), openTexRe.get(1).getRegionHeight());

                        float length2 = FontUtil.getLength(ScreenUtil.dp2px(20), "星巴克5元优惠券", 2);
                        FontUtil.draw(batch, "星巴克5元优惠券", ScreenUtil.dp2px(20), Color.WHITE, width / 2 - length2 / 2, height * 0.68f - openTexRe.get(1).getRegionHeight(), width);

                        batch.draw(openTexRe.get(2), width / 2 - openTexRe.get(2).getRegionWidth() / 2, height * 0.68f - openTexRe.get(1).getRegionHeight() - ScreenUtil.dp2px(22) - 15, openTexRe.get(2).getRegionWidth(), openTexRe.get(2).getRegionHeight());

                        float length3 = FontUtil.getLength(ScreenUtil.dp2px(12), "请到我的-优惠券里查看", 2);
                        FontUtil.draw(batch, "请到我的-优惠券里查看", ScreenUtil.dp2px(12), Color.WHITE, width / 2 - length3 / 2, height * 0.68f - openTexRe.get(1).getRegionHeight() - ScreenUtil.dp2px(22) - openTexRe.get(2).getRegionHeight() - 25, width);

                    } else {
                        float length1 = FontUtil.getLength(ScreenUtil.dp2px(18), "运气还差那么一点点", 2);
                        FontUtil.draw(batch, "运气还差那么一点点", ScreenUtil.dp2px(18), Color.WHITE, width / 2 - length1 / 2, height * 0.65f, width);

                        float length2 = FontUtil.getLength(ScreenUtil.dp2px(22), "锦囊空空如也", 2);
                        FontUtil.draw(batch, "锦囊空空如也", ScreenUtil.dp2px(22), Color.WHITE, width / 2 - length2 / 2, height * 0.55f, width);

                        batch.draw(openTexRe.get(2), width / 2 - openTexRe.get(2).getRegionWidth() / 2, height * 0.55f - ScreenUtil.dp2px(22) - 5, openTexRe.get(2).getRegionWidth(), openTexRe.get(2).getRegionHeight());

                        float length3 = FontUtil.getLength(ScreenUtil.dp2px(14), "努力就有收获，再接再厉吧！", 2);
                        FontUtil.draw(batch, "努力就有收获，再接再厉吧！", ScreenUtil.dp2px(14), Color.WHITE, width / 2 - length3 / 2, height * 0.55f - ScreenUtil.dp2px(22) - openTexRe.get(2).getRegionHeight() - 5, width);

//                        lazyBitmapFont = new LazyBitmapFont(new FreeTypeFontGenerator(Gdx.files.internal("font/msyh.ttf")), 40);
//                        lazyBitmapFont.draw(batch, "wwwwww,,,星巴克5元优惠券", 100, 200);
                    }

                } else {
                    batch.draw(successTexRe.get(0), 0, height * 4 / 5, successTexRe.get(0).getRegionWidth(), successTexRe.get(0).getRegionHeight());
                    batch.draw(successTexRe.get(1), width / 2 - successTexRe.get(1).getRegionWidth() / 2, height / 2 - successTexRe.get(1).getRegionHeight() / 2, successTexRe.get(1).getRegionWidth(), successTexRe.get(1).getRegionHeight());
                }

            } else {
                batch.draw(mKeyFrames[2], width / 2 - mKeyFrames[2].getRegionWidth() / 2, height / 2 - mKeyFrames[2].getRegionHeight() / 2, mKeyFrames[2].getRegionWidth(), mKeyFrames[2].getRegionHeight());
                batch.draw(mKeyFrames[1], width / 2 - mKeyFrames[1].getRegionWidth() / 2, height / 2 - mKeyFrames[2].getRegionHeight() / 2 + mKeyFrames[2].getRegionHeight() / 5, mKeyFrames[1].getRegionWidth(), mKeyFrames[1].getRegionHeight());
                if (Gdx.input.isTouched()) {
                    if (catchListener != null) {
                        catchListener.onTouched(0);
                    }
                }
            }
        }
        if (isCatch) {
            float i = width * 1f / mKeyFrames[0].getRegionWidth();
            float regionHeight = mKeyFrames[0].getRegionHeight() * i;
            batch.draw(mKeyFrames[0], 0, height / 2 - regionHeight / 2, width, regionHeight);
        }
        if (isCatchTip) {
            batch.draw(mKeyFrames[2], width / 2 - mKeyFrames[2].getRegionWidth() / 2, height / 2 - mKeyFrames[2].getRegionHeight() / 2, mKeyFrames[2].getRegionWidth(), mKeyFrames[2].getRegionHeight());
            batch.draw(mKeyFrames[3], width / 2 - mKeyFrames[3].getRegionWidth() / 2, height / 2 - mKeyFrames[2].getRegionHeight() / 2 + mKeyFrames[2].getRegionHeight() / 5, mKeyFrames[3].getRegionWidth(), mKeyFrames[3].getRegionHeight());
            if (Gdx.input.isTouched()) {
                if (catchListener != null) {
                    catchListener.onTouched(1);
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

    public void setIsFirst(boolean isFirst) {
        this.first = isFirst;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public boolean getFail() {
        return fail;
    }

    public void setCatch(boolean isCatch) {
        this.isCatch = isCatch;
    }

    public void setIsCatchTip(boolean isCatchTip) {
        this.isCatchTip = isCatchTip;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void setIsWin(boolean isWin) {
        this.isWin = isWin;
    }

    public void initResources() {
        assetManager.load("catch_bg.png", Texture.class);
        assetManager.load("catch_center.png", Texture.class);
        assetManager.load("catch_circle.png", Texture.class);
        assetManager.load("catch_good.png", Texture.class);
        assetManager.load("catch_miss.png", Texture.class);
        assetManager.load("catch_fail.png", Texture.class);
        assetManager.load("catch_tip.png", Texture.class);
        assetManager.load("catch_tip_text.png", Texture.class);
        assetManager.load("find_tip.png", Texture.class);
        assetManager.load("find_tip.png", Texture.class);
        assetManager.load("find_tip.png", Texture.class);
        assetManager.load("success_title.png", Texture.class);
        assetManager.load("success_center.png", Texture.class);
        assetManager.load("open_bg.png", Texture.class);
        assetManager.load("open_title.png", Texture.class);
        assetManager.load("open_line.png", Texture.class);
        assetManager.load("open_fail.png", Texture.class);
        assetManager.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_center.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_circle.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_good.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_miss.png")));

        successTexRe = new ArrayList<>();
        successTexRe.add(new TextureRegion((Texture) assetManager.get("success_title.png")));
        successTexRe.add(new TextureRegion((Texture) assetManager.get("success_center.png")));

        openTexRe = new ArrayList();
        openTexRe.add(new TextureRegion((Texture) assetManager.get("open_bg.png")));
        openTexRe.add(new TextureRegion((Texture) assetManager.get("open_title.png")));
        openTexRe.add(new TextureRegion((Texture) assetManager.get("open_line.png")));
        openTexRe.add(new TextureRegion((Texture) assetManager.get("open_fail.png")));

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("catch_tip.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("catch_fail.png"));
        mKeyFrames[2] = new TextureRegion((Texture) assetManager.get("find_tip.png"));
        mKeyFrames[3] = new TextureRegion((Texture) assetManager.get("catch_tip_text.png"));
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
