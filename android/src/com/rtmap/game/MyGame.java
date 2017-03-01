package com.rtmap.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Array;
import com.rtmap.game.camera.AndroidDeviceCameraController;
import com.rtmap.game.camera.LibGDXPerspectiveCamera;
import com.rtmap.game.screen.AimScreen;
import com.rtmap.game.screen.BeedScreen;
import com.rtmap.game.screen.CatchScreen;
import com.rtmap.game.screen.FindScreen;
import com.rtmap.game.screen.LoadingScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yxy on 2017/2/20.
 */
public class MyGame extends Game {
    private ModelBatch modelBatch;
    private Environment environment;
    private boolean loading;
    private AssetManager assets;
    private LibGDXPerspectiveCamera camera;
    private AndroidDeviceCameraController androidDeviceCameraController;
    private AndroidLauncher androidLauncher;
    private List<Screen> screenList;
    private LoadingScreen loadingScreen;
    private FindScreen findScreen;
    private Screen oldScreen;
    private CatchScreen catchScreen;
    private AimScreen aimScreen;
    public Array<ModelInstance> instances = new Array<ModelInstance>();

    /**
     * 设置相机模式
     */
    private int normal_Mode = 0;
    private int prepare_Mode = 1;
    private int preview_Mode = 2;

    private int mode = normal_Mode;

    public MyGame(AndroidLauncher androidLauncher, AndroidDeviceCameraController androidDeviceCameraController) {
        this.androidLauncher = androidLauncher;
        this.androidDeviceCameraController = androidDeviceCameraController;

        screenList = new ArrayList<>();

        assets = new AssetManager();
        assets.load("data/ship.obj", Model.class);
        loading = true;
    }

    private void doneLoading() {
        Model ship = assets.get("data/ship.obj", Model.class);
        ModelInstance shipInstance = new ModelInstance(ship);
        shipInstance.transform.setToTranslation(0, 0, -10);
        instances.add(shipInstance);
        loading = false;
        Gdx.app.error("gdx", "doneLoading()");
    }

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new LibGDXPerspectiveCamera(androidLauncher, 30f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setPosition(0, 0, 0);
        camera.far = 1000.0f;
        camera.near = 1f;

        loadingScreen = new LoadingScreen(this, androidDeviceCameraController);
        findScreen = new FindScreen(this, androidLauncher);

        setScreen(loadingScreen);
    }

    @Override
    public void render() {
        if (loading && assets.update())
            doneLoading();

        if (mode == normal_Mode) {
            Gdx.app.error("gdx", "normal_Mode");
            if (androidDeviceCameraController != null) {
                androidDeviceCameraController.prepareCameraAsync();
                mode = prepare_Mode;
            }

        } else if (mode == prepare_Mode) {
            Gdx.app.error("gdx", "prepare_Mode");
            if (androidDeviceCameraController != null)
                if (androidDeviceCameraController.isReady()) {
                    androidDeviceCameraController.startPreviewAsync();
                    mode = preview_Mode;
                }
        }
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.render();
        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        modelBatch.end();
        super.render();
    }

    @Override
    public void pause() {
        if (androidDeviceCameraController != null) {
            Gdx.app.error("gdx", "pause");
            androidDeviceCameraController.stopPreviewAsync();
            mode = normal_Mode;
        }
        super.pause();
    }

    @Override
    public void dispose() {
        for (int i = 0; i < screenList.size(); i++) {
            Screen screen = screenList.get(i);
            if (screen != null) {
                screen.dispose();
                screenList.remove(i);
            }
        }
    }

    public void showScreen() {
        setScreen(findScreen);

//        if (loadingScreen != null) {
//            Gdx.app.error("gdx", "2222222222222222");
//            // 由于 StartScreen 只有在游戏启动时展示一下, 之后都不需要展示,
//            // 所以启动完 GameScreen 后手动调用 StartScreen 的 dispose() 方法销毁开始场景。
//            loadingScreen.dispose();
//
//            // 场景销毁后, 场景变量值空, 防止二次调用 dispose() 方法
//            loadingScreen = null;
//        }
    }

//    public Screen getOldScreen() {
//        return oldScreen;
//    }

    public void showAimScreen() {
        aimScreen = new AimScreen(this, androidLauncher);
        setScreen(aimScreen);
    }

    public void showCatchScreen() {
        catchScreen = new CatchScreen(this, androidLauncher);
        setScreen(catchScreen);
    }

    public void showOldScreen() {
        if (oldScreen == null)
            return;
        if (oldScreen instanceof FindScreen) {
            setScreen(findScreen);
        } else if (oldScreen instanceof AimScreen) {
            setScreen(aimScreen);
        } else if (oldScreen instanceof CatchScreen) {
            setScreen(catchScreen);
        }
    }

    public void showBeedScreen(Screen oldScreen) {
        this.oldScreen = oldScreen;
        setScreen(new BeedScreen(this, androidLauncher));
    }

    public void showLoadingScreen() {
        setScreen(loadingScreen);
    }
}
