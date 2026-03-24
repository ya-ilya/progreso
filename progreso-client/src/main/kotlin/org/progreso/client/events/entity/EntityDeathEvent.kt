package org.progreso.client.events.entity

import net.minecraft.world.entity.Entity
import org.progreso.api.event.Event

data class EntityDeathEvent(val entity: Entity) : Event()