package com.rtmap.game.util;

import android.content.Context;
import android.util.TypedValue;

import com.rtmap.game.AndroidLauncher;

/**
 * Created by yxy on 2017/2/27.
 */
public class ScreenUtil {
    public static int dp2px(float dpsize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpsize, AndroidLauncher.getInstance().getResources().getDisplayMetrics());
    }
}
