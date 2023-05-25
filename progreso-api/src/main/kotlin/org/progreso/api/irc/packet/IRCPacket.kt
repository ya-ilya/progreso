package org.progreso.api.irc.packet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.progreso.api.irc.gson.RuntimeTypeAdapterFactory
import org.progreso.api.irc.packet.packets.IRCAuthFailedPacket
import org.progreso.api.irc.packet.packets.IRCAuthPacket
import org.progreso.api.irc.packet.packets.IRCMessagePacket

interface IRCPacket {
    companion object {
        private val GSON_FACTORY = RuntimeTypeAdapterFactory.of(IRCPacket::class.java)
            .registerSubtype(IRCAuthFailedPacket::class.java)
            .registerSubtype(IRCAuthPacket::class.java)
            .registerSubtype(IRCMessagePacket::class.java)

        val GSON: Gson = GsonBuilder()
            .registerTypeAdapterFactory(GSON_FACTORY)
            .create()
    }
}