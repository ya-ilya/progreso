package org.progreso.client.accessors

import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextComponentString
import org.progreso.api.accessor.ChatAccessor

object ChatAccessor : ChatAccessor {
    private val mc: Minecraft = Minecraft.getMinecraft()

    override fun send(message: Any) {
        mc.player?.sendMessage(TextComponentString(message.toString()))!!
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

    override fun addToSentMessages(message: Any) {
        mc.ingameGUI?.chatGUI?.addToSentMessages(message.toString())!!
    }
}