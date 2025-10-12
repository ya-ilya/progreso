package org.progreso.client.mixins.client;

import net.minecraft.client.Keyboard;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.progreso.client.Client;
import org.progreso.client.events.input.CharEvent;
import org.progreso.client.events.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class MixinKeyboard {
    @Inject(
        method = "onKey",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onKeyHook(long window, int action, KeyInput input, CallbackInfo callbackInfo) {
        if (action >= 1 && Client.EVENT_BUS.post(new KeyEvent(input.key(), input.scancode(), action))) {
            callbackInfo.cancel();
        }
    }

    @Inject(
        method = "onChar",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onCharHook(long window, CharInput input, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new CharEvent(input.codepoint()))) {
            callbackInfo.cancel();
        }
    }
}
