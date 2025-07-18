package org.example.redalert.utils

/**
 * 颜色工具类
 */
object ColorUtil {
    /**
     * 真彩色转高彩色
     * @param color 高彩色
     * @param isGreen 是否为绿色
     * @return 真彩色
     */
    @JvmStatic
    @JvmOverloads
    fun convertHighToTrue(color:Byte, isGreen:Boolean = false):Byte {
        return if (isGreen) {
            color.toInt() shl 2
        } else {
            color.toInt() shl 3
        }.toByte()
    }
    
    /**
     * 高彩色转真彩色
     * @param color 真彩色
     * @param isGreen 是否为绿色
     * @return 高彩色
     */
    @JvmStatic
    @JvmOverloads
    fun convertTrueToHigh(color:Byte, isGreen:Boolean = false):Byte {
        return if (isGreen) {
            color.toInt() shr 2
        } else {
            color.toInt() shr 3
        }.toByte()
    }
}
