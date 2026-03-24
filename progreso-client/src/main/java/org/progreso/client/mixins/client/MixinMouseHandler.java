package org.progreso.client.mixins.client;

import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.progreso.client.Client;
import org.progreso.client.events.input.MouseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MixinMouseHandler {
    @Inject(
        method = "onButton",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onButtonHook(
        long handle,
        MouseButtonInfo rawButtonInfo,
        int action,
        CallbackInfo callbackInfo
    ) {
        if (Client.EVENT_BUS.post(new MouseEvent(rawButtonInfo.button(), action))) {
            callbackInfo.cancel();
        }
    }
}