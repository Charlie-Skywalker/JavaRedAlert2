package org.example.redalert.libgdx.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3

class IdleInputProcessor(
    val camera:OrthographicCamera,
    val tp:Vector3,
):InputProcessor {
    
    override fun keyDown(keycode:Int):Boolean {
        return false
    }
    
    override fun keyUp(keycode:Int):Boolean {
        return false
    }
    
    override fun keyTyped(character:Char):Boolean {
        return false
    }
    
    override fun touchDown(screenX:Int, screenY:Int, pointer:Int, button:Int):Boolean {
        return false
    }
    
    override fun touchUp(screenX:Int, screenY:Int, pointer:Int, button:Int):Boolean {
        return false
    }
    
    override fun touchCancelled(screenX:Int, screenY:Int, pointer:Int, button:Int):Boolean {
        return false
    }
    
    override fun touchDragged(screenX:Int, screenY:Int, pointer:Int):Boolean {
        return false
    }
    
    override fun mouseMoved(screenX:Int, screenY:Int):Boolean {
        Gdx.app.log("IdleInputProcessor", "mouseMoved x: ${screenX}, y: ${screenY}")
        camera.unproject(tp.set(screenX.toFloat(), screenY.toFloat(), 0f))
        return false
    }
    
    override fun scrolled(amountX:Float, amountY:Float):Boolean {
        Gdx.app.log("IdleInputProcessor", "scrolled x: ${amountX}, y: ${amountY}")
        // y < 0 向前滚动
        // y > 0 向后滚动
        if (amountY > 0) {
            this.camera.zoom -= 0.02f
        } else if (amountY < 0) {
            this.camera.zoom += 0.02f
        }
        return false
    }
}
