package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.widget.ButtonWidget
import org.progreso.api.alt.AbstractAltAccount
import org.progreso.api.alt.accounts.CrackedAltAccount
import org.progreso.api.managers.AltManager
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.builders.ButtonBuilder.Companion.button
import org.progreso.client.gui.builders.ElementListBuilder.Companion.elementList
import org.progreso.client.gui.builders.ScreenBuilder.Companion.screen
import org.progreso.client.gui.builders.TextFieldBuilder.Companion.textField
import org.progreso.client.gui.drawText
import org.progreso.client.gui.minecraft.common.SimpleElementListEntry
import org.progreso.client.gui.minecraft.common.TitledScreen
import org.progreso.client.util.session.SessionUtil
import java.awt.Color
import kotlin.properties.Delegates

class ProgresoAltsScreen(private val alts: List<AbstractAltAccount>) : TitledScreen("Alts") {
    private var selectedAlt: AbstractAltAccount? = null

    override fun init() {
        var loginButtonWidget by Delegates.notNull<ButtonWidget>()
        var removeButtonWidget by Delegates.notNull<ButtonWidget>()

        elementList<AltEntry> { list ->
            list.left = width / 2 - 100
            list.listDimension(200, height, 24, height - 55 + 4, 36)

            for (alt in alts) {
                list.addEntry(AltEntry(alt))
            }

            list.select { entry ->
                selectedAlt = entry?.alt
                removeButtonWidget.active = selectedAlt != null
                loginButtonWidget.active = selectedAlt != null
            }

            list.renderHeader { context, x, y ->
                val text = "Current name: ${mc.session.username}"

                context.drawText(
                    text,
                    x + list.width / 2 - client!!.textRenderer.getWidth(text) / 2,
                    27.coerceAtMost(y),
                    Color.WHITE
                )
            }
        }

        button("Add account") { button ->
            button.dimensions(width / 2 - 100, height - 48, 96, 20)
            button.onPress {
                showCreateAltScreen()
            }
        }

        removeButtonWidget = button("Remove account") { button ->
            button.active = false
            button.dimensions(width / 2 + 4, height - 48, 96, 20)
            button.onPress {
                AltManager.removeAlt(selectedAlt!!)
                client!!.setScreen(ProgresoAltsScreen(AltManager.alts))
            }
        }

        loginButtonWidget = button("Login") { button ->
            button.active = false
            button.dimensions(width / 2 - 100, height - 24, 96, 20)
            button.onPress {
                if (SessionUtil.login(selectedAlt!!) == SessionUtil.LoginResult.Successful) {
                    close()
                }
            }
        }

        button("Done") { button ->
            button.dimensions(width / 2 + 4, height - 24, 96, 20)
            button.onPress {
                close()
            }
        }
    }

    private fun showCreateAltScreen() {
        client!!.setScreen(screen("Create Alt") {
            init {
                val name = textField { textField ->
                    textField.dimensions(width / 2 - 36, height / 2 - 20, 100, 20)
                }

                button("Add account") { button ->
                    button.dimensions(width / 2 - 66, height / 2 + 8, 132, 20)
                    button.onPress {
                        if (name.text.length >= 3) {
                            AltManager.addAlt(CrackedAltAccount(name.text))
                            close()
                        }
                    }
                }
            }

            render { context, _, _ ->
                renderBackgroundTexture(context)
                context.drawText("Name", width / 2 - 65, height / 2 - 14, Color.WHITE)
            }
        })
    }

    private class AltEntry(val alt: AbstractAltAccount) : SimpleElementListEntry<AltEntry>() {
        override fun render(context: DrawContext, index: Int, x: Int, y: Int) {
            context.drawText(
                "Name: ${alt.username}",
                x + 3,
                y + 6,
                Color.WHITE
            )
            context.drawText(
                "Type: ${alt.type}",
                x + 3,
                y + 26 - mc.textRenderer.fontHeight,
                Color.WHITE
            )
        }
    }
}