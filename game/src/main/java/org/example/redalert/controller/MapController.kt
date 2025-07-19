package org.example.redalert.controller

import org.example.redalert.map.Cell
import org.example.redalert.map.Map

object MapController {
    /**
     * 当前地图
     */
    @get:JvmStatic
    var map:Map? = null
    
    /**
     * 根据尺寸创建地图
     *
     * 尺寸和单元格对应关系参考 [ModEnc 关于 Map 文件 IsoMapPack5 Section 的解释](https://modenc.renegadeprojects.com/IsoMapPack5)
     * @param width 地图宽，单位为 [Cell]
     * @param height 地图高，单位为 [Cell]
     */
    @JvmStatic
    fun create(width:Int, height:Int) {
        this.map = Map(width, height)
    }
}
