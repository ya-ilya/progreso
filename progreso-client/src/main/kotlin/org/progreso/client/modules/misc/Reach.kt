package org.progreso.client.modules.misc

import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category

@AbstractModule.Register("Reach", Category.Misc)
object Reach : AbstractModule() {
    val range by setting("Range", 6, 1..10)
}