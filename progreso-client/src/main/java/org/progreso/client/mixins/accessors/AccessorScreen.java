package org.progreso.client.mixins.accessors;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Screen.class)
public interface AccessorScreen {
    @Invoker("addDrawableChild")
    <T extends Element & Drawable> T addDrawableChildInvoker(T drawableElement);
}
