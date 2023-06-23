package org.progreso.api.module

import org.progreso.api.Api
import org.progreso.api.event.events.ModuleEvent
import org.progreso.api.setting.AbstractSetting
import org.progreso.api.setting.container.SettingContainer

/**
 * Module abstract class
 */
abstract class AbstractModule : SettingContainer {
    override val settings = mutableSetOf<AbstractSetting<*>>()

    private var enableBlock: () -> Unit = { }
    private var disableBlock: () -> Unit = { }

    var bind by setting("Bind", 0)
    var enabled by setting("Enabled", false) { false }.apply {
        valueChanged { _, newValue ->
            if (newValue) {
                Api.EVENT.register(this@AbstractModule)
                Api.API_EVENT_BUS.post(ModuleEvent.Toggled(this@AbstractModule))
                enableBlock()
            } else {
                Api.EVENT.unregister(this@AbstractModule)
                Api.API_EVENT_BUS.post(ModuleEvent.Toggled(this@AbstractModule))
                disableBlock()
            }
        }
    }

    private val annotation = javaClass.getAnnotation(Register::class.java)

    val name = annotation.name
    val description = annotation.description
    val category = annotation.category

    protected fun onEnable(block: () -> Unit) {
        enableBlock = block
    }

    protected fun onDisable(block: () -> Unit) {
        disableBlock = block
    }

    fun toggle() {
        enabled = !enabled
    }

    @Target(AnnotationTarget.CLASS)
    annotation class Register(
        val name: String,
        val category: Category,
        val description: String = ""
    )
}