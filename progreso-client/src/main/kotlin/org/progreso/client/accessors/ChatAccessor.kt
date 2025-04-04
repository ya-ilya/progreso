package org.progreso.client.accessors

import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.progreso.api.accessor.ChatAccessor
import org.progreso.client.Client.Companion.mc

object ChatAccessor : ChatAccessor {
    override fun send(message: Any, overlay: Boolean) {
        mc.player.sendMessage(Text.of(message.toString()), overlay)
    }

    override fun info(message: Any) {
        send("${Formatting.GRAY}$message")
    }

    override fun warn(message: Any) {
        send("${Formatting.YELLOW}$message")
    }

    override fun error(message: Any) {
        send("${Formatting.RED}$message")
    }
}