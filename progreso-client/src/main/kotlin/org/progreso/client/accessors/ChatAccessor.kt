package org.progreso.client.accessors

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.progreso.api.accessor.ChatAccessor
import org.progreso.client.Client.Companion.mc

object ChatAccessor : ChatAccessor {
    override fun send(message: Any, overlay: Boolean) {
        if (overlay) {
            mc.player.sendOverlayMessage(Component.literal(message.toString()))
        } else {
            mc.player.sendSystemMessage(Component.literal(message.toString()))
        }
    }

    override fun info(message: Any) {
        send("${ChatFormatting.GRAY}$message")
    }

    override fun warn(message: Any) {
        send("${ChatFormatting.YELLOW}$message")
    }

    override fun error(message: Any) {
        send("${ChatFormatting.RED}$message")
    }
}