package org.progreso.client.mixins.client;

import net.minecraft.client.Mouse;
import net.minecraft.client.input.MouseInput;
import org.progreso.client.Client;
import org.progreso.client.events.input.MouseEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MixinMouse {
    @Inject(
        method = "onMouseButton",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onMouseButtonHook(long window, MouseInput input, int action, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new MouseEvent(input.button(), action))) {
            callbackInfo.cancel();
        }
    }
}
