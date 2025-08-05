package org.example.redalert.libgdx.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.graphics.use
import org.example.redalert.controller.MapController
import org.example.redalert.libgdx.input.IdleInputProcessor
import org.example.redalert.map.Cell

class BattlefieldScreen:KtxScreen {
    /**
     * 摄像头
     */
    private val camera by lazy {
        OrthographicCamera(
            Gdx.graphics.width.toFloat(),
            Gdx.graphics.height.toFloat()
        )
    }
    
    /**
     * 转换屏幕坐标到世界坐标
     */
    private val tp = Vector3()
    
    /**
     * 总体绘制
     */
    private val batch = SpriteBatch()
    
    /**
     * 绘制底图网格
     */
    private val sprite:Sprite
    
    /**
     * 鼠标指针指示
     */
    private val cursorCircle = ShapeRenderer()
    
    init {
        // 地图宽
        val mapWidth = 10
        // 地图高
        val mapHeight = 10
        
        val pixmap = Pixmap(
            mapWidth * Cell.WIDTH,
            mapHeight * Cell.HEIGHT,
            Pixmap.Format.RGBA8888
        )
        pixmap.filter = Pixmap.Filter.BiLinear
        pixmap.setColor(Color.BLACK)
        
        MapController.create(mapWidth, mapHeight)
        // 绘制单元格边框
        // 注意：Pixmap 的坐标系原点是在左上角
        for (cell in MapController.map.cells) {
            // 绘制 West-North 方向边
            pixmap.drawLine(cell.westX, cell.westY, cell.northX, cell.northY)
            // 绘制 East-North 方向边
            pixmap.drawLine(cell.northX, cell.northY, cell.eastX, cell.eastY)
            // 绘制 East-South 方向边
            pixmap.drawLine(cell.eastX, cell.eastY, cell.southX, cell.southY)
            // 绘制 West-South 方向边
            pixmap.drawLine(cell.southX, cell.southY, cell.westX, cell.westY)
        }
        
        val texture = Texture(pixmap)
        pixmap.disposeSafely()
        this.sprite = Sprite(texture)
        this.sprite.setSize(texture.width.toFloat(), texture.height.toFloat())
    }
    
    override fun show() {
        this.camera.position.set(
            this.sprite.width / 2f,
            this.sprite.height / 2f,
            0f
        )
        this.camera.zoom = this.sprite.width / this.camera.viewportWidth
        this.camera.update()
        Gdx.input.inputProcessor = IdleInputProcessor(this.camera, this.tp)
    }
    
    override fun resize(width:Int, height:Int) {
        this.camera.viewportWidth = width.toFloat()
        this.camera.viewportHeight = height.toFloat()
        this.camera.update()
    }
    
    override fun render(delta:Float) {
        input()
        logic()
        draw()
    }
    
    private fun input() {
        // if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
        //     Gdx.app.log("BattlefieldScreen", "handle input Q")
        //     this.camera.zoom -= 0.02f
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.E)) {
        //     Gdx.app.log("BattlefieldScreen", "handle input E")
        //     this.camera.zoom += 0.02f
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        //     Gdx.app.log("BattlefieldScreen", "handle input W")
        //     this.camera.translate(0f, 3f, 0f)
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        //     Gdx.app.log("BattlefieldScreen", "handle input S")
        //     this.camera.translate(0f, -3f, 0f)
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        //     Gdx.app.log("BattlefieldScreen", "handle input A")
        //     this.camera.translate(-3f, 0f, 0f)
        // }
        // if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        //     Gdx.app.log("BattlefieldScreen", "handle input D")
        //     this.camera.translate(3f, 0f, 0f)
        // }
        
        // this.camera.zoom = MathUtils.clamp(this.camera.zoom, 0.1f, MapController.map.width / this.camera.viewportWidth)
        //
        // val effectiveViewportWidth:Float = this.camera.viewportWidth * this.camera.zoom
        // val effectiveViewportHeight:Float = this.camera.viewportHeight * this.camera.zoom
        //
        // this.camera.position.x = MathUtils.clamp(this.camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f)
        // this.camera.position.y = MathUtils.clamp(this.camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f)
    }
    
    private fun logic() {
    }
    
    private fun draw() {
        this.camera.update()
        clearScreen(red = 0.7f, green = 0.7f, blue = 0.7f)
        this.batch.use(this.camera.combined) {
            sprite.draw(it)
        }
        this.cursorCircle.color = Color.RED
        this.cursorCircle.use(ShapeRenderer.ShapeType.Filled, this.camera.combined) {
            it.circle(tp.x, tp.y, 10f) // Segments 指使用多少边形近似
        }
    }
    
    override fun pause() {
    }
    
    override fun resume() {
    }
    
    override fun dispose() {
        this.batch.disposeSafely()
        this.sprite.texture.disposeSafely()
    }
}
