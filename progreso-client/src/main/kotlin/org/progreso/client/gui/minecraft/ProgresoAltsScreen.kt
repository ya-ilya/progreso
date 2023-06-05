package org.progreso.client.gui.minecraft

import com.google.gson.JsonElement
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.Session
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import org.progreso.api.Api
import org.progreso.api.alt.AbstractAltAccount
import org.progreso.api.alt.accounts.CrackedAccount
import org.progreso.api.managers.AltManager
import org.progreso.client.Client.Companion.mc
import org.progreso.client.gui.builders.ButtonBuilder.Companion.button
import org.progreso.client.gui.builders.ElementListBuilder.Companion.elementList
import org.progreso.client.gui.builders.ScreenBuilder.Companion.screen
import org.progreso.client.gui.builders.TextFieldBuilder.Companion.textField
import org.progreso.client.gui.minecraft.common.SimpleElementListEntry
import org.progreso.client.gui.minecraft.common.TitledScreen
import org.progreso.client.util.render.RenderContext.Companion.matrices
import java.awt.Color
import java.net.URL
import java.util.*
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
                val text = Text.of("Current name: ${mc.session.username}")

                client!!.textRenderer.draw(
                    context.matrices,
                    text,
                    (x + list.width / 2 - client!!.textRenderer.getWidth(text) / 2).toFloat(),
                    27.coerceAtMost(y).toFloat(),
                    Color.WHITE.rgb
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
                login()
            }
        }

        button("Done") { button ->
            button.dimensions(width / 2 + 4, height - 24, 96, 20)
            button.onPress {
                close()
            }
        }
    }

    private fun login() {
        when (selectedAlt) {
            is CrackedAccount -> {
                client!!.session = Session(
                    selectedAlt!!.name,
                    getUUID(selectedAlt!!.name),
                    "-",
                    Optional.empty(),
                    Optional.empty(),
                    Session.AccountType.LEGACY
                )
                close()
            }

            else -> {}
        }
    }

    private fun getUUID(username: String): String {
        try {
            val connection = URL("https://api.mojang.com/users/profiles/minecraft/$username").openConnection()
            val jsonElement = Api.GSON.fromJson(connection.getInputStream().reader(), JsonElement::class.java)

            if (jsonElement.isJsonObject) {
                return jsonElement.asJsonObject.get("id").asString
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
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
                            AltManager.addAlt(CrackedAccount(name.text))
                            close()
                        }
                    }
                }
            }

            render { context, _, _ ->
                renderBackgroundTexture(context.matrices)
                textRenderer.draw(context.matrices, "Name", width / 2 - 65f, height / 2 - 14f, Color.WHITE.rgb)
            }
        })
    }

    private class AltEntry(val alt: AbstractAltAccount) : SimpleElementListEntry<AltEntry>() {
        override fun render(matrices: MatrixStack, index: Int, x: Int, y: Int) {
            mc.textRenderer.draw(matrices, "Name: ${alt.name}", x.toFloat() + 3f, y.toFloat() + 6, Color.WHITE.rgb)
            mc.textRenderer.draw(
                matrices,
                "Type: ${alt.type}",
                x.toFloat() + 3f,
                y.toFloat() + 26 - mc.textRenderer.fontHeight,
                Color.WHITE.rgb
            )
        }
    }
}