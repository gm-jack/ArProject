package com.rtmap.game.util;

import android.util.TypedValue;

import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.rtmap.game.AndroidLauncher;

/**
 * Created by yxy on 2017/2/27.
 */
public class ScreenUtil {
    public static int dp2px(float dpsize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsize, AndroidLauncher.getInstance().getResources().getDisplayMetrics());
    }

    public static float getLength(int size, String str) {
        boolean isSize = true;
        float length = 0;
        char[] chars = str.toCharArray();
        char[] chars1 = FreeTypeFontGenerator.DEFAULT_CHARS.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            for (int j = 0; j < chars1.length; j++) {
                if (chars[i] == chars1[j]) {
                    length += size / 2;
                    isSize = false;
                }
            }
            if (isSize) {
                length += size;
            }
            isSize = true;
        }
//        Gdx.app.error("length", "length   ==  " + length);
//        switch (chars.length) {
//            case 0:
//                length = 0;
//                break;
//            case 1:
//                length = size;
//                break;
//            case 2:
//                length = chars.length * size + 1;
//                break;
//            default:
//                length = chars.length * size;
//                break;
//        }
        return length;
    }
}
