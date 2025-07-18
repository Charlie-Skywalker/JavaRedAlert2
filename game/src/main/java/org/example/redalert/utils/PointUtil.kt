package org.example.redalert.utils

import org.example.redalert.ui.element.Cell

/**
 * 点工具类
 */
object PointUtil {
    /**
     * 获取中心点
     * @param x 当前 X 坐标
     * @param y 当前 Y 坐标
     * @return 当前坐标所在地块的中心点坐标
     */
    @JvmStatic
    fun getCenter(x:Int, y:Int):Pair<Int, Int> {
        // 所有地块中心点 X 轴都是 15 的倍数
        // 所有地块中心点 Y 轴都是 7.5 的倍数
        // 按照屏幕比例，X 轴坐标和 Y 轴坐标的为 2:1
        val left = x / Cell.sWidth
        val centerX = left + (Cell.sWidth / 2)
        val centerY = centerX / 2
        return centerX to centerY
    }
}
