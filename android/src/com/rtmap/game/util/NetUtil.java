package com.rtmap.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

/**
 * Created by yxy on 2017/3/9.
 */
public class NetUtil {

    private static volatile NetUtil netUtil = null;
    private HttpRequestBuilder requestBuilder;

    public static NetUtil getInstance() {
        if (netUtil == null) {
            synchronized (NetUtil.class) {
                if (netUtil == null) {
                    netUtil = new NetUtil();
                }
            }
        }
        return netUtil;
    }

    private NetUtil() {
        requestBuilder = new HttpRequestBuilder();
    }

    /**
     * get请求
     * "http://182.92.31.114/rest/act/17888/15210420307"
     *
     * @param url
     * @return
     */
    public void get(String url, Net.HttpResponseListener responseListener) {
        Net.HttpRequest httpRequest = requestBuilder.newRequest().header("Content-Type",
                "application/json;charset=UTF-8").method(Net.HttpMethods.GET).url(url).build();
        Gdx.net.sendHttpRequest(httpRequest, responseListener);
    }

    /**
     * 请求图片数据
     * "http://182.92.31.114/rest/act/17888/15210420307"
     *
     * @param url
     * @return
     */
    public void getPicture(String url, Net.HttpResponseListener responseListener) {
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();
        Gdx.net.sendHttpRequest(httpRequest, responseListener);
    }
}
