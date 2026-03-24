package org.progreso.client.mixins.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.progreso.client.Client;
import org.progreso.client.events.render.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(
        method = "setScreen",
        at = @At("HEAD"),
        cancellable = true
    )
    public void setScreenHook(Screen screen, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new ScreenEvent.Set(screen))) {
            callbackInfo.cancel();
        }
    }
}
