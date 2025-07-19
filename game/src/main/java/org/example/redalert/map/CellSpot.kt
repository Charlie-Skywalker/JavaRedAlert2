package org.example.redalert.map

/**
 * 单元格内的点（子单元格）
 *
 * 参考 [ModEnc 关于 CellSpot 说明](https://modenc.renegadeprojects.com/Cell_Spots)
 */
data class CellSpot(
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
    
    companion object {
        /**
         * 宽度，等于单元格宽度的一半
         */
        const val WIDTH:Int = Cell.WIDTH / 2
        
        /**
         * 高度，等于单元格高度的一半
         */
        const val HEIGHT:Int = Cell.HEIGHT / 2
    }
}
