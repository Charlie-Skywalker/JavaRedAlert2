package org.example.redalert.resources.mix

class TDMixFile(
    /**
     * 文件头部分
     */
    val header:Header = Header(),
    
    /**
     * 索引部分
     */
    val indices:MutableList<Index> = mutableListOf(),
    
    /**
     * 文件部分
     */
    val files:MutableMap<Int, ByteArray> = mutableMapOf(),
) {
    /**
     * 文件头定义
     */
    class Header(
        /**
         * 文件数
         */
        var count:Int = 0,
        /**
         * Body 大小
         */
        var size:Int = 0,
    )
    
    /**
     * 索引定义
     */
    class Index(
        /**
         * ID 用于标识文件，替代传统的字符串 Name
         */
        var id:Int = 0,
        /**
         * 文件起始字节相对于 Body 起始字节的偏移量
         */
        var offset:Int = 0,
        /**
         * 文件大小
         */
        var size:Int = 0,
    )
}
