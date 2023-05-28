package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import org.progreso.api.managers.CommandManager
import org.progreso.client.module.modules.client.ClickGUI
import org.progreso.client.util.render.Render2DUtil
import java.awt.Color

class ProgresoChatScreen : ChatScreen(CommandManager.PREFIX.toString()) {
    private var predict = emptyList<String>()
    private val mc get() = client!!

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        val chatHud = mc.inGameHud.chatHud

        when (keyCode) {
            InputUtil.GLFW_KEY_ESCAPE -> mc.setScreen(null)

            InputUtil.GLFW_KEY_ENTER, InputUtil.GLFW_KEY_KP_ENTER -> {
                mc.setScreen(null)
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
                    ChatScreen(chatField.text).also { mc.setScreen(it) }
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
            ChatScreen(chatField.text).also { mc.setScreen(it) }
            return true
        }

        predict()

        return true
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        Render2DUtil.drawBorderedRect(matrices, 2, height - 14, width - 4, 12, Color(Int.MIN_VALUE), ClickGUI.theme)

        if (predict.isNotEmpty()) {
            textRenderer.drawWithShadow(
                matrices,
                predict.joinToString("/"),
                (textRenderer.getWidth(chatField.text) + chatField.x).toFloat(),
                chatField.y.toFloat(),
                ClickGUI.theme.rgb
            )
        }

        chatField.render(matrices, mouseX, mouseY, delta)
    }

    private fun predict() {
        val message = chatField.text.removePrefix(CommandManager.PREFIX.toString())
        val predict = CommandManager.DISPATCHER.predictNode(message)
        val variants = CommandManager.DISPATCHER.getVariants(message)

        this.predict = when {
            predict != null -> listOf(predict.name.removePrefix(message.split(" ").last()))
            variants != null -> variants
            else -> emptyList()
        }
    }
}