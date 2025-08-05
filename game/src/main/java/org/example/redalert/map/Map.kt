package org.example.redalert.map

/**
 * 地图
 */
class Map(
    /**
     * 宽度
     *
     * 尺寸含义参考 [ModEnc 关于 Map 文件 IsoMapPack5 Section 的解释](https://modenc.renegadeprojects.com/IsoMapPack5)
     */
    val width:Int,
    /**
     * 高度
     *
     * 尺寸含义参考 [ModEnc 关于 Map 文件 IsoMapPack5 Section 的解释](https://modenc.renegadeprojects.com/IsoMapPack5)
     */
    val height:Int,
) {
    /**
     * 单元格列表
     */
    val cells = mutableListOf<Cell>()
    
    /**
     * 点列表，用来进行寻路
     */
    val spots = mutableListOf<CellSpot>()
    
    init {
        // 尺寸和单元格对应关系参考 [ModEnc 关于 Map 文件 IsoMapPack5 Section 的解释](https://modenc.renegadeprojects.com/IsoMapPack5)
        // val maxX = (this.width * 2) - 1
        val maxX = this.width
        val maxY = this.height
        
        // 逐行创建
        var centerX = 0
        var centerY = Cell.HEIGHT / 2
        repeat(maxY) {
            // 起始单元格中心点从 (Cell.WIDTH / 2, Cell.HEIGHT / 2) 开始，每次递增 (Cell.WIDTH, Cell.HEIGHT)
            centerX = Cell.WIDTH / 2
            repeat(maxX) {
                // 循环创建单元格
                val cell = Cell(centerX, centerY)
                this.cells.add(cell)
                this.spots.addAll(cell.spots)
                centerX += Cell.WIDTH
            }
            centerY += Cell.HEIGHT
        }
    }
}
