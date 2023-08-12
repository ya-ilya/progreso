package org.progreso.client.modules.client

import net.minecraft.client.util.InputUtil
import org.progreso.api.module.AbstractModule
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.clickgui.ClickGUI
import java.awt.Color

@AbstractModule.AutoRegister
object ClickGUI : AbstractModule() {
    val scrollSpeed by setting("ScrollSpeed", 15, 0..30)
    val mainColor by setting("MainColor", Color.RED)
    val rectColor by setting("RectColor", Color(0, 0, 0, 130))
    val customFont by setting("CustomFont", true)
    val descriptions by setting("Descriptions", true)

    init {
        bind = InputUtil.GLFW_KEY_RIGHT_SHIFT

        onEnable {
            mc.setScreen(ClickGUI)
            toggle()
        }
    }
}