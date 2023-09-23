package org.progreso.client.mixins.render;

import net.minecraft.client.render.LightmapTextureManager;
import org.progreso.client.modules.render.FullBright;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightmapTextureManager.class)
public abstract class MixinLightmapTextureManager {
    @ModifyArgs(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"
        )
    )
    public void updateModifyArgs(Args args) {
        if (FullBright.INSTANCE.getEnabled()) {
            args.set(2, 0xFFFFFFFF);
        }
    }
}
