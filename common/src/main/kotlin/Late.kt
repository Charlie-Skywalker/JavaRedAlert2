package xyz.slkagura.kotlin

import kotlin.reflect.KProperty

/**
 * 参考 [Lazy]
 */
interface Late<V> {
    var value:V
    fun isInitialized():Boolean
}

/**
 * 参考 [getValue]
 */
inline operator fun <reified T, reified V> Late<V>.getValue(thisRef:T, property:KProperty<*>):V {
    return this.value
}

/**
 * 为 [Late] 扩展 setX 方法
 */
inline operator fun <reified T, reified V> Late<V>.setValue(thisRef:T, property:KProperty<*>, value:V) {
    this.value = value
}

/**
 * 参考 [lazy]
 */
inline fun <reified V> late(noinline initializer:(()->V)? = null):Late<V> = SynchronizedLateImpl(initializer)

internal object UninitializedValue

class SynchronizedLateImpl<V>(
    @Volatile
    private var initializer:(()->V)? = null,
    lock:Any? = null,
):Late<V> {
    @Volatile
    private var _value:Any? = UninitializedValue
    
    // final field to ensure safe publication of 'SynchronizedLazyImpl' itself through
    // var late = late() {}
    private val lock = lock ?: this
    
    override var value:V
        get() {
            val v1 = _value
            if (v1 !== UninitializedValue) {
                @Suppress("UNCHECKED_CAST")
                return v1 as V
            }
            
            return synchronized(lock) {
                val v2 = _value
                if (v2 !== UninitializedValue) {
                    @Suppress("UNCHECKED_CAST") (v2 as V)
                } else {
                    val typedValue = initializer?.invoke() ?: throw UninitializedPropertyAccessException("Late value not initialized yet.")
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }
        set(value) {
            _value = value
        }
    
    override fun isInitialized():Boolean = _value !== UninitializedValue
    override fun toString():String = if (isInitialized()) value.toString() else "Late value not initialized yet."
}
