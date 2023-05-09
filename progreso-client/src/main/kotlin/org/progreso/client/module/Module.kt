package org.progreso.client.module

import net.minecraft.client.Minecraft
import org.progreso.api.module.AbstractModule

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
        enableBlock.invoke()
    }

    override fun onDisable() {
        disableBlock.invoke()
    }

    protected fun onEnable(block: () -> Unit) {
        enableBlock = block
    }

    protected fun onDisable(block: () -> Unit) {
        disableBlock = block
    }
}