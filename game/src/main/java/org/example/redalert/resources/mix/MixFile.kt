package org.example.redalert.resources.mix

/**
 * MIX 文件
 */
sealed class MixFile {
    
    class Flag(
        /**
         * 标志位
         */
        val flag:Int
    ) {
        companion object {
            /**
             * 校验和掩码
             *
             * 在 Body 内容之后存在 20 字节的校验和内容，可以被忽略或删除
             */
            const val CHECKSUM_MASK = 0x00010000
            
            /**
             * 加密 Flag 掩码
             *
             * 在 Flag 之后，存在 80 字节的加密
             */
            const val ENCRYPTED_MASK = 0x00020000
        }
    }
    
    /**
     * 用于计算与 blowfish 加密一起使用的密钥
     */
    class KeySource(
        val bytes:ByteArray,
    )
    
    class Body(
    
    )
}
