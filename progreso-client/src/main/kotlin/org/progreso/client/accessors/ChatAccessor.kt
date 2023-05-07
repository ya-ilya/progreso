package org.progreso.client.accessors

import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextComponentString
import org.progreso.api.accessor.ChatAccessor

object ChatAccessor : ChatAccessor {
    private val mc: Minecraft = Minecraft.getMinecraft()

    override fun send(message: Any) {
        mc.player?.sendMessage(TextComponentString(message.toString()))!!
    }

    override fun addToSentMessages(message: Any) {
        mc.ingameGUI?.chatGUI?.addToSentMessages(message.toString())!!
    }
}