package org.progreso.client.modules.client

import net.minecraft.client.util.InputUtil
import org.progreso.api.module.AbstractModule
import org.progreso.api.module.Category
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.clickgui.ClickGUI
import java.awt.Color

@AbstractModule.Register("ClickGUI", Category.Client)
object ClickGUI : AbstractModule() {
    val scrollSpeed by setting("ScrollSpeed", 15, 0..30)
    val theme by setting("Theme", Color.RED)
    val rectColor by setting("RectColor", Color(0, 0, 0, 130))
    val descriptions by setting("Descriptions", true)

    init {
        bind = InputUtil.GLFW_KEY_RIGHT_SHIFT

        onEnable {
            mc.setScreen(ClickGUI)
            toggle()
        }
    }
}