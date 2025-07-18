package org.example.redalert.utils

import java.io.IOException
import java.net.URL
import java.util.*

/**
 * Java 资源工具类
 */
object JavaResourcesUtil {
    /**
     * 获取 Java Resources 资源
     * @param name 资源名称
     * @return 所有资源链接，不存在时返回空枚举
     */
    @JvmStatic
    fun getResources(name:String):Enumeration<URL> {
        var resources = Collections.emptyEnumeration<URL>()
        try {
            resources = JavaResourcesUtil::class.java.getClassLoader().getResources(name)
        } catch (e:IOException) {
            e.printStackTrace()
        }
        return resources
    }
}
