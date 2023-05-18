package org.progreso.client.gui.mc

import net.minecraft.client.gui.GuiChat
import org.lwjgl.input.Keyboard
import org.progreso.api.managers.CommandManager
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.mixins.historyBuffer
import org.progreso.client.mixins.sentHistoryCursor
import org.progreso.client.util.Render2DUtil
import java.awt.Color

class ProgresoGuiChat(
    defaultText: String,
    private val historyBufferIn: String? = null,
    private val sentHistoryCursorIn: Int? = null
) : GuiChat(defaultText) {
    private var predict = emptyList<String>()

    override fun initGui() {
        super.initGui()
        historyBufferIn?.let { historyBuffer = it }
        sentHistoryCursorIn?.let { sentHistoryCursor = it }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        val chatGUI = mc.ingameGUI.chatGUI

        when (keyCode) {
            Keyboard.KEY_ESCAPE -> mc.displayGuiScreen(null)

            Keyboard.KEY_RETURN, Keyboard.KEY_NUMPADENTER -> {
                val message = inputField.text.trim()
                if (message.isNotEmpty()) sendChatMessage(message)
                chatGUI.addToSentMessages(message)
                mc.displayGuiScreen(null)
            }

            Keyboard.KEY_TAB -> {
                if (predict.isEmpty()) return
                inputField.text = inputField.text + predict[0]
                predict = emptyList()
            }

            Keyboard.KEY_UP -> getSentHistory(-1)
            Keyboard.KEY_DOWN -> getSentHistory(1)
            Keyboard.KEY_PRIOR -> chatGUI.scroll(chatGUI.lineCount - 1)
            Keyboard.KEY_NEXT -> chatGUI.scroll(-chatGUI.lineCount + 1)

            else -> {
                inputField.textboxKeyTyped(typedChar, keyCode)

                if (inputField.text.isEmpty()) {
                    GuiChat(inputField.text).also {
                        mc.displayGuiScreen(it)
                        it.historyBuffer = historyBuffer
                        it.sentHistoryCursor = sentHistoryCursor
                    }

                    return
                }

                val message = inputField.text.removePrefix(CommandManager.PREFIX.toString())
                val predict = CommandManager.DISPATCHER.predictNode(message)
                val variants = CommandManager.DISPATCHER.getVariants(message)

                this.predict = when {
                    predict != null -> listOf(predict.name.removePrefix(message.split(" ").last()))
                    variants != null -> variants
                    else -> emptyList()
                }
            }
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        Render2DUtil.drawBorderedRect(2, height - 14, width - 4, 12, Color(Int.MIN_VALUE), ClickGUI.MODULE.theme)

        if (predict.isNotEmpty()) {
            fontRenderer.drawStringWithShadow(
                predict.joinToString("/"),
                (fontRenderer.getStringWidth(inputField.text) + inputField.x).toFloat(),
                inputField.y.toFloat(),
                ClickGUI.MODULE.theme.rgb
            )
        }

        inputField.drawTextBox()
    }
}