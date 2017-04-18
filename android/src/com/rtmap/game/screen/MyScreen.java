package com.rtmap.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rtmap.game.MyGame;
import com.rtmap.game.TestShader;
import com.rtmap.game.camera.MagicCamera;
import com.rtmap.game.interfaces.AnimationListener;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by yxy on 2017/2/24.
 */
public abstract class MyScreen implements Screen {

    private TestShader shader;
    private Viewport viewPort;
    //    private CameraInputController camController;
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
    private AnimationController animationController1;
    private AnimationController animationController2;
    private AnimationController animationController3;
    private float num = 0;
    private boolean isAnimation = false;

    //设置是否显示距离
    private boolean isLineShow = true;
    private int width;
    private int height;
    private SpriteBatch spriteBatch;
    private List<TextureRegion> texture = new ArrayList<>();
    private double angle;
    private double radius;
    private float nowAngle;
    //弧度制
    private double v;
    //字体
    private LazyBitmapFont lazyBitmapFont;
    private float x;
    private float y;
    //模型在球形背面
    private boolean isPositive = false;
    private int distance = 11;
    private int delNum = 0;
    private int drawNum = 100;
    //是否开启箭头动画
    private boolean isAnim = false;
    //动画监听
    private AnimationListener animationListener;
    //保存箭头是否显示
    private boolean oldIsLineShow = true;
    //动画持续时间，动画完成设置初始值0
    private float durations = 0;
    //判断是否正在平移动画
    private boolean isTranslate = false;
    private float time = 0;
    //是否启动线性移动，优先级高
    private boolean isStart = true;
    private Vector3 now;

    private int modelNumber = 0;
    //世界坐标转换成的屏幕坐标
    private Vector3 mProject;
    private float delTime = 0;
    private float mTextWidthRadiu;
    private int rayDistance;
    private Vector3 old;
    private float initAngle = 50f;
    private Material originalMaterial;
    private double mRelative;
    private String message;

    public MyScreen() {

    }

    public MyScreen(MyGame game) {
//        DefaultShader.defaultCullFace = 0;
        this.game = game;
        if (spriteBatch == null)
            spriteBatch = new SpriteBatch();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        radius = height * 3 / 8;

        if (modelBatch == null)
            modelBatch = new ModelBatch();
        if (environment == null)
            environment = new Environment();

        environment.set(new ColorAttribute(ColorAttribute.Emissive, 0.4f, 0.4f, 0.4f, 0f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

//        shader = new TestShader();
//        shader.init();
        if (camera == null) {
            camera = new MagicCamera(67f, width, height);
            camera.translate(0, 0, 0);
            camera.lookAt(7, 0, 7);
            camera.far = 500.0f;
            camera.near = 1f;
        }


        originalMaterial = new Material();
        originalMaterial.set(new ColorAttribute(ColorAttribute.createDiffuse(Color.YELLOW)));
//        originalMaterial.set(TextureAttribute.createDiffuse(new Texture(Gdx.files.internal("tiger/laohu.png"))), TextureAttribute.createAmbient(new Texture(Gdx.files.internal("tiger/laohu.png"))), TextureAttribute.createBump(new Texture(Gdx.files.internal("tiger/laohu.png"))), TextureAttribute.createEmissive(new Texture(Gdx.files.internal("tiger/laohu.png"))), TextureAttribute.createNormal(new Texture(Gdx.files.internal("tiger/laohu.png"))), TextureAttribute.createReflection(new Texture(Gdx.files.internal("tiger/laohu.png"))));
//        camController = new CameraInputController(camera);
    }

//    public MyScreen(MyGame game, Viewport viewPort) {
//        DefaultShader.defaultCullFace = 0;
//        this.game = game;
//        this.viewPort = viewPort;
//        spriteBatch = new SpriteBatch();
//
//        width = Gdx.graphics.getWidth();
//        height = Gdx.graphics.getHeight();
//
//        radius = height * 3 / 8;
//
//        if (modelBatch == null)
//            modelBatch = new ModelBatch();
//        if (environment == null)
//            environment = new Environment();
//
//        environment.set(new ColorAttribute(ColorAttribute.Emissive, 0.4f, 0.4f, 0.4f, 0f));
//        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
//
//
//        if (camera == null) {
//            camera = new MagicCamera(67f, width, height);
//            camera.translate(7, 7, 7);
//            camera.lookAt(0, 0, 0);
//            camera.far = 100.0f;
//            camera.near = 1f;
//        }
////        camController = new CameraInputController(camera);
//    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
    }

    private void doneLoading() {
        texture.add(new TextureRegion(game.asset.get("aim_line.png", Texture.class)));
        texture.add(new TextureRegion(game.asset.get("find_anim.png", Texture.class)));
        texture.add(new TextureRegion(game.asset.get("aim_success.png", Texture.class)));
        mTextWidthRadiu = texture.get(2).getRegionWidth() * 0.495f / 2;

        GameObject shipInstanceHuanHu = new GameObject(game.asset.get("tiger/laohu-huanhu.g3dj", Model.class));
        GameObject shipInstancePao = new GameObject(game.asset.get("tiger/laohu-pao.g3dj", Model.class));
        GameObject shipInstanceZhuaQu = new GameObject(game.asset.get("tiger/laohu-zhuaqu.g3dj", Model.class));
        shipInstanceHuanHu.userData = new Color(android.graphics.Color.YELLOW);
//        Material material = shipInstanceHuanHu.getMaterial("");
//        material.set(ColorAttribute.createDiffuse(Color.YELLOW));
//        shipInstanceHuanHu.materials.get(0).set(new TextureAttribute(TextureAttribute.Emissive, new Texture(Gdx.files.internal("tiger/laohu.png"))));
//        shipInstancePao.materials.get(0).set(new TextureAttribute(TextureAttribute.Emissive, new Texture(Gdx.files.internal("tiger/laohu.png"))));
//        shipInstanceZhuaQu.materials.get(0).set(new TextureAttribute(TextureAttribute.Emissive, new Texture(Gdx.files.internal("tiger/laohu.png"))));


//        Gdx.app.error("model",id);
//        Array<Material> materials = shipInstance.materials;
//        for (int i = 0; i < materials.size; i++) {
//            ship.
//        }
        /**
         * 0:run2|Wolf_creep_cycle
         * 1:run2|Wolf_Idle_
         * 2:run2|Wolf_Run_Cycle_
         * 3:run2|Wolf_seat_
         * 4:run2|Wolf_Walk_cycle_
         */
        shipInstanceHuanHu.transform.setToTranslation(-7, 0, 7);
//        shipInstanceHuanHu.transform.rotate(0, 1, 0, initAngle);
//        shipInstance.transform.setToScaling(1, 1, 1);
//        shipInstanceHuanHu.transform.setToLookAt()
        animationController1 = new AnimationController(shipInstanceHuanHu);
        animationController1.setAnimation("Take 001", -1);
        animationController2 = new AnimationController(shipInstancePao);
        animationController2.setAnimation("Take 001", -1);
        animationController3 = new AnimationController(shipInstanceZhuaQu);
        animationController3.setAnimation("Take 001", -1);
//        Take 001
        instances.add(shipInstanceHuanHu);
        instances.add(shipInstancePao);
        instances.add(shipInstanceZhuaQu);

        Array<Material> materials = instances.get(0).materials;
        for (int i = 0; i < materials.size; i++) {
            Material mat = materials.get(i);
            mat.clear();
            mat.set(originalMaterial);
        }
        getModelAngle();


        Gdx.app.error("model", "doneLoading()");
        isLoading = false;
    }

    /**
     * 设置模型正对相机角度
     */
    public void getModelAngle() {
        Vector3 modelAngle = new Vector3();
        instances.get(modelNumber).transform.getTranslation(modelAngle);
//        instances.get(modelNumber).transform.setToLookAt(modelAngle, new Vector3(0, 0, 0), new Vector3(modelAngle.x, modelAngle.y + 1, modelAngle.z));
        Quaternion quaternion = new Quaternion();
        double mAngle = Math.toDegrees(Math.asin(modelAngle.x / Math.sqrt(modelAngle.x * modelAngle.x + modelAngle.z * modelAngle.z)));
        instances.get(modelNumber).transform.rotate(0, 1, 0, -(float) mAngle - 80);
        instances.get(modelNumber).transform.getRotation(quaternion);
        Gdx.app.error("model", "模型角度  = " + mAngle + "  quaternion.getAngle()   " + quaternion.getAngle());
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
        for (int i = 0; i < texture.size(); i++) {
            texture.get(i).getTexture().dispose();
        }

        if (lazyBitmapFont != null)
            lazyBitmapFont.dispose();
    }

    /**
     * 平移渐变动画
     */
    public void translateAnimation() {
        if (time < durations) {
            float renderTime = Gdx.graphics.getDeltaTime();
            Gdx.app.error("model", "now.x  " + now.x + "  now.y   " + now.y + "  now.z  " + now.z);
            float scollY = (now.y - old.y) * renderTime / durations;
            float scollX = (now.x - old.x) * renderTime / durations;
            float scollZ = (now.z - old.z) * renderTime / durations;
            instances.get(modelNumber).transform.translate(scollX, scollY, scollZ);
            this.time += renderTime;
            Gdx.app.error("model", "time  =  " + this.time + "   durations == " + durations + "  scollX = " + scollX + "  scollY = " + scollY + "  scollZ = " + scollZ);
        } else {
            Gdx.app.error("model", "translate finish");
//            getModelAngle();
            isStart = true;
            durations = 0;
        }

    }

    public void setTranslate(boolean translate) {
        isTranslate = translate;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public void setDurations(int time) {
        if (isStart) {
            Random random = new Random();
            int i = random.nextInt(6) - 3;
            int j = random.nextInt(4) - 2;
            float offsetX = position.x + i;
            float offsetY = position.y + i;
            float offsetZ = position.z + j;
            Vector3 vector3 = new Vector3(offsetX, position.y, offsetZ);

            switchPosition(vector3);

            this.time = 0;
            this.durations = time;
            this.now = vector3;
            this.old = position;

            getModelInstanceAngle();

            isStart = false;
        }
    }

    /**
     * 计算移动旋转角度
     */
    public void getModelInstanceAngle() {
        float relativeX = now.x - old.x;
        float relativeY = now.z - old.z;
        if (relativeX > 0 && relativeY >= 0) {
            //一象限
            mRelative = Math.toDegrees(Math.atan(Math.abs(relativeY / relativeX)));
        } else if (relativeX >= 0 && relativeY < 0) {
            //四象限
            mRelative = Math.toDegrees(Math.atan(Math.abs(relativeX / relativeY))) - 90;
        } else if (relativeX < 0 && relativeY <= 0) {
            //三象限
            mRelative = Math.toDegrees(Math.atan(Math.abs(relativeY / relativeX))) - 180;
        } else if (relativeX <= 0 && relativeY > 0) {
            //二象限
            mRelative = Math.toDegrees(Math.atan(Math.abs(relativeX / relativeY))) - 270;
        }
        Gdx.app.error("angle", "angle  =  " + (-(float) mRelative));
        instances.get(modelNumber).transform.rotate(new Vector3(0, 1, 0), -(float) mRelative - 360);
    }

    @Override
    public void render(float delta) {
        if (spriteBatch == null) return;
        if (game.asset.update() && isLoading) {
            doneLoading();
        }
        if (!stopCamera && camera != null) {
            camera.update();
//            instances.get(0).transform.rotate()
//            Vector3 vec3 = camera.getVec3();
//            instances.get(modelNumber).transform.setFromEulerAngles(-vec3.x, -vec3.y, -vec3.z);
            isPositive();
            if (texture.size() > 0) {
                spriteBatch.begin();
                mProject = camera.project(position);
//                distance = (int) (Math.abs(Math.sqrt((mProject.x - width / 2) * (mProject.x - width / 2) + (mProject.y - height / 2) * (mProject.y - height / 2))));
                rayDistance = (int) (Math.abs(Math.sqrt((mProject.x - width / 2) * (mProject.x - width / 2) + (mProject.y - height / 2) * (mProject.y - height / 2))));
                distance = rayDistance / 120;
                Gdx.app.error("model", "  distance " + distance);
                if (animationListener != null && oldIsLineShow != isLineShow) {
                    animationListener.startAnim(distance >= 9);
                    oldIsLineShow = isLineShow;
                }
                Gdx.app.error("model", "mProject  x=  " + mProject.x + "  mProject  y=  " + mProject.y + "  mProject  z=  " + mProject.z);
                if (delTime <= Gdx.graphics.getDeltaTime() * 30)
                    delTime += Gdx.graphics.getDeltaTime();
                if (delTime > Gdx.graphics.getDeltaTime() * 30)
                    if (distance < 9 && !isPositive)
                        isLineShow = false;
                    else
                        isLineShow = true;

                if (isLineShow) {
                    //角度计算
                    getAngle(mProject);

                    double abs = Math.abs(Math.sqrt(radius * radius - width * width / 4));
                    double minRightAngle = Math.toDegrees(Math.atan(Math.abs(width / 2 / abs)));
                    double maxRightAngle = 90 + Math.toDegrees(Math.atan(Math.abs(abs / width * 2)));
                    double minLeftAngle = 180 + Math.toDegrees(Math.atan(Math.abs(width / 2 / abs)));
                    double maxLeftAngle = 270 + Math.toDegrees(Math.atan(Math.abs(abs / width * 2)));


                    if (angle >= minRightAngle && angle <= 90) {
                        v = (90 - angle) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = (float) (nowAngle * Math.sin(v));
                        x = (float) (nowAngle * Math.cos(v));
                    } else if (angle > 90 && angle <= maxRightAngle) {
                        v = (angle - 90) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = -(float) (nowAngle * Math.sin(v));
                        x = (float) (nowAngle * Math.cos(v));
                    } else if (angle >= minLeftAngle && angle < 270) {
                        v = (270 - angle) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = -(float) (nowAngle * Math.sin(v));
                        x = -(float) (nowAngle * Math.cos(v));
                    } else if (angle >= 270 && angle <= maxLeftAngle) {
                        v = (angle - 270) * Math.PI / 180;
                        nowAngle = (float) (width / 2 / Math.cos(v));
                        y = (float) (nowAngle * Math.sin(v));
                        x = -(float) (nowAngle * Math.cos(v));
                    } else {
                        v = angle * Math.PI / 180;
                        nowAngle = (float) radius;
                        x = (float) (nowAngle * Math.sin(v));
                        y = (float) (nowAngle * Math.cos(v));
                    }
                    float fontX = width / 2 + texture.get(0).getRegionWidth() / 2 + x;
                    if (fontX < 0) {
                        fontX = width * 0.01f;
                    } else if (fontX + width * 0.09f > width) {
                        fontX = width * 0.91f;
                    }
                    //绘制指示器
                    spriteBatch.draw(texture.get(0), width / 2 - texture.get(0).getRegionWidth() / 2, height / 2, texture.get(0).getRegionWidth() / 2, 0, texture.get(0).getRegionWidth(), Math.abs(nowAngle), 1, 1, (float) (-angle));


                    if (lazyBitmapFont == null)
                        lazyBitmapFont = new LazyBitmapFont(ScreenUtil.dp2px(10), Color.valueOf("#ffb54b"));

                    if (distance > 100 || isPositive) {
                        message = "距离\n>100";
                    } else {
                        message = "距离\n" + distance;
                    }
                    lazyBitmapFont.draw(spriteBatch, message, fontX, height / 2 + y, width * 0.09f, Align.left, true);
                }
                if (isLineShow && isAnim) {
                    if (delNum <= drawNum) {
                        spriteBatch.draw(texture.get(1), width / 2 + x * 0.75f / drawNum * delNum + x * 0.186f - texture.get(1).getRegionWidth() / 2, height / 2 + y * 0.75f / drawNum * delNum + y * 0.186f, texture.get(1).getRegionWidth() / 2, 0, texture.get(1).getRegionWidth(), texture.get(1).getRegionHeight(), 2, 2, (float) (-angle));
                        delNum++;
                    } else {
                        if (animationListener != null) {
                            animationListener.endAnim();
                        }
                        delNum = 0;
                    }
                }
                spriteBatch.end();
            }
        }
        if (instances.size > 0 && !stopRerder) {
            GameObject object = instances.get(modelNumber);
            modelBatch.begin(camera);
            if (isVisible(camera, object)) {
                modelBatch.render(object, environment);
            }
            modelBatch.end();
            //线性移动

            setDurations(2);
            translateAnimation();

            Gdx.app.error("model", "position.x  " + position.x + "  position.y   " + position.y + "  position.z  " + position.z);
            switchAnimationModel();


//            Gdx.app.error("mdoel", " unproject  x == " + unproject.x + "   unproject   y ==  " + unproject.y + "  unproject   z == " + unproject.z);
//            if (rayDistance < mTextWidthRadiu && !isPositive) {
//                addNumber();
//
//            } else {
//                subNumber();
//            }
            /**
             * 添加射线
             */
            Ray ray = camera.getPickRay(width / 2, height / 2);
            final GameObject instance = object;
            instance.transform.getTranslation(position);
            position.add(instance.center);
            final float len = ray.direction.dot(position.x - ray.origin.x, position.y - ray.origin.y, position.z - ray.origin.z);
            float dist2 = position.dst2(ray.origin.x + ray.direction.x * len, ray.origin.y + ray.direction.y * len, ray.origin.z + ray.direction.z * len);
            if (dist2 <= instance.radius * instance.radius && !isPositive) {
                addNumber();
            } else {
                subNumber();
            }
        }
    }

    private void switchPosition(Vector3 rangeVec) {

    }

    //设置显示模型
    public void setModelNumber(int number) {
        this.modelNumber = number;
        if (instances.size > 0) {
            instances.get(number).transform.setToTranslation(position.x, position.y, position.z);
            getModelAngle();
        }
    }

    public void switchAnimationModel() {
        switch (modelNumber) {
            case 0:
                animationController1.update(Gdx.graphics.getDeltaTime());
                break;
            case 1:
                animationController2.update(Gdx.graphics.getDeltaTime());
                break;
            case 2:
                animationController3.update(Gdx.graphics.getDeltaTime());
                break;
        }
    }

    public void setAnimationListener(AnimationListener animationListener) {
        this.animationListener = animationListener;
    }

    public boolean isAnim() {
        return isAnim;
    }

    public void setIsAnim(boolean isAnim) {
        this.isAnim = isAnim;
        delNum = 0;
    }

    public void setIsLineShow(boolean isLineShow) {
        this.isLineShow = isLineShow;
        delTime = 0;
    }

    public boolean isLineShow() {
        return isLineShow;
    }

    /**
     * 判断模型在摄像机正面还是背面
     * true:背面
     * false:正面
     */
    public void isPositive() {
        Vector3 direction = camera.direction;
        Vector3 backDirection = new Vector3(-direction.x, -direction.y, -direction.z);
        double abs1 = Math.abs(Math.sqrt((direction.x - position.x) * (direction.x - position.x) + (direction.y - position.y) * (direction.y - position.y) + (direction.z - position.z) * (direction.z - position.z)));
        double abs2 = Math.abs(Math.sqrt((backDirection.x - position.x) * (backDirection.x - position.x) + (backDirection.y - position.y) * (backDirection.y - position.y) + (backDirection.z - position.z) * (backDirection.z - position.z)));
        if (abs1 - abs2 > 0) {
            isPositive = true;
        } else {
            isPositive = false;
        }
    }

    /**
     * 计算角度
     *
     * @param project
     * @return
     */
    private void getAngle(Vector3 project) {
        double tan;
        if (project.x == width / 2) {
            if (project.y > height / 2) {
                angle = 0;
            } else if (project.y < height / 2) {
                angle = 180;
            }
        } else if (project.y == height / 2) {
            if (project.x > width / 2) {
                angle = 90;
            } else if (project.x < width / 2) {
                angle = 270;
            }
        } else {
            if (project.x > width / 2 && project.y > height / 2) {
                //第一象限
                if (isPositive) {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan))) - 180;
                } else {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            } else if (project.x > width / 2 && project.y < height / 2) {
                //第四象限
                if (isPositive) {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan))) - 90;
                } else {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = 90 + Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            } else if (project.x < width / 2 && project.y < height / 2) {
                //第三象限
                if (isPositive) {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = Math.toDegrees(Math.atan(Math.abs(tan)));
                } else {
                    tan = (project.x - width / 2) / (project.y - height / 2);
                    angle = 180 + Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            } else if (project.x < width / 2 && project.y > height / 2) {
                //第二象限
                if (isPositive) {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = 90 + Math.toDegrees(Math.atan(Math.abs(tan)));
                } else {
                    tan = (project.y - height / 2) / (project.x - width / 2);
                    angle = 270 + Math.toDegrees(Math.atan(Math.abs(tan)));
                }
            }
        }
    }

    //子类实现
    public void addNumber() {

    }

    //子类实现
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
