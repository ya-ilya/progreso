package org.progreso.client.module

import net.minecraft.client.Minecraft
import org.progreso.api.event.Event
import org.progreso.api.event.EventListener
import org.progreso.api.event.EventPriority
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client
import org.progreso.client.events.client.ModuleEvent

abstract class Module(
    name: String,
    description: String,
    category: Category
) : AbstractModule(name, description, category) {
    private var enableBlock: () -> Unit = { }
    private var disableBlock: () -> Unit = { }

    constructor(name: String, category: Category) : this(name, "", category)

    protected companion object {
        val mc: Minecraft = Minecraft.getMinecraft()
    }

    override fun onEnable() {
        if (Client.EVENT_BUS.post(ModuleEvent(this))) {
            return
        }

        enableBlock.invoke()
    }

    override fun onDisable() {
        if (Client.EVENT_BUS.post(ModuleEvent(this))) {
            return
        }

        disableBlock.invoke()
    }

    protected fun onEnable(block: () -> Unit) {
        enableBlock = block
    }

    protected fun onDisable(block: () -> Unit) {
        disableBlock = block
    }

    @Suppress("RedundantSamConstructor")
    protected inline fun <reified T : Event> eventListener(crossinline block: (T) -> Unit) {
        Client.EVENT_BUS.registerListener(
            instanceClass = javaClass,
            eventClass = T::class.java,
            priority = EventPriority.NORMAL,
            listener = EventListener<T> {
                block(it)
            }
        )
    }

    protected inline fun <reified T : Event> safeEventListener(crossinline block: (T) -> Unit) {
        eventListener<T> {
            if (mc.player != null && mc.world != null) {
                block(it)
            }
        }
    }
}