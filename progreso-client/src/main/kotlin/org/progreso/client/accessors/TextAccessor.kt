package org.progreso.client.accessors

import com.mojang.brigadier.Message
import net.minecraft.text.Text
import org.progreso.api.accessor.TextAccessor

object TextAccessor : TextAccessor {
    override fun i18n(key: String, vararg args: Any): String {
        return Text.translatable(key, *args).string
    }

    override fun i18nMessage(key: String, vararg args: Any): Message {
        return Text.translatable(key, *args)
    }
}