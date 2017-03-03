package com.rtmap.game.screen;

import android.content.Context;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MagicCamera;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AgainActor;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.actor.CatActor;
import com.rtmap.game.actor.CatchActor;
import com.rtmap.game.actor.CloseActor;
import com.rtmap.game.actor.CoverActor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.interfaces.CatchListener;
import com.rtmap.game.interfaces.CatchOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.stage.CatchStage;
import com.rtmap.game.util.SPUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yxy on 2017/2/20.
 */
public class CatchScreen extends MyScreen {

    private MyGame mGame;
    /**
     * 判断加载舞台
     * true:瞄准舞台；
     * false:捕捉舞台
     */
    private boolean isStage;
    private BackActor backActor;
    private BeedActor beedActor;

    /**
     * 捕捉舞台
     */
    private Context context;
    private CatActor catActor;
    private Group group3;
    private CatchStage catchStage;
    private CatchActor catchActor;
    private boolean stop = true;
    private CoverActor coverActor;
    private boolean firstCatch;
    private CloseActor closeActor;
    private AgainActor againActor;
    private boolean isWin = false;

    /**
     * 瞄准舞台
     */
    private Group group2;
    private Timer timer;
    private AimStage aimStage;
    private AimActor aimActor;
    //第一次开启场景初始
    private boolean isFirst = true;
    //判断是否第一次瞄准
    private boolean isAim = false;

    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assets;
    private MagicCamera camera;
    public Array<GameObject> instances = new Array<GameObject>();
    private Vector3 position = new Vector3();
    //判断是否失败返回的
    private boolean fail;
    private CameraInputController camController;
    //初始化瞄准舞台
    private boolean isAimInit = true;
    //初始化捕捉舞台
    private boolean isCatchInit = true;

    public CatchScreen(MyGame game, AndroidLauncher androidLauncher, boolean isStage, boolean fail) {
        this.mGame = game;
        this.context = androidLauncher;
        this.isStage = isStage;
        this.fail = fail;
        //捕捉怪兽舞台
        catchStage = new CatchStage(new ScreenViewport());
        backActor = new BackActor(new AssetManager());
        beedActor = new BeedActor(new AssetManager());

        group3 = new Group();
        catchActor = new CatchActor(new AssetManager());
        catchActor.setPosition(0, 0);
        catchActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group3.addActor(catchActor);

        coverActor = new CoverActor(new AssetManager());
        group3.addActor(coverActor);

        catActor = new CatActor(new AssetManager());
        group3.addActor(catActor);

        catchStage.addActor(group3);

        assets = new AssetManager();
        assets.load("data/ship.obj", Model.class);
        assets.finishLoading();
        //瞄准怪兽舞台
        aimStage = new AimStage(new ScreenViewport());

        group2 = new Group();
        aimActor = new AimActor(new AssetManager());
        aimActor.setPosition(0, 0);
        aimActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group2.addActor(aimActor);

        aimStage.addActor(group2);
    }

    @Override
    public void show() {

    }

    private void initListener() {
        if (isStage)
            Gdx.input.setInputProcessor(aimStage);
        else
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
                        setIsStage(true, true);
                        setIsAimInit(true);
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
        if (aimActor != null)
            aimActor.setAimListener(new AimListener() {
                @Override
                public void aimSuccess() {
                    setIsStage(false, false);
                    setIsCatchInit(true);
                }

                @Override
                public void aimFail() {
                    aimActor.setIsFind(false);
                }
            });
    }

    public void setIsStage(boolean isStage, boolean fail) {
//        if (isStage) {
//            if (catchStage != null) {
//                catchStage.dispose();
//            }
//        } else {
//            if (aimStage != null) {
//                aimStage.dispose();
//            }
//        }
        this.isStage = isStage;
        this.fail = fail;
    }

    public void setIsAimInit(boolean isAimInit) {
        this.isAimInit = isAimInit;
    }

    public void setIsCatchInit(boolean isCatchInit) {
        this.isCatchInit = isCatchInit;
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
                    setIsStage(true, false);
                    setIsAimInit(true);
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
                setIsStage(true, false);
                setIsAimInit(true);
//                if (mGame != null)
//                    mGame.showAimScreen(false);
            }
        });
        group3.addActor(closeActor);

    }

    @Override
    public void render(float delta) {
        if (instances.size > 0) {
            modelBatch.begin(camera);
            if (isVisible(camera, instances.get(0))) {
                modelBatch.render(instances, environment);
            }
            modelBatch.end();
        }
        initListener();
        switchStage();
    }

    private void switchStage() {
        if (isStage) {
            if (aimStage == null)
                return;
            if (isAimInit) {
                isAimInit = false;
                initAimStage();
            }
            camera.update();
            if (instances.size > 0) {
                Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                final GameObject instance = instances.get(0);
                instance.transform.getTranslation(position);
                position.add(instance.center);
                final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
                float dist2 = position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
                if (dist2 <= instance.radius * instance.radius) {

                    aimActor.addNumber();
                } else {
                    aimActor.subNumber();
                }
            }
            // 更新舞台逻辑
            aimStage.act();
            // 绘制舞台
            aimStage.draw();
        } else {
            if (catchStage == null)
                return;
            if (isCatchInit) {
                isCatchInit = false;
                initCatchStage();
            }
            // 更新舞台逻辑
            catchStage.act();
            // 绘制舞台
            catchStage.draw();
        }
    }

    protected boolean isVisible(final Camera cam, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
    }

    public void initAimStage() {
        group2.addActor(backActor);
        group2.addActor(beedActor);

        isAim = (boolean) SPUtil.get(context, "first_find", true);
        if (isAim) {
            aimActor.setIsTip(true);
            SPUtil.put(context, "first_find", false);
            if (timer == null)
                timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    aimActor.setIsTip(false);
                }
            }, 500);
        }
        aimActor.setIsFail(fail);
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new MagicCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(0, 0, 0);
        camera.lookAt(0, 0, 10);
        camera.far = 1000.0f;
        camera.near = 1f;

        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);

        doneLoading();
    }

    public void initCatchStage() {
        group3.addActor(backActor);
        group3.addActor(beedActor);

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
            }
        }, 1000);
    }

    @Override
    public void resize(int width, int height) {
        if (isFirst)
            initAimStage();
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
        if (aimStage != null) {
            aimStage.dispose();
        }
        if (timer != null) {
            timer.cancel();
        }
    }

    private void doneLoading() {
        Model ship = assets.get("data/ship.obj", Model.class);
        GameObject shipInstance = new GameObject(ship);
        shipInstance.transform.setToTranslation(0, 0, 5);
        instances.add(shipInstance);
        Gdx.app.error("gdx", "doneLoading()");
    }

    public static class GameObject extends ModelInstance {
        public final Vector3 center = new Vector3();
        public final Vector3 dimensions = new Vector3();
        public final float radius;

        private final static BoundingBox bounds = new BoundingBox();

        public GameObject(Model mode) {
            super(mode);
            calculateBoundingBox(bounds);
            bounds.getCenter(center);
            bounds.getDimensions(dimensions);
            radius = dimensions.len() / 2f;
        }
    }
}
