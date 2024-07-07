package org.progreso.client.mixins.network;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.progreso.client.Client;
import org.progreso.client.events.block.DamageBlockEvent;
import org.progreso.client.events.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
    @Inject(
        method = "attackBlock",
        at = @At("HEAD")
    )
    public void attackBlockHook(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (Client.EVENT_BUS.post(new DamageBlockEvent(pos, direction))) {
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(
        method = "attackEntity",
        at = @At("HEAD"),
        cancellable = true
    )
    public void attackEntityHook(PlayerEntity player, Entity target, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new AttackEntityEvent(target))) {
            callbackInfo.cancel();
        }
    }
}
