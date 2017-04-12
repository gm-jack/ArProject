package jp.co.cyberagent.android.gpuimage.util;

import android.graphics.SurfaceTexture;

/**
 * Created by yxy
 * on 2017/4/12.
 */

public class TextureUtil {
    private static TextureUtil mtextureUtil;

    public static synchronized TextureUtil getInstance() {
        if (mtextureUtil == null) {
            mtextureUtil = new TextureUtil();
        }
        return mtextureUtil;
    }

    public SurfaceTexture initSurfaceTexture(int id) {
        return new SurfaceTexture(id);
    }
}
