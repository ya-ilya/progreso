package org.progreso.client.module.modules.client

import org.lwjgl.input.Keyboard
import org.progreso.client.Client
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import java.awt.Color

class ClickGUI : Module("ClickGUI", Category.Client) {
    val customFont by setting("CustomFont", true)
    val scrollSpeed by setting("ScrollSpeed", 15, 0..30)
    val theme by setting("Theme", Color.RED)

    init {
        bind.value = Keyboard.KEY_RSHIFT

        onEnable {
            mc.displayGuiScreen(Client.CLICK_GUI)
            toggle()
        }
    }
}