package com.rtmap.game.util;

import android.widget.Toast;

import com.rtmap.game.AndroidLauncher;

/**
 * Created by yxy on 2017/3/16.
 */
public class ToastUtil {
    public static synchronized void toast(String message) {
        Toast.makeText(AndroidLauncher.getInstance().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
