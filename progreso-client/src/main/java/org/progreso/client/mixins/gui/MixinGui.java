package org.progreso.client.mixins.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.profiling.Profiler;
import org.progreso.client.Client;
import org.progreso.client.events.render.Render2DEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Inject(
        method = "extractRenderState",
        at = @At("TAIL")
    )
    public void onExtractRenderState(
        GuiGraphicsExtractor graphics,
        DeltaTracker deltaTracker,
        CallbackInfo ci
    ) {
        Profiler.get().push("progreso_2d_extract");

        Client.EVENT_BUS.post(new Render2DEvent(graphics));

        Profiler.get().pop();
    }
}