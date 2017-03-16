package com.rtmap.game.actor;

import android.graphics.Color;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedItemOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.PixmapUtil;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/21.
 */
public class BeedItemActor extends Actor {

    private Result result;
    private int width;
    private int height;
    private AssetManager assetManager;
    private InputListener listener;
    private List<TextureRegion> normal = new ArrayList<>();
    private float scale;
    private float realHeight;
    private boolean isUse = false;
    private Texture texture;
    private LazyBitmapFont lazyBitmapFont1;
    private LazyBitmapFont lazyBitmapFont2;
    private float radius = 0;

    public BeedItemActor(AssetManager assetManager, Result result) {
        super();
        this.result = result;
        this.assetManager = assetManager;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        initResources();

        radius = width * 0.173f;
    }

    private void initResources() {
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_bg.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_nouse.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_use.png")));
        normal.add(new TextureRegion((Texture) assetManager.get("beed_item_line.png")));


        scale = (float) width / (float) normal.get(0).getRegionWidth();
        realHeight = normal.get(0).getRegionHeight() * scale;
    }

    public void setListener(final BeedItemOnClickListener beedItemOnClickListener, int item) {
        listener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (beedItemOnClickListener != null) {
                    beedItemOnClickListener.onClick(BeedItemActor.this, result);
                }
            }
        };
        addListener(listener);
    }

    public void removeThisListener() {
        removeListener(listener);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (!isVisible()) {
            return;
        }
        if (normal.size() <= 0) return;
        batch.draw(normal.get(0), 0, getY() + realHeight * 0.04f, getOriginX(), getOriginY(), width, realHeight, getScaleX(), getScaleY(), getRotation());
        if (isUse)
            batch.draw(normal.get(2), width - normal.get(2).getRegionWidth() * 3 / 2, getY() + realHeight * 0.3f / 2 - normal.get(2).getRegionHeight() / 2 + realHeight * 0.06f, getOriginX(), getOriginY(), normal.get(2).getRegionWidth(), normal.get(2).getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        else
            batch.draw(normal.get(1), width - normal.get(1).getRegionWidth() * 3 / 2, getY() + realHeight * 0.3f / 2 - normal.get(1).getRegionHeight() / 2 + realHeight * 0.06f, getOriginX(), getOriginY(), normal.get(1).getRegionWidth(), normal.get(1).getRegionHeight(), getScaleX(), getScaleY(), getRotation());
        batch.draw(normal.get(3), 0, getY() + realHeight * 0.3f, getOriginX(), getOriginY(), width, normal.get(3).getRegionHeight(), getScaleX(), getScaleY(), getRotation());

        if (null != result && null != result.getImgUrl() && texture == null) {
            Gdx.app.error("http", "请求图片   " + (texture == null));
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
                                int min = Math.min(pixmap.getWidth(), pixmap.getHeight());
                                texture = new Texture(PixmapUtil.createRoundedPixmap(pixmap, min / 2, (int) radius, (int) radius));
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
                batch.draw(texture, width * 0.13f, getY() + realHeight * 0.3f + width * 0.04f, radius, radius);
        }
        if (lazyBitmapFont1 == null)
            lazyBitmapFont1 = new LazyBitmapFont(ScreenUtil.dp2px(11), com.badlogic.gdx.graphics.Color.WHITE);
        lazyBitmapFont1.draw(batch, "有效期限: 2016.09.30-2017.06.30", width * 0.13f, getY() + realHeight * 0.3f / 2 + ScreenUtil.dp2px(11) / 2 + realHeight * 0.04f, width, Align.left, false);
        lazyBitmapFont1.draw(batch, "请到适用门店兑换", width * 0.13f + radius + width * 0.04f, getY() + realHeight / 2, width, Align.left, false);

        if (lazyBitmapFont2 == null)
            lazyBitmapFont2 = new LazyBitmapFont(ScreenUtil.dp2px(15), com.badlogic.gdx.graphics.Color.WHITE);
        lazyBitmapFont2.draw(batch, result.getMain(), width * 0.13f + radius + width * 0.04f, getY() + realHeight * 0.65f, width, Align.left, false);
    }


    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        if (lazyBitmapFont1 != null)
            lazyBitmapFont1.dispose();
        if (lazyBitmapFont2 != null)
            lazyBitmapFont2.dispose();
        for (int i = 0; i < normal.size(); i++) {
            if (normal.get(i) != null)
                normal.get(i).getTexture().dispose();
        }
    }

    public float getRealHeight() {
        return realHeight;
    }
}
