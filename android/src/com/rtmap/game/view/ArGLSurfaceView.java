package com.rtmap.game.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.badlogic.gdx.Gdx;

/**
 * Created by yxy
 * on 2017/3/29.
 */

public class ArGLSurfaceView extends GLSurfaceView {

    public ArGLSurfaceView(Context context) {
        this(context, null);
    }

    public ArGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Gdx.app.error("view", "getMeasuredWidth   = " + getMeasuredWidth() + "   getMeasuredHeight  = " + getMeasuredHeight());
        setMeasuredDimension(200, 200);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Gdx.app.error("view", "onLayout()");
    }
}
