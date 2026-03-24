package org.progreso.client.mixins.entity;

import net.minecraft.world.entity.player.Player;
import org.progreso.client.Client;
import org.progreso.client.events.player.ClipAtLedgeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class MixinPlayer {
    @Inject(
        method = "isStayingOnGroundSurface",
        at = @At("HEAD"),
        cancellable = true
    )
    public void isStayingOnGroundSurfaceHook(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ClipAtLedgeEvent event = new ClipAtLedgeEvent();
        Client.EVENT_BUS.post(event);

        if (event.getClip() != null) {
            callbackInfoReturnable.setReturnValue(event.getClip());
        }
    }
}