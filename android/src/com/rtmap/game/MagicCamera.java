package com.rtmap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by 程树欣 on 2017/3/1.
 */

public class MagicCamera extends PerspectiveCamera {
    public MagicCamera() {
    }

    public MagicCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
        super(fieldOfViewY, viewportWidth, viewportHeight);
    }

    /**平滑转动camera*/
    Vector3 oldVector3 = new Vector3();
    Vector3 directionVector3 = new Vector3();
    private void updateCamera() {

        Matrix4 mat4 = new Matrix4();
        Gdx.input.getRotationMatrix(mat4.val);
        Vector3 newVector3 = new Vector3(mat4.val[Matrix4.M11], mat4.val[Matrix4.M12], mat4.val[Matrix4.M10]);
        if(oldVector3==null)oldVector3 = new Vector3();
        oldVector3.lerp(newVector3, 5 * Math.min(0.05f, Gdx.graphics.getDeltaTime()));
        up.set(oldVector3);
        Vector3 dVector3 = new Vector3(-mat4.val[Matrix4.M21], -mat4.val[Matrix4.M22], -mat4.val[Matrix4.M20]);
        if(directionVector3==null)directionVector3 = new Vector3();
        directionVector3.lerp(dVector3, 5 * Math.min(0.05f, Gdx.graphics.getDeltaTime()));
        direction.set(directionVector3);
    }
    @Override
    public void update() {
        updateCamera();
        super.update();
    }
}
