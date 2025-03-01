package org.progreso.client.mixins.render;

import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.LightmapTextureManager;
import org.progreso.client.modules.render.FullBright;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LightmapTextureManager.class)
public abstract class MixinLightmapTextureManager {
    @Shadow @Final private SimpleFramebuffer lightmapFramebuffer;

    @Inject(
        method = "update",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gl/SimpleFramebuffer;endWrite()V",
            shift = At.Shift.BEFORE
        )
    )
    private void updateHook(CallbackInfo callbackInfo) {
        if (FullBright.INSTANCE.getEnabled()) {
            this.lightmapFramebuffer.clear();
        }
    }
}
