package com.rtmap.game.screen;

import android.content.Context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.gson.Gson;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AgainActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CatActor;
import com.rtmap.game.actor.CatchActor;
import com.rtmap.game.actor.CloseActor;
import com.rtmap.game.actor.CoverActor;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.interfaces.CatchListener;
import com.rtmap.game.interfaces.CatchOnClickListener;
import com.rtmap.game.model.Result;
import com.rtmap.game.stage.CatchStage;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.MD5Encoder;
import com.rtmap.game.util.NetUtil;
import com.rtmap.game.util.SPUtil;
import com.rtmap.game.util.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class CatchScreen extends MyScreen {

    private Context context;
    private CatActor catActor;
    private float deltaSum;
    private MyGame mGame;
    //    private Texture mainBg;

    private BackActor backActor;
    private BeedActor beedActor;
    private Group group3;
    private Timer timer;

    private CatchStage catchStage;
    private CatchActor catchActor;

    private boolean stop = true;
    private CoverActor coverActor;
    private boolean isFirst = true;
    private boolean firstCatch;
    private CloseActor closeActor;
    private AgainActor againActor;
    private boolean isWin = false;
    private boolean isInit = true;

    public CatchScreen(MyGame game, AndroidLauncher androidLauncher) {
        super(game);
        this.mGame = game;
        this.context = androidLauncher;
        //捕捉怪兽舞台
        catchStage = new CatchStage(new ScreenViewport());

        group3 = new Group();
        catchActor = new CatchActor(mGame.asset);
        catchActor.setPosition(0, 0);
        catchActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group3.addActor(catchActor);

        backActor = new BackActor(mGame.asset);
        group3.addActor(backActor);

        beedActor = new BeedActor(mGame.asset);
        group3.addActor(beedActor);

        coverActor = new CoverActor(mGame.asset);
        group3.addActor(coverActor);

        catActor = new CatActor(mGame.asset);
        group3.addActor(catActor);
        //添加关闭按钮actor
        closeActor = new CloseActor(mGame.asset);
        group3.addActor(closeActor);
        //添加再来一次按钮actor
        againActor = new AgainActor(mGame.asset);
        group3.addActor(againActor);

        catchStage.addActor(group3);
    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(catchStage);
        if (isFirst && !firstCatch) {
            isFirst = false;
            setlistener();
        }
        if (catchActor != null)
            catchActor.setCatchListener(new CatchListener() {
                @Override
                public void onFirst() {
                    Gdx.app.error("gdx", "onFirst");
                    SPUtil.put(context, "first_catch", false);
                    coverActor.setIsFirst(true);
                    setlistener();
                }

                @Override
                public void onSuccess() {
                    Gdx.app.error("gdx", "onSuccess");
                    setStopRerder(true);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (firstCatch) {
                                SPUtil.put(context, "first_catch", false);
                                coverActor.setIsFirst(false);
                                catchActor.setIsFirst(false);
                            }
                            catActor.setIsCatch(false);
                            catchActor.setFail(true);
                            catchActor.setIsSuccess(true);
                        }
                    }, 500);
                }

                @Override
                public void onFail() {
                    Gdx.app.error("gdx", "onFail");
                    setStopRerder(true);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            catActor.setIsShow(false);
                            catchActor.setFail(true);
                            catchActor.setIsSuccess(false);
                        }
                    }, 500);
                }

                @Override
                public void onNumberFail(int number) {
                    Gdx.app.error("gdx", "onNumberFail");
                    setStopRerder(true);
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            catActor.setIsShow(false);
                            catchActor.setFail(true);
                        }
                    }, 1000);
                }

                @Override
                public void onTouched(int num) {
                    if (num == 0) {
                        if (mGame != null)
                            mGame.showAimScreen(true);
                    } else if (num == 1) {
                        catchActor.setIsCatchTip(false);
                        catchActor.setIsStop(false);
                    }
                }
            });
    }

    private void setlistener() {
        if (backActor != null)
            backActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {

                }
            });
        if (beedActor != null)
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    mGame.showBeedScreen(CatchScreen.this);
                }
            });
        if (catActor != null)
            catActor.setListener(new CatchOnClickListener() {

                @Override
                public void onCatchClick() {
                    catchActor.setIsStop(true);
//                    stop = !stop;
                }

                @Override
                public void onSuccessClick() {
                    Gdx.app.error("gdx", "onSuccessClick()");
                    setResult();
                }
            });
        if (closeActor != null)
            closeActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    if (mGame != null)
                        mGame.showAimScreen(false);
                    CatchScreen.this.dispose();
                }
            });
        if (againActor != null)
            againActor.setListener(new AgainActor.AgainOnClickListener() {
                @Override
                public void againClick() {
                    mGame.showAimScreen(false);
                    CatchScreen.this.dispose();
                }
            });
    }

    private void setResult() {
        NetUtil.getInstance().get(Contacts.WIN_NET, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String resultAsString = httpResponse.getResultAsString();
                Gdx.app.error("http", "handleHttpResponse  ==  " + resultAsString);
                catActor.removeListener();
                catActor.setIsShow(false);
                Result result = new Gson().fromJson(resultAsString, Result.class);
//                Result result = new Json().fromJson(Result.class, httpResponse.getResultAsStream());
                Gdx.app.error("http", "result  ==  " + result.toString());
                if ("0".equals(result.getCode())) {
                    isWin = true;
                    catchActor.setIsWin(isWin);
                    catchActor.setData(result);
                } else {
                    isWin = false;
                    catchActor.setIsWin(isWin);
                }
                catchActor.setIsOpen(true);
                againActor.setIsShow(!isWin);
                closeActor.setIsShow(true);
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("http", t.getMessage());
            }

            @Override
            public void cancelled() {
                Gdx.app.error("http", "请求取消");
            }
        });
//        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
//        Net.HttpRequest httpRequest = requestBuilder.newRequest().header("Content-Type",
//                "application/json;charset=UTF-8").method(Net.HttpMethods.GET).url("http://182.92.31.114/rest/act/17888/15210420307").build();
//        Gdx.net.sendHttpRequest(httpRequest, );
    }

    @Override
    public void render(float delta) {
        if (catchStage == null)
            return;
        super.render(delta);
        // 更新舞台逻辑
        catchStage.act();
        // 绘制舞台
        catchStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        setStopCamera(true);
        if (isInit) {
            Gdx.app.error("gdx", "CatchScreen resize");
            isInit = false;
            catchActor.setIsStop(true);
            firstCatch = (boolean) SPUtil.get(context, "first_catch", true);
            catchActor.setIsFirst(firstCatch);
            if (timer == null)
                timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    catchActor.setCatch(false);
                    catchActor.setIsCatchTip(firstCatch);
                    if (!firstCatch) {
                        catchActor.setIsStop(false);
                    }
                    initListener();
                }
            }, 1000);
        } else {
            initListener();
        }
        super.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // 场景被销毁时释放资源
        if (catchStage != null) {
            catchStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
