package org.progreso.api.config

/**
 * Config provider abstract class
 */
abstract class AbstractConfigProvider<T : AbstractConfig> {
    abstract fun create(name: String): T
    abstract fun apply(config: T)
}