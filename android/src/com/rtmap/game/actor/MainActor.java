package com.rtmap.game.actor;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.FontUtil;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class MainActor extends Actor {
    private AssetManager asset;
    private List<TextureRegion> texReArray = new ArrayList();
    private int width;
    private int height;


    public MainActor(AssetManager assetManager) {
        super();
        this.asset = assetManager;
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
        if (texReArray.size() <= 0) return;
        batch.draw(texReArray.get(0), 0, 0, width, height);
        batch.draw(texReArray.get(1), width * 0.103f, height * 0.09f, width * 0.802f, height * 0.587f);
        float fontWidth1 = ScreenUtil.getLength(ScreenUtil.dp2px(18), "星巴克5元优惠券");
        LazyBitmapFont.setFontSize(ScreenUtil.dp2px(18), Color.WHITE).draw(batch, "星巴克5元优惠券", width / 2 - fontWidth1 / 2, height * 0.552f, width * 0.7f, Align.left, true);

//        float length2 = FontUtil.getLength(ScreenUtil.dp2px(12), "星巴克5元优惠券", 1);
//        FontUtil.draw(batch, "星巴克5元优惠券", ScreenUtil.dp2px(12), Color.WHITE, width / 2 - length2 / 2, height * 0.552f, width * 0.7f);

    }

    public void initResources() {
        asset.load("m_bg.png", Texture.class);
        asset.load("m_rule.png", Texture.class);
        asset.finishLoading();

        texReArray = new ArrayList<>();
        texReArray.add(new TextureRegion((Texture) asset.get("m_bg.png")));
        texReArray.add(new TextureRegion((Texture) asset.get("m_rule.png")));
    }

    @Override
    public void clear() {
        for (int i = 0; i < texReArray.size(); i++) {
            texReArray.get(i).getTexture().dispose();
        }
    }
}
