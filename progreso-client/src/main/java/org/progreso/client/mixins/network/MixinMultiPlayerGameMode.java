package org.progreso.client.mixins.network;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.progreso.client.Client;
import org.progreso.client.events.block.DamageBlockEvent;
import org.progreso.client.events.player.AttackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MixinMultiPlayerGameMode {
    @Inject(
        method = "startDestroyBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    public void startDestroyBlockHook(
        BlockPos pos,
        Direction direction,
        CallbackInfoReturnable<Boolean> callbackInfoReturnable
    ) {
        if (Client.EVENT_BUS.post(new DamageBlockEvent(pos, direction))) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(
        method = "attack",
        at = @At("HEAD"),
        cancellable = true
    )
    public void attackHook(Player player, Entity target, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new AttackEntityEvent(target))) {
            callbackInfo.cancel();
        }
    }
}