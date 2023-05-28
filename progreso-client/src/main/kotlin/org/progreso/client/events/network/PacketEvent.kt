package org.progreso.client.events.network

import net.minecraft.network.packet.Packet
import org.progreso.api.event.Event

sealed class PacketEvent<T : Packet<*>>(val packet: T) : Event() {
    class Receive<T : Packet<*>>(packet: T) : PacketEvent<T>(packet)
    class Send<T : Packet<*>>(packet: T) : PacketEvent<T>(packet)
}