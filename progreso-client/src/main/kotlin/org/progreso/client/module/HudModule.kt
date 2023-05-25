package org.progreso.client.module

import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.progreso.api.module.AbstractHudModule
import org.progreso.client.gui.clickgui.HudEditor
import org.progreso.client.module.modules.client.ClickGUI
import org.progreso.client.util.Render2DUtil.drawRect

abstract class HudModule(
    name: String,
    description: String,
    category: Category
) : AbstractHudModule(name, description, category) {
    constructor(name: String, category: Category) : this(name, "", category)

    protected companion object {
        val mc: Minecraft = Minecraft.getMinecraft()
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Text) {
        if (mc.currentScreen is HudEditor) {
            drawRect(x, y, width, height, ClickGUI.rectColor)
        }

        render()
    }
}