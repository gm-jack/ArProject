package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MagicCamera;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.AimActor;
import com.rtmap.game.actor.BackActor;
import com.rtmap.game.actor.BeedActor;
import com.rtmap.game.interfaces.AimListener;
import com.rtmap.game.interfaces.BackOnClickListener;
import com.rtmap.game.interfaces.BeedOnClickListener;
import com.rtmap.game.stage.AimStage;
import com.rtmap.game.util.SPUtil;

import java.util.Timer;

/**
 * Created by yxy on 2017/2/20.
 */
public class AimScreen extends MyScreen {

    private boolean fail;
    private AndroidLauncher androidLauncher;
    private MyGame mGame;
    //    private Texture mainBg;

    private BackActor backActor;
    private BeedActor beedActor;
    private Group group2;
    private Timer timer;
    private AimStage aimStage;
    private AimActor aimActor;
    private boolean isFirst = true;
    private boolean isAim = false;

    private ModelBatch modelBatch;
    private Environment environment;
    private AssetManager assets;
    private MagicCamera camera;
    public Array<GameObject> instances = new Array<GameObject>();

    public AimScreen(MyGame game, AndroidLauncher androidLauncher, boolean fail) {
        this.mGame = game;
        this.androidLauncher = androidLauncher;
        this.fail = fail;
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

        backActor = new BackActor(new AssetManager());
        group2.addActor(backActor);

        beedActor = new BeedActor(new AssetManager());
        group2.addActor(beedActor);
        aimStage.addActor(group2);
    }

    @Override
    public void show() {
        isAim = (boolean) SPUtil.get(androidLauncher, "first_find", true);
        initListener();
    }

    private void initListener() {
        Gdx.input.setInputProcessor(aimStage);
        if (isFirst) {
            isFirst = false;
            backActor.setListener(new BackOnClickListener() {
                @Override
                public void onClick() {
                    Gdx.app.error("gdx", "退出");
                    mGame.showCatchScreen();
                }
            });
            beedActor.setListener(new BeedOnClickListener() {
                @Override
                public void onClick() {
                    //打开背包Stage
                    Gdx.app.error("gdx", "打开背包");
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
        }
    }

    private Vector3 position = new Vector3();

    @Override
    public void render(float delta) {
        if (aimStage == null)
            return;
        camera.update();
        if (instances.size > 0) {
            modelBatch.begin(camera);
            if (isVisible(camera, instances.get(0)))
                modelBatch.render(instances, environment);
            modelBatch.end();

            Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            final GameObject instance = instances.get(0);
            instance.transform.getTranslation(position);
            position.add(instance.center);
            final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
            float dist2 = position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
            if (dist2 <= instance.radius * instance.radius) {
                Gdx.app.error("gdx", "击中目标111111111111");
                aimActor.addNumber();
            } else {
                aimActor.subNumber();
            }
        }

        // 更新舞台逻辑
        aimStage.act();
        // 绘制舞台
        aimStage.draw();
    }

    protected boolean isVisible(final Camera cam, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
    }

    @Override
    public void resize(int width, int height) {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new MagicCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(0, 0, 0);
        camera.lookAt(0, 0, 1);
        camera.far = 1000.0f;
        camera.near = 1f;

        doneLoading();
    }

    private void doneLoading() {
        Model ship = assets.get("data/ship.obj", Model.class);
        GameObject shipInstance = new GameObject(ship);
        shipInstance.transform.setToTranslation(0, 0, 10);
        instances.add(shipInstance);
        Gdx.app.error("gdx", "doneLoading()");
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
