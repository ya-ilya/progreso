package org.progreso.client.mixins;

import net.minecraft.entity.player.PlayerEntity;
import org.progreso.client.Client;
import org.progreso.client.events.player.ClipAtLedgeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity {
    @Inject(
        method = "clipAtLedge",
        at = @At("HEAD"),
        cancellable = true
    )
    public void clipAtLedgeHook(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ClipAtLedgeEvent event = new ClipAtLedgeEvent();
        Client.EVENT_BUS.post(event);

        if (event.getClip() != null) {
            callbackInfoReturnable.setReturnValue(event.getClip());
        }
    }
}
