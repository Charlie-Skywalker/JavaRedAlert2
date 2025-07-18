package org.example.redalert.utils

import java.io.RandomAccessFile

/**
 * 读取工具类
 */
object ReadUtil {
    /**
     * 读取数据并转成 int 类型
     * @param reader 读取器
     * @param length 长度，最长为 4 byte
     * @param buffer 缓冲
     * @param isLittleEndian 使用小端序
     * @return 读取结果
     */
    @JvmStatic
    @JvmOverloads
    fun readToInt(
        reader:RandomAccessFile,
        length:Int,
        buffer:ByteArray = ByteArray(4),
        isLittleEndian:Boolean = true,
    ):Int {
        if (length < 1 || length > 4) {
            throw IllegalArgumentException("Length must be between 0 and 4")
        }
        reader.read(buffer, 0, length)
        var result = 0
        if (isLittleEndian) {
            buffer.forEach {
                result = (result shl 8) or it.toInt()
            }
        } else {
            buffer.forEachIndexed { index, byte ->
                result = result or (byte.toInt() shl (8 * index + 8))
            }
        }
        return result
    }
}
