package org.example.redalert.libgdx.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use

class BattlefieldScreen:KtxScreen {
    private val batch = SpriteBatch()
    private val viewport = FitViewport(5f, 5f)
    private val image = Texture("logo.png".toInternalFile(), true).apply {
        setFilter(Linear, Linear)
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
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            it.draw(image, 0f, 0f, 1f, 1f)
        }
    }
    
    override fun resize(width:Int, height:Int) {
        viewport.update(width, height, true)
    }
    
    override fun pause() {
    }
    
    override fun resume() {
    }
    
    override fun dispose() {
        image.disposeSafely()
        batch.disposeSafely()
    }
}
