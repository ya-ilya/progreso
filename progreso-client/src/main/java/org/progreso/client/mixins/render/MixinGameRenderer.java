package org.progreso.client.mixins.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.profiler.Profilers;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.progreso.client.Client;
import org.progreso.client.events.render.Render3DEvent;
import org.progreso.client.modules.render.FullBright;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(
        method = "getNightVisionStrength(Lnet/minecraft/entity/LivingEntity;F)F",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void getNightVisionStrengthHook(
        LivingEntity entity,
        float tickDelta,
        CallbackInfoReturnable<Float> callbackInfoReturnable
    ) {
        if (FullBright.INSTANCE.getEnabled()) {
            callbackInfoReturnable.setReturnValue(1F);
        }
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE_STRING",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            args = {"ldc=hand"}
        )
    )
    public void renderWorldHook(
        RenderTickCounter tickCounter,
        CallbackInfo callbackInfo,
        @Local(ordinal = 2) Matrix4f matrix4f2,
        @Local(ordinal = 1) float tickDelta
    ) {
        Profilers.get().push("progreso_3d_render");
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiplyPositionMatrix(matrix4f2);
        Client.EVENT_BUS.post(new Render3DEvent(matrixStack, tickDelta));
        Profilers.get().pop();
    }
}
