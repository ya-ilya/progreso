package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.InputUtil
import org.progreso.api.managers.CommandManager
import org.progreso.client.gui.drawBorderedRect
import org.progreso.client.gui.drawText
import org.progreso.client.modules.client.ClickGUI
import java.awt.Color

class ProgresoChatScreen : ChatScreen(CommandManager.PREFIX.toString()) {
    private var predict = emptyList<String>()

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        val chatHud = client!!.inGameHud.chatHud

        when (keyCode) {
            InputUtil.GLFW_KEY_ESCAPE -> client!!.setScreen(null)

            InputUtil.GLFW_KEY_ENTER, InputUtil.GLFW_KEY_KP_ENTER -> {
                client!!.setScreen(null)
                val message = this.chatField.text
                if (message.isNotEmpty()) sendMessage(message, true)
            }

            InputUtil.GLFW_KEY_TAB -> {
                if (predict.isEmpty()) return true
                chatField.text = chatField.text + predict[0]
                predict = emptyList()
            }

            InputUtil.GLFW_KEY_UP -> setChatFromHistory(-1)
            InputUtil.GLFW_KEY_DOWN -> setChatFromHistory(1)
            InputUtil.GLFW_KEY_PAGE_UP -> chatHud.scroll(chatHud.visibleLineCount - 1)
            InputUtil.GLFW_KEY_PAGE_DOWN -> chatHud.scroll(-chatHud.visibleLineCount + 1)

            InputUtil.GLFW_KEY_BACKSPACE -> {
                chatField.keyPressed(keyCode, scanCode, modifiers)

                if (chatField.text.isEmpty()) {
                    ChatScreen(chatField.text).also { client!!.setScreen(it) }
                    return true
                }

                predict()
            }

            else -> chatField.keyPressed(keyCode, scanCode, modifiers)
        }

        return true
    }

    override fun charTyped(chr: Char, modifiers: Int): Boolean {
        chatField.charTyped(chr, modifiers)

        if (chatField.text.isEmpty()) {
            ChatScreen(chatField.text).also { client!!.setScreen(it) }
            return true
        }

        predict()

        return true
    }

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        context.drawBorderedRect(2, height - 14, width - 4, 12, Color(Int.MIN_VALUE), ClickGUI.theme)

        if (predict.isNotEmpty()) {
            context.drawText(
                predict.joinToString("/"),
                textRenderer.getWidth(chatField.text) + chatField.x,
                chatField.y,
                ClickGUI.theme
            )
        }

        chatField.render(context, mouseX, mouseY, delta)
    }

    private fun predict() {
        val message = chatField.text.removePrefix(CommandManager.PREFIX.toString())
        val predict = CommandManager.DISPATCHER.predict(message)
        val variants = CommandManager.DISPATCHER.variants(message)

        this.predict = when {
            predict != null -> listOf(predict.name.removePrefix(message.split(" ").last()))
            variants != null -> variants.map { it.toString() }
            else -> emptyList()
        }
    }
}