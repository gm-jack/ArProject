/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import jp.co.cyberagent.android.gpuimage.util.AnimEndListener;
import jp.co.cyberagent.android.gpuimage.util.HuaweiUtil;

public class GPUImageFilter {
    public static final String NO_FILTER_VERTEX_SHADER = "" +
            "uniform mat4 uMVPMatrix;\n" +
            "uniform float uMatrix;\n" +
            "varying float matrix;\n" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = uMVPMatrix * position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    matrix = uMatrix;\n" +
            "}";
    public static final String NO_FILTER_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "}";

    private final LinkedList<Runnable> mRunOnDraw;
    private final String mVertexShader;
    private String mFragmentShader;
    protected int mGLProgId;
    protected int mGLAttribPosition;
    protected int mGLUniformTexture;
    protected int mGLAttribTextureCoordinate;
    protected int mOutputWidth;
    protected int mOutputHeight;
    private boolean mIsInitialized;
    private FloatBuffer verticalsBuffer;
    private float mChangeW = 0;
    private boolean isCat = false;
    private AnimEndListener listener;
    private boolean isFirst = true;
    private int muMVPMatrixHandle;
    //    private float[] mvpMatrix = new float[16];
    private int mCircleProgram;
    private float[] projectionMatrix = new float[16];
    private float[] projectionMatrixs = new float[]{1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1};
    private int mMatrixHandle;
    private Context mContext;

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public GPUImageFilter(final String vertexShader, final String fragmentShader) {
        mRunOnDraw = new LinkedList<Runnable>();
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public final void init() {
        onInit();
        mIsInitialized = true;
        onInitialized();
    }

    public void onInit() {
        mGLProgId = OpenGlUtils.loadProgram(mVertexShader, mFragmentShader);
        mCircleProgram = OpenGlUtils.loadProgram(mVertexShader, circleFragmentShaderCode);
        mGLAttribPosition = GLES20.glGetAttribLocation(mGLProgId, "position");
        mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture");
        mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mGLProgId,
                "inputTextureCoordinate");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mGLProgId, "uMVPMatrix");
        mMatrixHandle = GLES20.glGetUniformLocation(mGLProgId, "uMatrix");

        mIsInitialized = true;
    }

    public void onInitialized() {
        float[] verticals = new float[120 * 3 * 3];
        float x = 0;
        float y = 0;
        float z = 0;
        float r = 1;
        int index = -1;
        for (int i = 3; i <= 360; i = i + 3) {
            double d1 = i * Math.PI / 180;
            verticals[++index] = 0;
            verticals[++index] = 0;
            verticals[++index] = 0;

            verticals[++index] = (float) (x + r * Math.cos(d1 - 3 * Math.PI / 180));
            verticals[++index] = (float) (y + r * Math.sin(d1 - 3 * Math.PI / 180));
            verticals[++index] = 0;

            verticals[++index] = (float) (x + r * Math.cos(d1));
            verticals[++index] = (float) (y + r * Math.sin(d1));
            verticals[++index] = 0;
        }
        verticalsBuffer = ByteBuffer.allocateDirect(verticals.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(verticals);
        verticalsBuffer.position(0);
    }

    public final void destroy() {
        mIsInitialized = false;
//        GLES20.glDeleteProgram(mCircleProgram);
        GLES20.glDeleteProgram(mGLProgId);
        onDestroy();
    }

    public void onDestroy() {
    }

    //根据屏幕的width 和 height 创建投影矩阵
    public void onOutputSizeChanged(final int width, final int height) {
        float aspectRatio = (float) width / (float) height;
        if (HuaweiUtil.isHUAWEI() && mContext != null) {
            aspectRatio = (float) width / (float) (HuaweiUtil.getDpi(mContext));
        }
        Matrix.orthoM(projectionMatrix, 0, -1f * aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        mOutputWidth = width;
        mOutputHeight = height;
    }

    public void onDraw(final int textureId, final FloatBuffer cubeBuffer,
                       final FloatBuffer textureBuffer) {
        runPendingOnDrawTasks();
        if (!mIsInitialized) {
            return;
        }

        GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);

        GLES20.glUseProgram(mGLProgId);
//        if (isCat) {
//            //启用剪裁测试
//            GLES20.glEnable(GL10.GL_SCISSOR_TEST);
//            if (mChangeW < mOutputWidth / 2 && mChangeH < mOutputHeight / 2) {
//                //设置区域
//                GLES20.glScissor((int) (mOutputWidth / 2 - mChangeW), (int) (mOutputHeight / 2 - mChangeH), (int) mChangeW * 2, (int) mChangeH * 2);
//                mChangeW += mMScaleW;
//                mChangeH += mMScaleH;
//                Log.e("filter", "mChangeW   " + mChangeW + "   mChangeH   " + mChangeH);
//            } else {
//                mChangeW = mOutputWidth / 2;
//                mChangeH = mOutputHeight / 2;
//            }
//        }
        if (isCat)
            if (mChangeW < 1) {
                GLES20.glUniform1f(mMatrixHandle, mChangeW);
                mChangeW += 0.03;
                GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, projectionMatrix, 0);
            } else {
                if (listener != null) {
                    listener.animEnd();
                }
                mChangeW = 0;
                isCat = false;
                GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, projectionMatrixs, 0);
            }
        cubeBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);

        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
//        if (isCat) {
//            //禁用剪裁测试
//            GLES20.glDisable(GL10.GL_SCISSOR_TEST);
//            if (mChangeW >= mOutputWidth / 2 && mChangeH >= mOutputHeight / 2) {
//                if (listener != null) {
//                    listener.animEnd();
//                }
//                isCat = false;
//                mChangeW = 0;
//                mChangeH = 0;
//            }
//        }
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    protected void onDrawArraysPre() {
    }

    protected void runPendingOnDrawTasks() {
        while (!mRunOnDraw.isEmpty()) {
            mRunOnDraw.removeFirst().run();
        }
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public int getOutputWidth() {
        return mOutputWidth;
    }

    public int getOutputHeight() {
        return mOutputHeight;
    }

    public int getProgram() {
        return mGLProgId;
    }

    public int getAttribPosition() {
        return mGLAttribPosition;
    }

    public int getAttribTextureCoordinate() {
        return mGLAttribTextureCoordinate;
    }

    public int getUniformTexture() {
        return mGLUniformTexture;
    }

    protected void setInteger(final int location, final int intValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1i(location, intValue);
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1f(location, floatValue);
            }
        });
    }

    protected void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec3(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatArray(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glUniform1fv(location, arrayValue.length, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setPoint(final int location, final PointF point) {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                float[] vec2 = new float[2];
                vec2[0] = point.x;
                vec2[1] = point.y;
                GLES20.glUniform2fv(location, 1, vec2, 0);
            }
        });
    }

    protected void setUniformMatrix3f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void setUniformMatrix4f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void runOnDraw(final Runnable runnable) {
        synchronized (mRunOnDraw) {
            mRunOnDraw.addLast(runnable);
        }
    }

    public static String loadShader(String file, Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream ims = assetManager.open(file);

            String re = convertStreamToString(ims);
            ims.close();
            return re;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String circleFragmentShaderCode = "precision mediump float;"
            + "void main(){"
            + "gl_FragColor = vec4(1,0,0,1);"
            + "}";

    public void setCat(boolean cat, AnimEndListener listener) {
        this.isCat = cat;
        this.listener = listener;
        if (cat) {
            mChangeW = 0;
        }
    }
}
