package com.rtmap.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.rtmap.game.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/24.
 */
public class FontUtil {
    public static List<Font> fontlist = new ArrayList<>();
    public static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/SourceHanSansCN-Normal.otf"));

    public static void draw(Batch sb, String str, int fontsize, Color color, float x, float y, float width, int paddinglr, int paddingtb, int boderWidth, Color boderColor) {
        sb.end();
        sb.begin();
        char[] dstr = StringUtil.dereplication(str).toCharArray();
        String addStr = "";
        for (char c : dstr) {
            boolean include = false;
            for (Font f : fontlist)
                if (f.include(c, fontsize))
                    include = true;
            if (!include)
                addStr += c;
        }
        if (addStr.length() > 0)
            fontlist.add(Font.generateFont(addStr, fontsize, boderWidth, boderColor));

        float currentX = x;
        float currentY = y;
        for (char c : str.toCharArray()) {
            if (currentX - x > width) {
                currentX = x;
                currentY -= fontsize + paddingtb;
            }
            BitmapFont f = getFont(fontsize, c);
            f.setColor(color);
            f.draw(sb, String.valueOf(c), currentX, currentY);
            currentX += fontsize + paddinglr;
        }
        sb.end();
        sb.begin();
    }

    public static float getLength(int fontsize, String str, float paddinglr) {
        float currentX = 0;
        for (char c : str.toCharArray()) {
            currentX += fontsize + paddinglr;
        }
        return currentX;
    }

    private static BitmapFont getFont(int fontsize, char str) {
        for (Font f : fontlist)
            if (f.include(str, fontsize))
                return f.font;
        return null;
    }

    public static void draw(Batch sb, String str, int fontsize, Color color, float x, float y,
                            float width) {
        draw(sb, str, fontsize, color, x, y, width, 2, 2, 0, Color.BLACK);
    }

    public static void draw(Batch sb, String str, int fontsize, Color color, float x, float y,
                            float width, int boderWidth, Color boderColor) {
        draw(sb, str, fontsize, color, x, y, width, 2, 2, boderWidth, boderColor);
    }

}
