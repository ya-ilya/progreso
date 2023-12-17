package org.progreso.client.events.misc

import net.minecraft.client.sound.SoundInstance
import org.progreso.api.event.Event

data class SoundEvent(val sound: SoundInstance) : Event()