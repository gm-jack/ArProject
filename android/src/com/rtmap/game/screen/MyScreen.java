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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.rtmap.game.MagicCamera;

/**
 * Created by yxy on 2017/2/24.
 */
public abstract class MyScreen implements Screen {
    private AssetManager assets;
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

    public MyScreen() {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new MagicCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.translate(0, 0, 0);
        camera.lookAt(0, 0, 10);
        camera.far = 1000.0f;
        camera.near = 1f;

        assets = new AssetManager();
        assets.load("data/ship.obj", Model.class);
        assets.finishLoading();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        if (isLoading)
            doneLoading();
    }

    private void doneLoading() {
        Model ship = assets.get("data/ship.obj", Model.class);
        GameObject shipInstance = new GameObject(ship);
        shipInstance.transform.setToTranslation(0, 0, 7);
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

    }

    @Override
    public void render(float delta) {
        if (!stopCamera) {
            Gdx.app.error("gdx", "camera.update()");
            camera.update();
        }
        if (instances.size > 0 && !stopRerder) {
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
                addNumber();
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
