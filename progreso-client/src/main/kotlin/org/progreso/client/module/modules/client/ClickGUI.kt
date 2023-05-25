package org.progreso.client.module.modules.client

import org.lwjgl.input.Keyboard
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import java.awt.Color

object ClickGUI : Module("ClickGUI", Category.Client) {
    val customFont by setting("CustomFont", true)
    val scrollSpeed by setting("ScrollSpeed", 15, 0..30)
    val theme by setting("Theme", Color.RED)
    val rectColor by setting("RectColor", Color(0, 0, 0, 130))

    init {
        bind.value = Keyboard.KEY_RSHIFT

        onEnable {
            mc.displayGuiScreen(ClickGUI)
            toggle()
        }
    }
}