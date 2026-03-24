package org.progreso.client.mixins.render;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.progreso.client.modules.render.FullBright;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(
        method = "getNightVisionScale",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getNightVisionScaleHook(
        LivingEntity camera,
        float delta,
        CallbackInfoReturnable<Float> callbackInfoReturnable
    ) {
        if (FullBright.INSTANCE.getEnabled()) {
            callbackInfoReturnable.setReturnValue(1.0F);
        }
    }
}