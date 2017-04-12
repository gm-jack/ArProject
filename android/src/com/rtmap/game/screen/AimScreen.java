package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.AnimationListener;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.util.SPUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class AimScreen extends MyScreen {
    private AndroidLauncher androidLauncher;
    private MyGame mGame;

    private BackActor backActor;
    private BeedActor beedActor;
    private boolean fail;
    private Group group2;
    private Timer timer;
    private AimStage aimStage;
    private AimActor aimActor;
    private boolean isFirst = true;
    private boolean isAim = false;
    private boolean isAnimation = true;
    private int nums = 0;

    public AimScreen(MyGame game, AndroidLauncher androidLauncher, ScreenViewport viewport) {
        super(game);
        this.mGame = game;
        this.androidLauncher = androidLauncher;
        //瞄准怪兽舞台
        aimStage = new AimStage(viewport);

        group2 = new Group();
        aimActor = new AimActor(mGame.asset, isAnimation);
        aimActor.setPosition(0, 0);
        aimActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group2.addActor(aimActor);

        backActor = new BackActor(mGame.asset, isAnimation);
        group2.addActor(backActor);

        beedActor = new BeedActor(mGame.asset, isAnimation);
        group2.addActor(beedActor);
        aimStage.addActor(group2);
    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(aimStage);

        if (isFirst) {
            isFirst = false;
            backActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    nums++;
                    if (nums > 2) {
                        nums = 0;
                    }
                    setModelNumber(nums);
//                    Gdx.app.error("gdx", "退出");
//                    if (mGame != null)
//                        mGame.showCatchScreen();
                }
            });
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
                    if (mGame != null)
                        mGame.showBeedScreen(AimScreen.this);
                }
            });
            aimActor.setAimListener(new AimListener() {
                @Override
                public void aimSuccess() {
                    if (mGame != null)
                        mGame.showCatchScreen();
                }

                @Override
                public void aimFail() {
                    aimActor.setIsFind(false);
                }
            });
            aimActor.setAnimationListener(new AnimationListener() {
                @Override
                public void startAnim(boolean isDistance) {

                }

                @Override
                public void endAnim() {
                    aimActor.setIsStartAnimation(false);
                    setIsAnim(true);
                }
            });
            setAnimationListener(new AnimationListener() {
                @Override
                public void startAnim(boolean isDistance) {
                    if (!aimActor.isAnimation()) {
                        if (isDistance) {
                            aimActor.setIsStartAnimation(true);
                        } else {
                            isAim = (boolean) SPUtil.get("first_find", true);
                            if (isAim) {
                                aimActor.setIsTip(true);
                                SPUtil.put("first_find", false);
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        aimActor.setIsTip(false);
                                    }
                                }, 1000);
                            }
                            aimActor.setIsStartAnimation(false);
                            setIsAnim(false);
                        }
                    }
                }

                @Override
                public void endAnim() {
                    setIsAnim(false);
                    aimActor.setIsStartAnimation(true);
                }
            });
        }
    }

    @Override
    public void addNumber() {
        if (aimActor != null)
            aimActor.addNumber();
    }

    @Override
    public void subNumber() {
        if (aimActor != null)
            aimActor.subNumber();
    }

    @Override
    public void render(float delta) {
        if (aimStage == null)
            return;
        super.render(delta);
        // 更新舞台逻辑
        aimStage.act();
        // 绘制舞台
        aimStage.draw();
    }

    public void setIsFail(boolean fail) {
        this.fail = fail;
        if (aimActor != null)
            aimActor.setIsFail(fail);
    }

    @Override
    public void resize(int width, int height) {
        setIsLineShow(false);
        setStopCamera(false);
        setStopRerder(false);
        initListener();
//        isAnimation = (boolean) SPUtil.get(Contacts.ANIM_IS_ANIMATION, true);
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
        if (aimStage != null) {
            aimStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
