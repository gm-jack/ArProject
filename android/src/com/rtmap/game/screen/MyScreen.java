package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Animation;
import com.badlogic.gdx.graphics.g3d.model.NodeAnimation;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.rtmap.game.MagicCamera;
import com.rtmap.game.MyGame;

/**
 * Created by yxy on 2017/2/24.
 */
public abstract class MyScreen implements Screen {
    private MyGame game;
    public Array<GameObject> instances = new Array<GameObject>();
    private ModelBatch modelBatch;
    private Environment environment;
    private MagicCamera camera;
    private Vector3 position = new Vector3();
    //是否停止更新camera
    private boolean stopCamera = true;
    //是否加载完成模型
    private boolean isLoading = true;
    //是否停止绘制模型
    private boolean stopRerder = false;
    private float degree = 180;
    private AnimationController animationController;
    private float num = 0;
    private boolean isAnimation = false;

    public MyScreen() {

    }

    public MyScreen(MyGame game) {
        this.game = game;
        if (modelBatch == null)
            modelBatch = new ModelBatch();
        if (environment == null)
            environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        if (camera == null) {
            camera = new MagicCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            camera.translate(0, 0, 0);
            camera.lookAt(0, 0, 15);
            camera.far = 1000.0f;
            camera.near = 1f;
        }
        game.asset.load("wolf/Wolf_fbx.g3dj", Model.class);
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
    }

    private void doneLoading() {
        Model ship = game.asset.get("wolf/Wolf_fbx.g3dj", Model.class);
        GameObject shipInstance = new GameObject(ship);
        /**
         * 0:run2|Wolf_creep_cycle
         * 1:run2|Wolf_Idle_
         * 2:run2|Wolf_Run_Cycle_
         * 3:run2|Wolf_seat_
         * 4:run2|Wolf_Walk_cycle_
         */
        shipInstance.transform.setToTranslation(0, 0, 5);
//        shipInstance.transform.setToScaling(1, 1, 1);
        shipInstance.transform.rotate(0, 1, 0, degree);
        animationController = new AnimationController(shipInstance);
        animationController.setAnimation("run2|Wolf_Run_Cycle_", -1);

        instances.add(shipInstance);
        Gdx.app.error("gdx", "doneLoading()");
        isLoading = false;
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
        game.asset.unload("wolf/Wolf_fbx.g3dj");
    }

    /**
     * 平移渐变动画
     *
     * @param positions 新的世界坐标
     * @param time      平移持续时间
     */
    public void translateAnimation(Vector3 positions, float time) {
//        if (instances.size > 0 && !isAnimation) {
//            isAnimation = true;
//            instances.get(0).transform.setToTranslation(positions);
//        }

        if (instances.size > 0 && !isAnimation) {
            isAnimation = true;
            float scollX = positions.x - position.x;
            float scollY = positions.y - position.y;
            float scollZ = positions.z - position.z;
//            float oldL = oldPosition.x * oldPosition.x + oldPosition.y * oldPosition.y + oldPosition.z * oldPosition.z;
            float newL = positions.x * positions.x + 0 + positions.z * positions.z;
            double sqrt = Math.sqrt(Math.abs(scollX) * Math.abs(scollX) + Math.abs(scollY) * Math.abs(scollY) + Math.abs(scollZ) * Math.abs(scollZ));
//            double cos = (sqrt * sqrt + oldL * oldL - newL * newL) / 2 / oldL / sqrt;
//            num = (int) (sqrt / time * 1000);
//            Gdx.app.error("animation", " num   ==  " + num);
            Gdx.app.error("animation", " oldPosition ==  " + position.x + "  " + position.y + "    " + position.z);
            float number = time / Gdx.graphics.getDeltaTime();
            Gdx.app.error("animation", " getDeltaTime   ==  " + Gdx.graphics.getDeltaTime());
//            for (int i = 1; i <= num; i++) {
//               instances.get(0).transform.setToTranslation(oldPosition.x + scollX * i / num, oldPosition.y + scollY * i / num, oldPosition.z + scollZ * i / num);
//            }
            if (num <= number) {
                instances.get(0).transform.setToTranslation(position.x + scollX * Gdx.graphics.getDeltaTime() * num, position.y + scollY * Gdx.graphics.getDeltaTime() * num, position.z + scollZ * Gdx.graphics.getDeltaTime() * num);
                Gdx.app.error("animation", " position ==  " + (position.x + scollX * Gdx.graphics.getDeltaTime() * num) + "  " + (position.y + scollY * Gdx.graphics.getDeltaTime() * num) + "    " + (position.z + scollZ * Gdx.graphics.getDeltaTime() * num));
                num++;
            }

//            degree -= cos * 180 / 3.14;
//            instances.get(0).transform.rotate(0, 1, 0, degree);
//            isAnimation = false;
        }
    }

    @Override
    public void render(float delta) {
        if (game.asset.update() && isLoading) {
            doneLoading();
        }
        if (!stopCamera && camera != null) {
            camera.update();
        }
        if (instances.size > 0 && !stopRerder) {
            modelBatch.begin(camera);
            if (isVisible(camera, instances.get(0))) {
                modelBatch.render(instances, environment);
            }
            modelBatch.end();
            animationController.update(Gdx.graphics.getDeltaTime());
            Ray ray = camera.getPickRay(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            final GameObject instance = instances.get(0);
            instance.transform.getTranslation(position);
            position.add(instance.center);
            final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
            float dist2 = position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
            if (dist2 <= instance.radius * instance.radius) {
                Gdx.app.error("gdx", "击中目标111111111111");
                addNumber();
//                translateAnimation(new Vector3(0, 0, 5), 5000);
//                instance.transform.setToTranslation(0, 0, 2);
            } else {
                subNumber();
            }

        }
    }

    public void addNumber() {

    }

    public void subNumber() {

    }

    public void setStopCamera(boolean stopCamera) {
        this.stopCamera = stopCamera;
    }

    public void setStopRerder(boolean stopRerder) {
        this.stopRerder = stopRerder;
    }

    protected boolean isVisible(final Camera cam, final GameObject instance) {
        instance.transform.getTranslation(position);
        position.add(instance.center);
        return cam.frustum.sphereInFrustum(position, instance.radius);
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
