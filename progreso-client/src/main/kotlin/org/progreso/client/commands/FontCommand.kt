package org.progreso.client.commands

import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.progreso.api.command.AbstractCommand
import org.progreso.client.Client
import org.progreso.client.Client.Companion.config
import org.progreso.client.gui.createDefaultTextRenderer
import org.progreso.client.gui.customTextRenderer
import org.progreso.client.managers.minecraft.ProgresoResourceManager
import org.progreso.client.managers.minecraft.exceptions.ProgresoResourceManagerException
import org.progreso.client.util.render.createTextRendererFromProgresoResource

@AbstractCommand.Register("font")
object FontCommand : AbstractCommand() {
    override fun build(builder: LiteralArgumentBuilder<Any>) {
        builder.then(
            literal("load").then(
                argument("font", StringArgumentType.string())
                    .then(
                        argument("size", FloatArgumentType.floatArg()).executesSuccess { context ->
                            loadFont(
                                StringArgumentType.getString(context, "font"),
                                FloatArgumentType.getFloat(context, "size")
                            )
                        }
                    )
                    .executesSuccess { context ->
                        loadFont(StringArgumentType.getString(context, "font"))
                    }
            )
        )

        builder.then(
            literal("reset").executesSuccess {
                customTextRenderer = createDefaultTextRenderer()
                config.customFont = null

                infoLocalized("command.font.reset")
            }
        )

        builder.then(
            literal("list").executesSuccess {
                infoLocalized(
                    "command.font.list",
                    ProgresoResourceManager.fonts.joinToString()
                )
            }
        )

        builder.executesSuccess {
            if (config.customFont != null) {
                infoLocalized(
                    "command.font.current",
                    config.customFont!!.name!!,
                    config.customFont!!.size!!
                )
            } else {
                errorLocalized("command.font.current_error")
            }
        }
    }

    private fun loadFont(fontName: String, size: Float = 11f) {
        try {
            customTextRenderer = createTextRendererFromProgresoResource(fontName, size)!!
            config.customFont = Client.ProgresoGlobalConfigAccessor.GlobalConfig.CustomFont(fontName, size)

            infoLocalized(
                "command.font.load",
                fontName
            )
        } catch (ex: ProgresoResourceManagerException) {
            errorLocalized(
                "command.font.not_found_error",
                fontName
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            errorLocalized(
                "command.font.unknown_error",
                fontName
            )
        }
    }
}