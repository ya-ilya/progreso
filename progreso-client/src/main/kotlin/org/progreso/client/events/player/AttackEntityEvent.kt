package org.progreso.client.events.player

import net.minecraft.world.entity.Entity
import org.progreso.api.event.Event

data class AttackEntityEvent(val entity: Entity) : Event()