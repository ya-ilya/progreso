package org.progreso.client.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.progreso.client.Client;
import org.progreso.client.events.render.Render2DEvent;
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
    public void renderHook(DrawContext context, float tickDelta, CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new Render2DEvent(context));
    }
}
