package org.progreso.client.mixins.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profilers;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.progreso.client.Client;
import org.progreso.client.events.render.Render3DEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(
        method = "renderWorld",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
            opcode = Opcodes.GETFIELD,
            ordinal = 0
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
