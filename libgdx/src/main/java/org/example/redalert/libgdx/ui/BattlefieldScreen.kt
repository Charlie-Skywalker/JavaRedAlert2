package org.example.redalert.libgdx.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use

class BattlefieldScreen:KtxScreen {
    private val batch = SpriteBatch()
    private val viewport = FitViewport(6f, 3f)
    private val texture = let {
        val pixmap = Pixmap(100, 100, Pixmap.Format.RGBA8888)
        pixmap.filter = Pixmap.Filter.BiLinear
        // 注意：Pixmap 的坐标系原点是在左上角
        pixmap.setColor(Color.BLUE)
        pixmap.drawLine(0, 0, pixmap.width, pixmap.height)
        // pixmap.fillRectangle(0, 0, pixmap.width, pixmap.height)
        val texture = Texture(pixmap, true)
        pixmap.disposeSafely()
        texture
    }
    
    override fun show() {
    }
    
    override fun render(delta:Float) {
        input()
        logic()
        draw()
    }
    
    private fun input() {
    }
    
    private fun logic() {
    }
    
    private fun draw() {
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        this.viewport.apply(true)
        this.batch.use(this.viewport.camera.combined) {
            it.draw(this.texture, 0f, 0f, 1f, 1f)
        }
    }
    
    override fun resize(width:Int, height:Int) {
        this.viewport.update(width, height, true)
    }
    
    override fun pause() {
    }
    
    override fun resume() {
    }
    
    override fun dispose() {
        this.batch.disposeSafely()
        this.texture.disposeSafely()
    }
}
