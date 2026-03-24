package org.progreso.client.mixins.client;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.CharacterEvent;
import org.progreso.client.Client;
import org.progreso.client.events.input.CharEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class MixinKeyboardHandler {
    @Inject(
        method = "keyPress",
        at = @At("HEAD"),
        cancellable = true
    )
    public void keyPressHook(
        long handle,
        int action,
        net.minecraft.client.input.KeyEvent event,
        CallbackInfo callbackInfo
    ) {
        if (action >= 1 && Client.EVENT_BUS.post(new org.progreso.client.events.input.KeyEvent(
            event.key(),
            event.scancode(),
            action
        ))) {
            callbackInfo.cancel();
        }
    }

    @Inject(
        method = "charTyped",
        at = @At("HEAD"),
        cancellable = true
    )
    public void charTypedHook(
        long handle,
        CharacterEvent event,
        CallbackInfo callbackInfo
    ) {
        if (Client.EVENT_BUS.post(new CharEvent(event.codepoint()))) {
            callbackInfo.cancel();
        }
    }
}