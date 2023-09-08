package org.progreso.client.commands

import org.progreso.api.command.AbstractCommand
import org.progreso.api.command.argument.arguments.NumberArgumentType.Companion.number
import org.progreso.api.command.argument.arguments.StringArgumentType.Companion.string
import org.progreso.client.Client
import org.progreso.client.Client.Companion.config
import org.progreso.client.gui.createDefaultTextRenderer
import org.progreso.client.gui.customTextRenderer
import org.progreso.client.managers.minecraft.ProgresoResourceManager
import org.progreso.client.managers.minecraft.exceptions.ProgresoResourceManagerException
import org.progreso.client.util.render.TextRendererUtil

@AbstractCommand.Register("font")
object FontCommand : AbstractCommand() {
    init {
        literal("load").argument("font", string()) {
            argument("size", number<Float>()).executes { context ->
                loadFont(
                    context.get("font"),
                    context.get("size")
                )
            }

            executes { context ->
                loadFont(context.get("font"))
            }
        }

        literal("reset").executes { _ ->
            customTextRenderer = createDefaultTextRenderer()
            config.customFont = null

            infoLocalized("command.font.reset")
        }

        literal("list").executes { _ ->
            infoLocalized(
                "command.font.list",
                ProgresoResourceManager.fonts.joinToString()
            )
        }

        executes { _ ->
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
            customTextRenderer = TextRendererUtil.createTextRendererFromProgresoResource(fontName, size)!!
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