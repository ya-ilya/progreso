package org.progreso.client.mixins

import net.minecraft.client.gui.GuiChat
import org.progreso.client.mixin.mixins.accessors.AccessorGuiChat

var GuiChat.historyBuffer
    get() = (this as AccessorGuiChat).historyBuffer
    set(value) {
        (this as AccessorGuiChat).historyBuffer = value
    }

var GuiChat.sentHistoryCursor
    get() = (this as AccessorGuiChat).sentHistoryCursor
    set(value) {
        (this as AccessorGuiChat).sentHistoryCursor = value
    }