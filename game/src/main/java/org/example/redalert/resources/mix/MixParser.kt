package org.example.redalert.resources.mix

import java.io.File
import java.io.RandomAccessFile

/**
 * 参考 [XCC 关于 MIX 文件格式的文档](https://xhp.xwis.net/documents/)
 */
object MixParser {
    @JvmStatic
    fun parse(file:File) {
        parse(RandomAccessFile(file, "r"))
    }
    
    fun parse(reader:RandomAccessFile):TDMixFile {
        return reader.use { reader ->
            val mix = TDMixFile()
            // Header 部分
            mix.header.count = reader.readShort().toInt() // 2 字节，内部文件数量
            mix.header.size = reader.readInt() // 4 字节，Body 大小
            
            // Indices 部分
            repeat(mix.header.count) {
                val index = TDMixFile.Index()
                index.id = reader.readInt()
                index.offset = reader.readInt()
                index.size = reader.readInt()
                mix.indices.add(index)
            }
            
            mix.indices.forEach { index ->
                val bytes = ByteArray(index.size)
                reader.read(bytes)
                mix.files.put(index.id, bytes)
            }
            
            mix
        }
    }
}
