package org.example.redalert.resources.mix

class TSMixFile(
    /**
     * 是否校验
     */
    var isChecksum:Boolean = false,
    
    /**
     * 是否加密
     */
    var isEncrypted:Boolean = false,
    
    /**
     * Blowfish Key Source
     */
    val secret:ByteArray = ByteArray(80),
    
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
    companion object {
        /**
         * 校验和掩码
         *
         * 在 Body 内容之后存在 20 字节的校验和内容，可以被忽略或删除
         */
        const val CHECKSUM_MASK = 0x00010000
        
        /**
         * 加密 Flags 掩码
         *
         * 在 Flags 之后，存在 80 字节的加密
         */
        const val ENCRYPTED_MASK = 0x00020000
    }
    
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
