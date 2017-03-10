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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/21.
 */
public class DetailActor extends Actor {
    private float oriX;
    private float oriY;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private boolean isOpen = false;
    private Texture texture1;
    private Result result;
    private Texture texture;
    private LazyBitmapFont lazyBitmapFont3;
    private LazyBitmapFont lazyBitmapFont2;
    private LazyBitmapFont lazyBitmapFont1;
    private float qrWidth = 400;
    private List<TextureRegion> beedList = new ArrayList<>();

    public DetailActor(AssetManager assetManager) {
        super();
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        initResouces();
        oriY = height * 0.11f;
        oriX = 0.07f * width;
    }

    public void setListener(final StartOnClickListener startOnClickListener) {
        listener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (startOnClickListener != null) {
                    startOnClickListener.onClick();
                }
            }
        };
        addListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (isOpen) {
            if (result == null)
                return;
            //从顶部向下绘制
            batch.draw(beedList.get(0), oriX, oriY, width * 0.86f, height * 0.79f);
            float fontWidth1 = ScreenUtil.getLength(ScreenUtil.dp2px(18), result.getMain());
            if (lazyBitmapFont1 == null)
                lazyBitmapFont1 = new LazyBitmapFont(ScreenUtil.dp2px(18), Color.WHITE);
            lazyBitmapFont1.draw(batch, "满100减40", width / 2 - fontWidth1 / 2, height * 0.725f - 60, width * 0.707f, Align.left, true);

            batch.draw(beedList.get(1), width / 2 - beedList.get(1).getRegionWidth() / 2, height * 0.725f - 80, beedList.get(1).getRegionWidth(), beedList.get(1).getRegionHeight());

            float fontWidth2 = ScreenUtil.getLength(ScreenUtil.dp2px(12), "店内部分商品参加活动");
            if (lazyBitmapFont2 == null)
                lazyBitmapFont2 = new LazyBitmapFont(ScreenUtil.dp2px(12), Color.WHITE);
            lazyBitmapFont2.draw(batch, "店内部分商品参加活动", width / 2 - fontWidth2 / 2, height * 0.725f - 100, width * 0.707f, Align.left, true);
            lazyBitmapFont2.draw(batch, "12627238383333333", width / 2 - qrWidth / 2, height * 0.377f - 20, width * 0.707f, Align.left, true);
            lazyBitmapFont2.draw(batch, "门店信息", width * 0.293f, height * 0.287f + ScreenUtil.dp2px(10) * 8, width * 0.707f, Align.left, true);

            if (lazyBitmapFont3 == null)
                lazyBitmapFont3 = new LazyBitmapFont(ScreenUtil.dp2px(10), Color.WHITE);
            lazyBitmapFont3.draw(batch, "11111111111111111", width / 2 - qrWidth / 2, height * 0.377f - 20, width, Align.left, true);
            float length = ScreenUtil.getLength(ScreenUtil.dp2px(10), "星巴克西單大悅城");
            lazyBitmapFont3.draw(batch, "星巴克西單大悅城", width / 2 - length / 2, height * 0.725f - 10, width * 0.707f, Align.left, true);
            float length2 = ScreenUtil.getLength(ScreenUtil.dp2px(10), "兑换地址：" + result.getShopName());
            lazyBitmapFont3.draw(batch, "兑换地址：" + result.getShopName(), width / 2 - length2 / 2, height * 0.026f + 30, width * 0.707f, Align.left, true);
            float length3 = ScreenUtil.getLength(ScreenUtil.dp2px(10), "有效期限: 2016.09.30-2017.06.30");
            lazyBitmapFont3.draw(batch, "有效期限: 2016.09.30-2017.06.30", width / 2 - length3 / 2, height * 0.026f, width * 0.707f, Align.left, true);

            if (null != result && null != result.getImgUrl() && texture1 == null) {
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
                                    texture1 = new Texture(pixmap);
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
                if (texture1 != null)
                    batch.draw(texture1, width / 2 - qrWidth / 2, height * 0.377f, qrWidth, qrWidth);
            }

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
                    batch.draw(texture, width / 2 - 50, height * 0.725f, 100, 100);
            }
        }
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    private void initResouces() {
        beedList.add(new TextureRegion((Texture) assetManager.get("open_bg.png")));
        beedList.add(new TextureRegion((Texture) assetManager.get("open_line.png")));
    }

    public void setResult(Result item) {
        this.result = item;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        for (int i = 0; i < beedList.size(); i++) {
            beedList.get(i).getTexture().dispose();
        }
        if (lazyBitmapFont1 != null) {
            lazyBitmapFont1.dispose();
        }
        if (lazyBitmapFont2 != null) {
            lazyBitmapFont2.dispose();
        }
        if (lazyBitmapFont3 != null) {
            lazyBitmapFont3.dispose();
        }
    }
}
