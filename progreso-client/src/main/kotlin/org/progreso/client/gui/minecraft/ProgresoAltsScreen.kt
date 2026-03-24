package org.progreso.client.gui.minecraft

import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ContainerObjectSelectionList
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.util.Util
import org.progreso.api.alt.AltAccount
import org.progreso.api.managers.AltManager
import org.progreso.client.Client.Companion.mc
import org.progreso.client.accessors.TextAccessor.i18n
import org.progreso.client.gui.builders.ButtonBuilder.Companion.button
import org.progreso.client.gui.builders.ElementListBuilder.Companion.elementList
import org.progreso.client.gui.builders.ScreenBuilder.Companion.screen
import org.progreso.client.gui.builders.TextFieldBuilder.Companion.textField
import org.progreso.client.gui.drawBorder
import org.progreso.client.gui.drawText
import org.progreso.client.gui.invoke
import org.progreso.client.gui.minecraft.common.SimpleElementListEntry
import org.progreso.client.gui.minecraft.common.TitledScreen
import org.progreso.client.util.misc.SessionUtil
import java.awt.Color
import kotlin.concurrent.thread

class ProgresoAltsScreen(private val alts: Set<AltAccount>) : TitledScreen(i18n = "gui.alts.title") {
    private var selectedAlt: AltAccount? = null

    private var removeButtonWidget: Button? = null
    private var loginButtonWidget: Button? = null

    override fun init() {
        elementList<AltEntry> { list ->
            list.listDimension(
                x = width / 2 - 100,
                y = 24,
                width = 200,
                height = height - 75 - 24,
                itemHeight = 36
            )

            for (alt in alts) {
                list.addEntry {
                    AltEntry(this, alt)
                }
            }

            list.select { entry ->
                selectedAlt = entry?.alt
                removeButtonWidget?.active = selectedAlt != null
                loginButtonWidget?.active = selectedAlt != null
            }
        }

        button(i18n = "gui.alts.button.add_offline_alt") { button ->
            button.dimensions(width / 2 - 100, height - 72, 96, 20)
            button.onPress { showAddOfflineAltScreen() }
        }

        button(i18n = "gui.alts.button.add_microsoft_alt") { button ->
            button.dimensions(width / 2 + 4, height - 72, 96, 20)
            button.onPress { showAddMicrosoftAltScreen() }
        }

        removeButtonWidget = button(i18n = "gui.alts.button.remove_alt") { button ->
            button.active = false
            button.dimensions(width / 2 - 100, height - 48, 96, 20)
            button.onPress {
                AltManager.removeAlt(selectedAlt!!)
                minecraft.setScreen(ProgresoAltsScreen(AltManager.alts))
            }
        }

        loginButtonWidget = button(i18n = "gui.alts.button.login") { button ->
            button.active = false
            button.dimensions(width / 2 + 4, height - 48, 96, 20)
            button.onPress {
                if (SessionUtil.login(selectedAlt!!) == SessionUtil.LoginResult.Successful) {
                    onClose()
                }
            }
        }

        button(i18n = "gui.alts.button.done") { button ->
            button.dimensions(width / 2 - 100, height - 24, 200, 20)
            button.onPress { onClose() }
        }
    }

    private fun showAddOfflineAltScreen() {
        minecraft.setScreen(screen(i18n = "gui.alts.title.add_offline_alt") {
            init {
                val name = textField { textField ->
                    textField.dimensions(width / 2 - 66, height / 2 - 20, 132, 20)
                }

                button(i18n = "gui.alts.button.add_offline_alt") { button ->
                    button.dimensions(width / 2 - 66, height / 2 + 8, 132, 20)
                    button.onPress {
                        if (name.value.length >= 3) {
                            if (!AltManager.alts.any { it.username == name.value }) {
                                AltManager.addAlt(SessionUtil.createOfflineAltAccount(name.value))
                                onClose()
                            } else {
                                showErrorCreateAltScreen(i18n("gui.alts.label.error_alt_exists"))
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showAddMicrosoftAltScreen() {
        minecraft.setScreen(screen(i18n = "gui.alts.title.add_microsoft_alt") {
            val result = object {
                var set = false
                var url: String? = null
                var pair: Pair<SessionUtil.LoginResult, AltAccount.Microsoft?>? = null
            }

            val thread = thread {
                result.pair = SessionUtil.createMicrosoftAltAccount {
                    Util.getPlatform().openUri(it)
                    result.url = it
                }
                result.set = true
            }

            init {
                button(i18n = "gui.alts.button.open_link") { button ->
                    button.dimensions(width / 2 - 136, height / 2 + 8, 132, 20)
                    button.onPress { if (result.url != null) Util.getPlatform().openUri(result.url!!) }
                }

                button(i18n = "gui.alts.button.done") { button ->
                    button.dimensions(width / 2 + 4, height / 2 + 8, 132, 20)
                    button.onPress { onClose() }
                }
            }

            close {
                thread.interrupt()
            }

            render { context, mouseX, mouseY, delta ->
                if (result.set) {
                    val (status, account) = result.pair ?: return@render onClose()

                    if (status is SessionUtil.LoginResult.Error) {
                        showErrorCreateAltScreen(status.message)
                    } else if (!AltManager.alts.any { it.username == account!!.username }) {
                        AltManager.addAlt(account!!)
                        onClose()
                    } else {
                        showErrorCreateAltScreen(i18n("gui.alts.label.error_alt_exists"))
                    }
                }

                val text = i18n("gui.alts.label.add_microsoft_alt_link")

                extractBackground(context, mouseX, mouseY, delta)
                context.drawText(
                    font,
                    text,
                    width / 2 - font.width(text) / 2,
                    height / 2 - 14,
                    Color.WHITE
                )
            }
        })
    }

    private fun showErrorCreateAltScreen(error: String) {
        val errorI18n = i18n("gui.alts.label.error_create_alt", error)

        minecraft.setScreen(screen(i18n = "gui.alts.title.error_create_alt") {
            init {
                button(i18n = "gui.alts.button.done") { button ->
                    button.dimensions(width / 2 - 66, height / 2 + 8, 132, 20)
                    button.onPress { onClose() }
                }
            }

            render { context, mouseX, mouseY, delta ->
                extractBackground(context, mouseX, mouseY, delta)
                context.drawText(
                    font,
                    errorI18n,
                    width / 2 - font.width(errorI18n) / 2,
                    height / 2 - 14, Color.WHITE
                )
            }
        })
    }

    private class AltEntry(
        val parent: ContainerObjectSelectionList<AltEntry>,
        val alt: AltAccount
    ) : SimpleElementListEntry<AltEntry>() {
        override fun render(context: GuiGraphicsExtractor, x: Int, y: Int, width: Int, height: Int) = context {
            drawText(
                mc.font,
                i18n("gui.alts.label.alt_name", alt.username),
                x + 3,
                y + 6,
                Color.WHITE
            )
            drawText(
                mc.font,
                i18n(
                    "gui.alts.label.alt_type", when (alt) {
                        is AltAccount.Offline -> i18n("gui.alts.label.offline_alt_type")
                        is AltAccount.Microsoft -> i18n("gui.alts.label.microsoft_alt_type")
                    }
                ),
                x + 3,
                y + 26 - mc.font.lineHeight,
                Color.WHITE
            )

            if (parent.selected == this@AltEntry) {
                drawBorder(x, y, width, height, Color.WHITE)
            }
        }

        override fun mouseClicked(event: MouseButtonEvent, doubleClick: Boolean): Boolean {
            parent.selected = this

            return super.mouseClicked(event, doubleClick)
        }
    }
}