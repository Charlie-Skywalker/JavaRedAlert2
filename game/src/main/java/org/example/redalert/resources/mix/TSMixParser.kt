package org.example.redalert.resources.mix

import java.io.File
import java.io.RandomAccessFile

/**
 * 红色警戒2
 * 参考 [XCC 关于 MIX 文件格式的文档](https://xhp.xwis.net/documents/)
 */
object TSMixParser {
    @JvmStatic
    fun parse(file:File) {
        parse(RandomAccessFile(file, "r"))
    }
    
    fun parse(reader:RandomAccessFile):TSMixFile {
        return reader.use { reader ->
            val mix = TSMixFile()
            // Flags 部分
            val flags = reader.readShort().toInt()
            mix.isChecksum = flags and TSMixFile.CHECKSUM_MASK != 0
            mix.isEncrypted = flags and TSMixFile.ENCRYPTED_MASK != 0
            
            // Header 部分
            if (mix.isEncrypted) {
                // 读取 Key Source
                reader.read(mix.secret)
                
                // 读取加密的 Header Size 部分
                // reader.read(3)
            } else {
                // 直接读取未加密的 Header
                mix.header.count = reader.readShort().toInt() // 2 字节，内部文件数量
                mix.header.size = reader.readInt() // 4 字节，Body 大小
            }
            
            // Indices 部分
            repeat(mix.header.count) {
                val index = TSMixFile.Index()
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
