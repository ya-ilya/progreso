package org.progreso.client.mixin.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.progreso.client.Client;
import org.progreso.client.events.block.DamageBlockEvent;
import org.progreso.client.events.block.DestroyBlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
    @Inject(
        method = "onPlayerDamageBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onPlayerDamageBlockHook(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (Client.EVENT_BUS.post(new DamageBlockEvent(posBlock, directionFacing))) {
            callbackInfo.setReturnValue(true);
        }
    }

    @Inject(
        method = "onPlayerDestroyBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;onPlayerDestroy(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V",
            remap = false
        )
    )
    public void onPlayerDestroyBlockHook(BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfo) {
        Client.EVENT_BUS.post(new DestroyBlockEvent(pos));
    }
}
