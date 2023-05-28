package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import net.minecraft.util.Util
import org.progreso.api.plugin.AbstractPlugin
import org.progreso.client.gui.minecraft.widgets.ProgresoPluginInfoWidget
import org.progreso.client.gui.minecraft.widgets.ProgresoPluginListWidget
import java.io.File
import java.nio.file.Paths
import kotlin.properties.Delegates

class ProgresoPluginsScreen(private val plugins: List<AbstractPlugin>) : Screen(Text.of("Plugins")) {
    private var listWidget by Delegates.notNull<ProgresoPluginListWidget>()
    private var infoWidget by Delegates.notNull<ProgresoPluginInfoWidget>()

    private val folder = Paths.get("progreso${File.separator}plugins").toFile()

    var selectedPlugin: AbstractPlugin? = null

    override fun init() {
        listWidget = ProgresoPluginListWidget(200, height, plugins, this)
        listWidget.setLeftPos(width / 2 - 4 - 200)
        addSelectableChild(listWidget)

        infoWidget = ProgresoPluginInfoWidget(200, height, this)
        infoWidget.setLeftPos(width / 2 + 4)
        addSelectableChild(infoWidget)

        addDrawableChild(
            ButtonWidget.builder(Text.of("Open Folder")) { Util.getOperatingSystem().open(folder) }
                .dimensions(width / 2 - 154, height - 48, 150, 20)
                .build()
        )

        addDrawableChild(
            ButtonWidget.builder(Text.of("Done")) { close() }
                .dimensions(width / 2 + 4, height - 48, 150, 20)
                .build()
        )
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackgroundTexture(matrices)
        listWidget.render(matrices, mouseX, mouseY, delta)
        infoWidget.render(matrices, mouseX, mouseY, delta)
        drawCenteredTextWithShadow(matrices, textRenderer, title, width / 2, 8, 16777215)
        super.render(matrices, mouseX, mouseY, delta)
    }

    fun selectPlugin(plugin: AbstractPlugin?) {
        selectedPlugin = plugin
    }
}