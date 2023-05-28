package org.progreso.client.mixins;

import net.minecraft.client.Keyboard;
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
    public void onKeyHook(long window, int key, int scancode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (action >= 1 && Client.EVENT_BUS.post(new KeyEvent(key, scancode, action))) {
            callbackInfo.cancel();
        }
    }

    @Inject(
        method = "onChar",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onCharHook(long window, int codePoint, int modifiers, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new CharEvent(codePoint))) {
            callbackInfo.cancel();
        }
    }
}
