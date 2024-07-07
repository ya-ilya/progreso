package org.progreso.client.mixins.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
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
    public void renderHook(
        DrawContext context,
        RenderTickCounter tickCounter,
        CallbackInfo callbackInfo
    ) {
        Client.getMc().getClient().getProfiler().push("progreso_2d_render");
        Client.EVENT_BUS.post(new Render2DEvent(context));
        Client.getMc().getClient().getProfiler().pop();
    }
}
