package com.rtmap.game.text;

/**
 * Created by yxy on 2017/2/24.
 */

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.rtmap.game.util.FontUtil;

public class Font {
    public String chars;
    public int size;
    public BitmapFont font;

    public boolean include(char c, int fs) {
        return fs == size && chars.indexOf(new String(new char[]{c})) != -1;
    }

    public static Font generateFont(String chars, int size, int boderWidth, Color boderColor) {
        Font f = new Font();
        f.chars = chars;
        f.size = size;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = chars;
        if (boderWidth != 0) {
            parameter.borderWidth = boderWidth;
            parameter.borderColor = boderColor;
        }
        f.font = FontUtil.generator.generateFont(parameter);//generator只需要一个即可，我写在了另一个叫FontUtil的类里，各位可以自己创建一个公共的generator对象。
        return f;
    }

}
