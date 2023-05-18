package org.progreso.client.mixins

import net.minecraft.entity.Entity
import org.progreso.client.mixin.mixins.accessors.AccessorEntity

val Entity.isInWeb
    get() = (this as AccessorEntity).isInWeb