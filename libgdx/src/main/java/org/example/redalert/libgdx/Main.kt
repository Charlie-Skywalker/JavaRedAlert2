package org.example.redalert.libgdx

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import org.example.redalert.libgdx.ui.BattlefieldScreen

class Main:KtxGame<KtxScreen>() {
    var initialized = false
    
    /**
     * 生命周期阶段 1：创建
     */
    override fun create() {
        if (initialized) {
            super.create()
            return
        }
        KtxAsync.initiate()
        addScreen(BattlefieldScreen())
        setScreen<BattlefieldScreen>()
    }
    
    /**
     * 生命周期阶段 2：设置大小
     *
     * 如果屏幕大小改变也会走到该阶段
     *
     * @param width 屏幕宽度
     * @param height 屏幕高度
     */
    override fun resize(width:Int, height:Int) {
        super.resize(width, height)
    }
    
    /**
     * 生命周期阶段 2：渲染
     *
     * 游戏主循环也在此处
     */
    override fun render() {
        super.render()
    }
    
    /**
     * 生命周期阶段 3：暂停
     */
    override fun pause() {
        super.pause()
    }
    
    /**
     * 生命周期阶段 4：恢复
     */
    override fun resume() {
        super.resume()
    }
    
    /**
     * 生命周期阶段 5：结束
     */
    override fun dispose() {
        super.dispose()
    }
}
