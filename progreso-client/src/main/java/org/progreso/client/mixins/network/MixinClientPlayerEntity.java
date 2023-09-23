package org.progreso.client.mixins.network;

import net.minecraft.client.network.ClientPlayerEntity;
import org.progreso.client.Client;
import org.progreso.client.events.misc.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity {
    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void tickHook(CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new TickEvent());
    }
}
