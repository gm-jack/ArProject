package com.rtmap.game.screen;

import android.content.Context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
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
import com.rtmap.game.stage.CatchStage;
import com.rtmap.game.util.SPUtil;

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

    public CatchScreen(MyGame game, AndroidLauncher androidLauncher) {
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
                        mGame.showAimScreen();
                } else if (num == 1) {
                    catchActor.setIsCatchTip(false);
                    catchActor.setIsStop(false);
                }
            }
        });
    }

    private void setlistener() {
        backActor.setListener(new BackOnClickListener() {
            @Override
            public void onClick() {

            }
        });
        beedActor.setListener(new BeedOnClickListener() {
            @Override
            public void onClick() {
                //打开背包Stage
                Gdx.app.error("gdx", "打开背包");
                mGame.showBeedScreen(CatchScreen.this);
            }
        });
        catActor.setListener(new CatchOnClickListener() {

            @Override
            public void onCatchClick() {
                catchActor.setIsStop(stop);
                stop = !stop;
            }

            @Override
            public void onSuccessClick() {
                catActor.removeListener();
                catchActor.setIsOpen(true);
                catActor.setIsShow(false);
                setResult();
            }
        });
    }

    private void setResult() {
        isWin = true;
        catchActor.setIsWin(isWin);
        if (isWin) {

        } else {
            //添加再来一次按钮actor
            againActor = new AgainActor(new AssetManager());
            againActor.setListener(new AgainActor.AgainOnClickListener() {
                @Override
                public void againClick() {
                    mGame.showAimScreen();
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
                    mGame.showAimScreen();
                CatchScreen.this.dispose();
            }
        });
        group3.addActor(closeActor);
    }

    @Override
    public void render(float delta) {
        if (catchStage == null)
            return;


        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // 更新舞台逻辑
        catchStage.act();
        // 绘制舞台
        catchStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        catchActor.setIsStop(true);
        firstCatch = (boolean) SPUtil.get(context, "first_catch", true);
        Gdx.app.error("gdx", "firstCatch   " + firstCatch);
        catchActor.setIsFirst(firstCatch);
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
