package org.progreso.client.mixins;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.progreso.client.Client;
import org.progreso.client.events.render.RenderOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class MixinInGameHud {
    @Inject(
        method = "render",
        at = @At("TAIL")
    )
    public void renderHook(MatrixStack matrices, float tickDelta, CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new RenderOverlayEvent(matrices));
    }
}
