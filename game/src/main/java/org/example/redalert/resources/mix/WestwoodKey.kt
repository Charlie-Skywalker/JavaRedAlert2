package org.example.redalert.resources.mix

object WestwoodKey {
    /**
     * Westwood Key Source 长度
     */
    const val SIZE = 80
    
    /**
     * Westwood Key Source 计算公钥
     */
    const val PUBLIC_KEY = "AihRvNoIbTn85FZRYNZRcT+i6KpU+maCsEqr3Q5q+LDB5tH7Tz2qQ38V"
    
    /**
     * Base64 用到的 Char -> Int 表
     */
    val b64Map = byteArrayOf(
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
        -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
        -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    )
    
    private fun initPublicKey() {
        val bytes = ByteArray(256)
        var i = 0
        var j = 0
        
        while (i < PUBLIC_KEY.length) {
            var tmp = b64Map[PUBLIC_KEY[i++].code].toInt() and 0xFF
            tmp = (tmp shl 6) or (b64Map[PUBLIC_KEY[i++].code].toInt() and 0xFF)
            tmp = (tmp shl 6) or (b64Map[PUBLIC_KEY[i++].code].toInt() and 0xFF)
            tmp = (tmp shl 6) or (b64Map[PUBLIC_KEY[i++].code].toInt() and 0xFF)
            
            bytes[j++] = ((tmp shr 16) and 0xFF).toByte()
            bytes[j++] = ((tmp shr 8) and 0xFF).toByte()
            bytes[j++] = (tmp  and 0xFF).toByte()
        }
    }
    
    /**
     * 从 Westwood Key Source 计算获取 Blowfish 密钥
     */
    fun getBlowfishKey(bytes:ByteArray):ByteArray {
        if (bytes.size != SIZE) return byteArrayOf()
        return bytes
    }
}
