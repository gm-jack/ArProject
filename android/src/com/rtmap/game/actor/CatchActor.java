package com.rtmap.game.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.CatchListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.NetUtil;
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
    private float changeRadiu = 0;
    //控制红圈的动画速率
    private int num = 150;
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

    private Result result;
    private Texture texture;
    private LazyBitmapFont lazyBitmapFont1;
    private LazyBitmapFont lazyBitmapFont2;
    private LazyBitmapFont lazyBitmapFont3;
    private LazyBitmapFont lazyBitmapFont4;
    private LazyBitmapFont lazyBitmapFont5;
    private float pictureMeter;
    private float bottomHeight;
    private float mX;
    private float mFloat;
    private boolean isCatchFirst = true;
    private float mScale;
    private float catchCenterWidth;
    private float catchCenterHeight;
    private float centerWidth;
    private float centerHeight;
    private float titleWidth;
    private float titleHeight;
    private float goodWidth;
    private float goodHeight;
    private float mKey2Width;
    private float mKey2Height;
    private float mKey3Width;
    private float mKey3Height;
    private float openFailWidth;
    private float openFailHeight;

    public CatchActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        changeX = width / 2;
        changeY = height / 2;
        changeRadiu = height * 2 / 5;
        pictureMeter = width * 0.136f;
        bottomHeight = height * 0.287f;
        mX = width * 0.293f;
        mFloat = height * 0.68f;
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
    public void draw(final Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        // 如果 region 为 null 或者 演员不可见, 则直接不绘制
        if (!isVisible()) {
            return;
        }
        if (assetManager.update()) {
            initResources();
        }
        if (texReArray.size() <= 0 || successTexRe.size() <= 0 || openTexRe.size() <= 0 || mKeyFrames.length <= 0)
            return;
        batch.draw(texReArray.get(0), 0, 0, width, height);
        if (!fail) {
            float aimWidth = width / 2 - catchCenterWidth / 2;
            float aimHeight = height / 2 - catchCenterHeight / 2;
            batch.draw(texReArray.get(1), aimWidth, aimHeight, catchCenterWidth, catchCenterHeight);

            float minRadius = catchCenterHeight * 3 / 10;
            float maxRadius = catchCenterHeight * 12 / 25;
            //测试
//            int minRadius = 0;
//            int maxRadius = 1000;
            if (isBig) {
                batch.draw(texReArray.get(2), changeX - changeRadiu, changeY - changeRadiu, changeRadiu * 2, changeRadiu * 2);
                if (changeRadiu >= height * 2 / 5) {
                    isBig = false;
                    if (catchNumber == 5) {
                        if (catchListener != null && isFirst) {
                            isFirst = false;
                            catchListener.onNumberFail(catchNumber);
                        }
                    }
                    catchNumber++;
                }
                if (!isStop) {
                    if (changeRadiu < maxRadius) {
                        num = 80;
                    } else {
                        num = 120;
                    }
                    radius = height * 2 / 5 / num;
                    changeRadiu += radius;
                } else if (!isCatch) {
                    //在开始捕捉界面之后运行
                    if (!isCatchTip) {
                        if (changeRadiu > minRadius && changeRadiu < maxRadius) {
                            batch.draw(texReArray.get(3), width / 2 - goodWidth / 2, height / 2 - goodHeight / 2, goodWidth, goodHeight);
                            if (catchListener != null && isFirst) {
                                isFirst = false;
                                catchListener.onSuccess();
                            }
                        } else {
                            batch.draw(texReArray.get(4), width / 2 - goodWidth / 2, height / 2 - goodHeight / 2, goodWidth, goodHeight);
                            if (catchListener != null && isFirst) {
                                isFirst = false;
                                catchListener.onFail();
                            }
                        }
                    }
                }
            } else {
                batch.draw(texReArray.get(2), changeX - changeRadiu, changeY - changeRadiu, changeRadiu * 2, changeRadiu * 2);
                if (changeRadiu <= 0) {
                    isBig = true;
                }
                if (!isStop) {
                    if (changeRadiu >= ((minRadius + maxRadius) / 2 - 3) && changeRadiu <= ((minRadius + maxRadius) / 2 + 3) && first) {
                        batch.draw(texReArray.get(3), width / 2 - goodWidth / 2, height / 2 - goodHeight / 2, goodWidth, goodHeight);
                        if (catchListener != null && first) {
                            catchListener.onFirst();
                        }
                    } else {
                        if (changeRadiu < maxRadius) {
                            num = 80;
                        } else {
                            num = 120;
                        }
                        radius = height * 2 / 5 / num;
                        changeRadiu -= radius;
                    }
                } else if (!isCatch) {
                    if (!isCatchTip) {
                        if (changeRadiu > minRadius && changeRadiu < maxRadius) {
                            batch.draw(texReArray.get(3), width / 2 - goodWidth / 2, height / 2 - goodHeight / 2, goodWidth, goodHeight);
                            if (catchListener != null && isFirst) {
                                isFirst = false;
                                catchListener.onSuccess();
                            }
                        } else {
                            batch.draw(texReArray.get(4), width / 2 - goodWidth / 2, height / 2 - goodHeight / 2, goodWidth, goodHeight);
                            if (catchListener != null && isFirst) {
                                isFirst = false;
                                catchListener.onFail();
                            }
                        }
                    }
                }
            }
        } else {
            //绘制遮罩
            batch.draw(successTexRe.get(2), 0, 0, width, height);
            if (isSuccess) {
                if (isOpen) {
                    batch.draw(openTexRe.get(0), 0.07f * width, height * 0.11f, width * 0.86f, height * 0.79f);
                    if (isWin) {
                        if (result == null)
                            return;
                        //从顶部向下绘制
                        batch.draw(openTexRe.get(1), width / 2 - openTexRe.get(1).getRegionWidth() * 1.5f / 2, mFloat - openTexRe.get(1).getRegionHeight() / 2, getOriginX(), getOriginY(), openTexRe.get(1).getRegionWidth(), openTexRe.get(1).getRegionHeight(), 1.5f, 1.5f, getRotation());

                        float fontWidth1 = ScreenUtil.getLength(ScreenUtil.dp2px(18), result.getMain());
                        if (lazyBitmapFont1 == null)
                            lazyBitmapFont1 = new LazyBitmapFont(ScreenUtil.dp2px(18), Color.WHITE);
                        lazyBitmapFont1.draw(batch, result.getMain(), width / 2 - fontWidth1 / 2, mFloat - openTexRe.get(1).getRegionHeight(), width * 0.707f, Align.left, true);

                        batch.draw(openTexRe.get(2), width / 2 - openTexRe.get(2).getRegionWidth() / 2, mFloat - openTexRe.get(1).getRegionHeight() - ScreenUtil.dp2px(18) - 15, openTexRe.get(2).getRegionWidth(), openTexRe.get(2).getRegionHeight());

                        float fontWidth2 = ScreenUtil.getLength(ScreenUtil.dp2px(12), "请到我的-优惠券里查看");
                        if (lazyBitmapFont2 == null)
                            lazyBitmapFont2 = new LazyBitmapFont(ScreenUtil.dp2px(12), Color.WHITE);

                        lazyBitmapFont2.draw(batch, "请到我的-优惠券里查看", width / 2 - fontWidth2 / 2, mFloat - openTexRe.get(1).getRegionHeight() - ScreenUtil.dp2px(18) - openTexRe.get(2).getRegionHeight() - 25, width * 0.707f, Align.left, true);
                        lazyBitmapFont2.draw(batch, result.getShopName(), mX + pictureMeter * 9 / 8, bottomHeight + pictureMeter, width * 0.707f, Align.left, true);


                        lazyBitmapFont2.draw(batch, "门店信息", mX, bottomHeight + pictureMeter * 7 / 4, width * 0.707f, Align.left, true);

                        if (lazyBitmapFont3 == null)
                            lazyBitmapFont3 = new LazyBitmapFont(ScreenUtil.dp2px(10), Color.WHITE);
                        lazyBitmapFont3.draw(batch, "地址:" + result.getPosition(), mX, bottomHeight, width * 0.707f, Align.left, true);
                        lazyBitmapFont3.draw(batch, result.getDesc(), mX + pictureMeter * 9 / 8, bottomHeight + pictureMeter / 2, width * 0.707f, Align.left, true);
                        lazyBitmapFont3.draw(batch, "离你0.2KM", mX + pictureMeter * 9 / 8, bottomHeight + pictureMeter * 3 / 4, width * 0.707f, Align.left, true);

                        if (null != result && null != result.getImgUrl() && texture == null) {
                            NetUtil.getInstance().getPicture(result.getImgUrl(), new Net.HttpResponseListener() {
                                @Override
                                public void handleHttpResponse(Net.HttpResponse httpResponse) {
                                    // 获取响应状态
                                    HttpStatus httpStatus = httpResponse.getStatus();

                                    if (httpStatus.getStatusCode() == 200) {
                                        // 请求成功
                                        Gdx.app.error("http", "请求成功");

                                        // 以字节数组的方式获取响应内容
                                        final byte[] result = httpResponse.getResult();

                                        // 还可以以流或字符串的方式获取
                                        // httpResponse.getResultAsStream();
                                        // httpResponse.getResultAsString();

                                            /*
                                             * 在响应回调中属于其他线程, 获取到响应结果后需要
                                             * 提交到 渲染线程（create 和 render 方法执行所在线程） 处理。
                                             */
                                        Gdx.app.postRunnable(new Runnable() {
                                            @Override
                                            public void run() {
                                                // 把字节数组加载为 Pixmap
                                                Pixmap pixmap = new Pixmap(result, 0, result.length);
                                                // 把 pixmap 加载为纹理
                                                texture = new Texture(pixmap);
                                                // pixmap 不再需要使用到, 释放内存占用
                                                pixmap.dispose();
                                            }
                                        });
                                    } else {
                                        Gdx.app.error("http", "请求失败, 状态码: " + httpStatus.getStatusCode());
                                    }
                                }

                                @Override
                                public void failed(Throwable t) {
                                    Gdx.app.error("http", t.getMessage());
                                }

                                @Override
                                public void cancelled() {
                                    Gdx.app.error("http", "请求取消");
                                }
                            });
                        } else {
                            if (texture != null)
                                batch.draw(texture, mX, bottomHeight + pictureMeter / 4, pictureMeter, pictureMeter);
                        }
                    } else {
//                        float length1 = ScreenUtil.getLength(ScreenUtil.dp2px(18), "运气还差那么一点点");
//                        if (lazyBitmapFont1 == null)
//                            lazyBitmapFont1 = new LazyBitmapFont(ScreenUtil.dp2px(18), Color.WHITE);
//                        lazyBitmapFont1.draw(batch, "运气还差那么一点点", width / 2 - length1 / 2, height * 0.65f, width, Align.left, true);
                        batch.draw(openTexRe.get(3), width / 2 - openFailWidth / 2, height * 0.65f, openFailWidth, openFailHeight);

                        float length2 = ScreenUtil.getLength(ScreenUtil.dp2px(18), "锦囊空空如也");
                        if (lazyBitmapFont5 == null)
                            lazyBitmapFont5 = new LazyBitmapFont(ScreenUtil.dp2px(18), Color.WHITE);
                        lazyBitmapFont5.draw(batch, "锦囊空空如也", width / 2 - length2 / 2, height * 0.55f, width, Align.left, true);

                        batch.draw(openTexRe.get(2), width / 2 - openTexRe.get(2).getRegionWidth() / 2, height * 0.55f - ScreenUtil.dp2px(22) - 15, openTexRe.get(2).getRegionWidth(), openTexRe.get(2).getRegionHeight());

                        float length3 = ScreenUtil.getLength(ScreenUtil.dp2px(14), "努力就有收获，再接再厉吧！");
                        if (lazyBitmapFont4 == null)
                            lazyBitmapFont4 = new LazyBitmapFont(ScreenUtil.dp2px(14), Color.WHITE);
                        lazyBitmapFont4.draw(batch, "努力就有收获，再接再厉吧！", width / 2 - length3 / 2, height * 0.55f - ScreenUtil.dp2px(22) - openTexRe.get(2).getRegionHeight() - ScreenUtil.dp2px(18), width, Align.left, true);
                    }

                } else {
                    batch.draw(successTexRe.get(0), 0, height * 0.756f, titleWidth, titleHeight);
                    batch.draw(successTexRe.get(1), width / 2 - centerWidth / 2, height / 2 - centerHeight / 2, centerWidth, centerHeight);
                }
            } else {
//                batch.draw(mKeyFrames[2], width / 2 - mKeyFrames[2].getRegionWidth() / 2, height / 2 - mKeyFrames[2].getRegionHeight() / 2, mKeyFrames[2].getRegionWidth(), mKeyFrames[2].getRegionHeight());
//                float fontWidth3 = ScreenUtil.getLength(ScreenUtil.dp2px(12), "点击任意位置继续");
//                if (lazyBitmapFont2 == null)
//                    lazyBitmapFont2 = new LazyBitmapFont(ScreenUtil.dp2px(12), Color.WHITE);
//                lazyBitmapFont2.draw(batch, "点击任意位置继续", width / 2 - fontWidth3 / 2, height / 2 - mKeyFrames[2].getRegionHeight() / 2 - 15, width, Align.left, true);

//                if (Gdx.input.isTouched()) {
                if (catchListener != null && isCatchFirst) {
                    isCatchFirst = false;
                    catchListener.onTouched(0);
                }
//                }
            }
        }
        if (isCatch) {
            float i = (width * 1f) / (mKeyFrames[0].getRegionWidth() * 1f);
            float regionHeight = mKeyFrames[0].getRegionHeight() * i;
            //绘制遮罩
            batch.draw(successTexRe.get(2), 0, 0, width, height);
            batch.draw(mKeyFrames[0], 0, height / 2 - regionHeight / 2, width, regionHeight);
        }
        if (isCatchTip) {
            //绘制遮罩
            batch.draw(successTexRe.get(2), 0, 0, width, height);
            batch.draw(mKeyFrames[1], width / 2 - mKey2Width / 2, height / 2 - mKey2Height / 2, mKey2Width, mKey2Height);
            batch.draw(mKeyFrames[2], width / 2 - mKey3Width / 2, height / 2 - mKey2Height / 2 + mKey2Height / 5, mKey3Width, mKey3Height);
            float fontWidth3 = ScreenUtil.getLength(ScreenUtil.dp2px(12), "点击任意位置继续");
            if (lazyBitmapFont2 == null)
                lazyBitmapFont2 = new LazyBitmapFont(ScreenUtil.dp2px(12), Color.WHITE);
            lazyBitmapFont2.draw(batch, "点击任意位置继续", width / 2 - fontWidth3 / 2, height / 2 - mKey2Height / 2 - 15, width, Align.left, true);

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

    public void setData(Result result) {
        this.result = result;
    }

    public void initResources() {
        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_bg.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_center.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_circle.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_good.png")));
        texReArray.add(new TextureRegion((Texture) assetManager.get("catch_miss.png")));

        mScale = (float) width / (float) texReArray.get(0).getRegionWidth();
        catchCenterWidth = width * 0.4f;
        catchCenterHeight = width * 0.4f;
        goodWidth = texReArray.get(3).getRegionWidth() * mScale;
        goodHeight = texReArray.get(3).getRegionHeight() * mScale;

        successTexRe = new ArrayList<>();
        successTexRe.add(new TextureRegion((Texture) assetManager.get("success_title.png")));
        successTexRe.add(new TextureRegion((Texture) assetManager.get("success_center.png")));
        successTexRe.add(new TextureRegion((Texture) assetManager.get("cover.png")));

        titleWidth = width * 0.839f;
        titleHeight = height * 0.117f;
        centerWidth = successTexRe.get(1).getRegionWidth() * mScale;
        centerHeight = successTexRe.get(1).getRegionHeight() * mScale;

        openTexRe = new ArrayList();
        openTexRe.add(new TextureRegion((Texture) assetManager.get("beed_open_bg.png")));
        openTexRe.add(new TextureRegion((Texture) assetManager.get("open_title.png")));
        openTexRe.add(new TextureRegion((Texture) assetManager.get("open_line.png")));
        openTexRe.add(new TextureRegion((Texture) assetManager.get("open_fail.png")));

        openFailWidth = (float) openTexRe.get(3).getRegionWidth() * 2;
        openFailHeight = (float) openTexRe.get(3).getRegionHeight() * 2;

        mKeyFrames[0] = new TextureRegion((Texture) assetManager.get("catch_tip.png"));
        mKeyFrames[1] = new TextureRegion((Texture) assetManager.get("find_tip.png"));
        mKeyFrames[2] = new TextureRegion((Texture) assetManager.get("catch_tip_text.png"));

        mKey2Width = mKeyFrames[1].getRegionWidth() * mScale;
        mKey2Height = mKeyFrames[1].getRegionHeight() * mScale;
        mKey3Width = mKeyFrames[2].getRegionWidth() * mScale;
        mKey3Height = mKeyFrames[2].getRegionHeight() * mScale;
    }

    @Override
    public void clear() {
        if (texture != null) {
            texture.dispose();
        }
        for (int i = 0; i < texReArray.size(); i++) {
            texReArray.get(i).getTexture().dispose();
        }
        for (int i = 0; i < openTexRe.size(); i++) {
            openTexRe.get(i).getTexture().dispose();
        }
        for (int i = 0; i < successTexRe.size(); i++) {
            successTexRe.get(i).getTexture().dispose();
        }
        for (int i = 0; i < mKeyFrames.length; i++) {
            mKeyFrames[i].getTexture().dispose();
        }
        if (lazyBitmapFont1 != null)
            lazyBitmapFont1.dispose();
        if (lazyBitmapFont2 != null)
            lazyBitmapFont2.dispose();
        if (lazyBitmapFont3 != null)
            lazyBitmapFont3.dispose();
        if (lazyBitmapFont4 != null)
            lazyBitmapFont4.dispose();
        if (lazyBitmapFont5 != null)
            lazyBitmapFont5.dispose();
    }
}
