package com.rtmap.game.screen;

import android.content.Context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Group;
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
import com.rtmap.game.util.MD5Encoder;
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
        super();
        this.mGame = game;
        this.context = androidLauncher;
        //捕捉怪兽舞台
        catchStage = new CatchStage(new ScreenViewport());

        group3 = new Group();
        catchActor = new CatchActor(new AssetManager());
        catchActor.setPosition(0, 0);
        catchActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group3.addActor(catchActor);

        backActor = new BackActor(new AssetManager());
        group3.addActor(backActor);

        beedActor = new BeedActor(new AssetManager());
        group3.addActor(beedActor);

        coverActor = new CoverActor(new AssetManager());
        group3.addActor(coverActor);

        catActor = new CatActor(new AssetManager());
        group3.addActor(catActor);

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
                    setResult();
                }
            });
    }

    private void setResult() {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("http://182.92.31.114/rest/act/17888/18833720712").build();
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                catActor.removeListener();
                catActor.setIsShow(false);
//                try {
////                        HttpUtil.downloadFileByHttpConnection(result.getImgUrl(), MD5Encoder.encode(result.getImgUrl()));
//                    HttpUtil.downloadFileByHttpConnection("http://res.rtmap.com/image/prize_pic/2017-03/1488787034247.png", MD5Encoder.encode("http://res.rtmap.com/image/prize_pic/2017-03/1488787034247.png"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Gdx.app.error("http", httpResponse.getResultAsString());
                Result result = new Gson().fromJson(httpResponse.getResultAsString(), Result.class);
                if (null != result.getCode() && "0".equals(result.getCode())) {
                    isWin = true;
                    catchActor.setIsWin(isWin);
                    catchActor.setData(result);
                } else {
                    isWin = false;
                    catchActor.setIsWin(isWin);
                }
                catchActor.setIsOpen(true);
                if (!isWin) {
                    //添加再来一次按钮actor
                    againActor = new AgainActor(new AssetManager());
                    againActor.setListener(new AgainActor.AgainOnClickListener() {
                        @Override
                        public void againClick() {
                            mGame.showAimScreen(false);
                            CatchScreen.this.dispose();
                        }
                    });
                    group3.addActor(againActor);
                }
                //添加关闭按钮actor
                closeActor = new CloseActor(new AssetManager());
                closeActor.setListener(new BackOnClickListener() {
                    @Override
                    public void onClick() {
                        if (mGame != null)
                            mGame.showAimScreen(false);
                        CatchScreen.this.dispose();
                    }
                });
                group3.addActor(closeActor);
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
//                String http = HttpUtil.connInfo(HttpUtil.GET, "http://182.92.31.114/rest/act/17888/18833720712", new HttpUtil.HttpResultListener() {
//            @Override
//            public void success(String results) {
//                Gdx.app.error("http", results);
//
//            }
//
//            @Override
//            public void fail(int code, String result) {
//                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//            }
//        });
//        Gdx.app.error("http", http);
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
