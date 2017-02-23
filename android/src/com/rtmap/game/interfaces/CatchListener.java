package com.rtmap.game.interfaces;

/**
 * Created by yxy on 2017/2/21.
 */
public interface CatchListener {

    void onSuccess();

    void onFail();

    void onNumberFail(int number);
}
