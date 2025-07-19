package org.example.redalert.libgdx

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import org.example.redalert.libgdx.ui.BattlefieldScreen

class Main:KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        addScreen(BattlefieldScreen())
        setScreen<BattlefieldScreen>()
    }
}
