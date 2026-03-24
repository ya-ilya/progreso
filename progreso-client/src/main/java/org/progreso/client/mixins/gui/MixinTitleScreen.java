package org.progreso.client.mixins.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.progreso.api.managers.AltManager;
import org.progreso.api.managers.PluginManager;
import org.progreso.client.accessors.TextAccessor;
import org.progreso.client.gui.minecraft.ProgresoAltsScreen;
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Component title) {
        super(title);
    }

    @Inject(
        method = "init",
        at = @At("TAIL")
    )
    public void initHook(CallbackInfo callbackInfo) {
        this.addRenderableWidget(
            Button.builder(
                    Component.literal(TextAccessor.INSTANCE.i18n("gui.alts.title")),
                    _ -> showAltsScreen()
                )
                .bounds(5, 5, 70, 20)
                .build()
        );

        this.addRenderableWidget(
            Button.builder(
                    Component.literal(TextAccessor.INSTANCE.i18n("gui.plugins.title")),
                    _ -> showPluginsScreen()
                )
                .bounds(5, 28, 70, 20)
                .build()
        );
    }

    @Unique
    private void showAltsScreen() {
        this.minecraft.setScreen(new ProgresoAltsScreen(AltManager.INSTANCE.getAlts()));
    }

    @Unique
    private void showPluginsScreen() {
        this.minecraft.setScreen(new ProgresoPluginsScreen(PluginManager.INSTANCE.getPlugins()));
    }
}