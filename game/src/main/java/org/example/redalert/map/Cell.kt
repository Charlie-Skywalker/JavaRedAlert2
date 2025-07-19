package org.example.redalert.map

/**
 * 地图单元格
 *
 * 每个单元格是一个正方形的斜 45 度投影，显示形状是横纵轴比为 2:1 的菱形
 *
 * 为了方便在坐标系中绘制，实际会以长宽比为 2:1 的矩形进行设计
 */
data class Cell(
    /**
     * 中心点 X 坐标
     */
    val centerX:Int,
    /**
     * 中心点 Y 坐标
     */
    val centerY:Int
) {
    /**
     * 左上角 X 坐标
     */
    val northWestX:Int = centerX - WIDTH / 2
    
    /**
     * 左上角 Y 坐标
     */
    val northWestY:Int = centerY - HEIGHT / 2
    
    /**
     * 右上角 X 坐标
     */
    val northEastX:Int = centerX + WIDTH / 2
    
    /**
     * 右上角 Y 坐标
     */
    val northEastY:Int = centerY - HEIGHT / 2
    
    /**
     * 左下角 X 坐标
     */
    val southWestX:Int = centerX - WIDTH / 2
    
    /**
     * 左下角 Y 坐标
     */
    val southWestY:Int = centerY + HEIGHT / 2
    
    /**
     * 右下角 X 坐标
     */
    val southEastX:Int = centerX + WIDTH / 2
    
    /**
     * 右下角 Y 坐标
     */
    val southEastY:Int = centerY + HEIGHT / 2
    
    /**
     * 左顶点 X 坐标
     */
    val westX:Int = centerX - WIDTH / 2
    
    /**
     * 左顶点 Y 坐标
     */
    val westY:Int = centerY
    
    /**
     * 上顶点 X 坐标
     */
    val northX:Int = centerX
    
    /**
     * 上顶点 Y 坐标
     */
    val northY:Int = centerY - HEIGHT / 2
    
    /**
     * 右顶点 X 坐标
     */
    val eastX:Int = centerX + WIDTH / 2
    
    /**
     * 右顶点 Y 坐标
     */
    val eastY:Int = centerY
    
    /**
     * 下顶点 X 坐标
     */
    val southX:Int = centerX
    
    /**
     * 下顶点 Y 坐标
     */
    val southY:Int = centerY + HEIGHT / 2
    
    /**
     * 点（子单元格）
     */
    val spots = mutableListOf(
        // 0 号点，位于单元格中心，等同于单元格，不使用
        CellSpot(centerX, centerY),
        // 1 号点，位于单元格上方，不使用
        CellSpot(centerX, centerY - CellSpot.HEIGHT / 2),
        // 2 号点，位于单元格右侧
        CellSpot(centerX + CellSpot.WIDTH / 2, centerY),
        // 3 号点，位于单元格下方
        CellSpot(centerX, centerY + CellSpot.HEIGHT / 2),
        // 4 号点，位于单元格左侧
        CellSpot(centerX - CellSpot.WIDTH / 2, centerY),
    )
    
    companion object {
        /**
         * 宽度
         *
         * 根据经验，在不同的游戏中单元格宽度是不同的
         *
         * 参考 [ModEnc 关于 TMP 文件 Block 属性说明](https://modenc.renegadeprojects.com/TMP)
         */
        const val WIDTH:Int = 60
        
        /**
         * 高度
         *
         * 根据经验，在不同的游戏中单元格高度是不同的
         *
         * 参考 [ModEnc 关于 TMP 文件 Block 属性说明](https://modenc.renegadeprojects.com/TMP)
         */
        const val HEIGHT:Int = 30
    }
}
