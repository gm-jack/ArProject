package com.rtmap.game.screen;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rtmap.game.AndroidLauncher;
import com.rtmap.game.MyGame;
import com.rtmap.game.actor.MainActor;
import com.rtmap.game.actor.StartActor;
import com.rtmap.game.interfaces.StartOnClickListener;
import com.rtmap.game.stage.MainStage;
import com.rtmap.game.text.LazyBitmapFont;
import com.rtmap.game.util.Contacts;
import com.rtmap.game.util.SPUtil;
import com.rtmap.game.util.ScreenUtil;

/**
 * Created by yxy on 2017/3/2.
 */
public class MainScreen extends MyScreen {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Texture bgTexture;
    private Texture cursorTexture;
    private LazyBitmapFont bitmapFont;
    private StartActor startActor;
    private MyGame mGame;
    private AndroidLauncher androidLauncher;
    private MainStage mainStage;
    private Group group;
    private MainActor mainActor;
    private AssetManager assetManager;
    private final TextField mTextField;

    public MainScreen(MyGame game, AndroidLauncher androidLauncher) {
        this.mGame = game;
        this.androidLauncher = androidLauncher;

        mainStage = new MainStage(new ScreenViewport());
        assetManager = new AssetManager();

        initResources();

        group = new Group();
        mainActor = new MainActor(assetManager);
        mainActor.setPosition(0, 0);
        mainActor.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        group.addActor(mainActor);

        startActor = new StartActor(assetManager);
        group.addActor(startActor);

        bgTexture = createBackgroundTexture();
        cursorTexture = createCursorTexture();
        // 为了方便演示, 这里直接使用 gdx.jar 中自带的字体文件创建位图字体（只要在 BitmapFont 中包含有的字符才能够被输入）
        bitmapFont = new LazyBitmapFont(ScreenUtil.dp2px(12), Color.WHITE);

        /*
         * 第 2 步: 创建 TextFieldStyle
         */
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();

        // 设置背景纹理区域
        style.background = new TextureRegionDrawable(new TextureRegion(bgTexture));

        // 设置光标纹理区域
        style.cursor = new TextureRegionDrawable(new TextureRegion(cursorTexture));

        // 设置文本框显示文本的字体来源
        style.font = bitmapFont;

        // 设置文本框字体颜色为白色
        style.fontColor = new Color(1, 1, 1, 1);
        mTextField = new TextField((String) SPUtil.get(Contacts.PHONE, "输入手机号"), style);
        mTextField.setSize(400, 100);
        mTextField.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 0.9f);
        mTextField.setAlignment(Align.center);

        mainStage.addActor(group);
    }

    /**
     * 创建文本框的背景纹理
     */
    private Texture createBackgroundTexture() {
        Pixmap pixmap = new Pixmap(400, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1);
        pixmap.drawRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /**
     * 创建文本框中的光标纹理
     */
    private Texture createCursorTexture() {
        Pixmap pixmap = new Pixmap(1, 100 - 4, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 0, 0, 1);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void initResources() {
        assetManager.load("m_bg.png", Texture.class);
        assetManager.load("m_rule.png", Texture.class);
        assetManager.load("m_start.png", Texture.class);
    }

    @Override
    public void show() {

    }

    private void initListener() {
        Gdx.input.setInputProcessor(mainStage);
        group.addActor(mTextField);
        startActor.setListener(new StartOnClickListener() {
            @Override
            public void onClick() {
                if (TextUtils.isEmpty(mTextField.getText())) {
                    return;
                } else {
                    String trim = mTextField.getText().trim();
                    SPUtil.put(Contacts.PHONE, trim);
                    Contacts.LIST_NET += trim;
                    Contacts.WIN_NET += trim;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (androidLauncher.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || androidLauncher.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || androidLauncher.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        androidLauncher.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                    } else if (mGame != null)
                        mGame.showLoadingScreen();
                } else if (mGame != null)
                    mGame.showLoadingScreen();
            }
        });
    }

    @Override
    public void render(float delta) {
        if (mainStage == null)
            return;

        // 更新舞台逻辑
        mainStage.act();
        // 绘制舞台
        mainStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        initListener();
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
        assetManager.unload("m_bg.png");
        assetManager.unload("m_rule.png");
        assetManager.unload("m_start.png");
        // 场景被销毁时释放资源
        if (bgTexture != null) {
            bgTexture.dispose();
        }
        if (cursorTexture != null) {
            cursorTexture.dispose();
        }
        if (bitmapFont != null) {
            bitmapFont.dispose();
        }
        if (mainStage != null) {
            mainStage.dispose();
        }
    }
}
