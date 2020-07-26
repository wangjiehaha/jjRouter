package com.cv.led.annotation

class ServiceImpl(
    private val key: String?,
    private val implementation: String = "",
    private val implementationClazz: Class<*>?,
    private val singleton: Boolean
) {

    constructor(
        key: String?,
        implementation: String,
        singleton: Boolean
    ) : this(key ?: implementation, implementation, null, singleton)

    fun toConfig(): String {
        var s = key + SPLITTER + implementation
        if (singleton) {
            s += SPLITTER + SINGLETON
        }
        return s
    }

    override fun toString(): String {
        return implementation
    }

    companion object {
        const val SPLITTER = ":"
        const val SINGLETON = "singleton"
        private const val DEFAULT_IMPL_KEY = "_service_default_impl"

        fun checkConflict(
            interfaceName: String?,
            impl: ServiceImpl?,
            previous: ServiceImpl?
        ): String? {
            if (impl != null && previous != null && (previous.implementation == impl.implementation)) {
                return if (DEFAULT_IMPL_KEY == impl.key) {
                    "接口${interfaceName}的默认实现只允许纯在一个\n目前存在多个默认的实现${previous}, $impl"
                } else {
                    "接口${interfaceName}对应key=${impl.key}存在多个实现：${previous}, $impl"
                }
            }
            return null
        }
    }
}