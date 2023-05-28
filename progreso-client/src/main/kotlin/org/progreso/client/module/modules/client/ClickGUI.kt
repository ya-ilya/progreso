package org.progreso.client.module.modules.client

import net.minecraft.client.util.InputUtil
import org.progreso.client.gui.clickgui.ClickGUI
import org.progreso.client.module.Category
import org.progreso.client.module.Module
import java.awt.Color

object ClickGUI : Module("ClickGUI", Category.Client) {
    val scrollSpeed by setting("ScrollSpeed", 15, 0..30)
    val theme by setting("Theme", Color.RED)
    val rectColor by setting("RectColor", Color(0, 0, 0, 130))

    init {
        bind = InputUtil.GLFW_KEY_RIGHT_SHIFT

        onEnable {
            mc.setScreen(ClickGUI)
            toggle()
        }
    }
}