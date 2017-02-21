package com.rtmap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.rtmap.game.actor.CustomActor;

/**
 * Created by yxy on 2017/2/20.
 */
public class GameScreen extends ScreenAdapter {
    private Texture manTexture;

    private Stage stage;

    private CustomActor manActor;

    public GameScreen() {
        // 创游戏人物的纹理, 图片 badlogic.jpg 的宽高为 256 * 256
        manTexture = new Texture(Gdx.files.internal("badlogic.jpg"));

        // 使用伸展视口创建舞台
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // 创建游戏人物演员
        manActor = new CustomActor(new TextureRegion(manTexture));

        // 添加演员到舞台
        stage.addActor(manActor);
    }



    @Override
    public void render(float delta) {
        // 红色清屏
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 更新舞台逻辑
        stage.act();
        // 绘制舞台
        stage.draw();
    }

    @Override
    public void dispose() {
        super.dispose();
        // 场景被销毁时释放资源
        if (stage != null) {
            stage.dispose();
        }
        if (manTexture != null) {
            manTexture.dispose();
        }
    }
}
