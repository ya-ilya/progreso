package org.progreso.client.module

import org.progreso.api.module.AbstractModule
import org.progreso.client.Client

abstract class Module(
    name: String,
    description: String,
    category: Category
) : AbstractModule(name, description, category) {
    private var enableBlock: () -> Unit = { }
    private var disableBlock: () -> Unit = { }

    constructor(name: String, category: Category) : this(name, "", category)

    protected companion object {
        val mc by lazy { Client.mc }
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