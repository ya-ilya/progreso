package org.progreso.api.config

/**
 * Config abstract class
 *
 * @param name Config name
 */
abstract class AbstractConfig(val name: String) {
    override fun toString(): String {
        return name
    }
}