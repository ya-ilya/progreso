package org.progreso.client.mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.progreso.api.i18n.I18n;
import org.progreso.api.managers.AltManager;
import org.progreso.api.managers.PluginManager;
import org.progreso.client.gui.minecraft.ProgresoAltsScreen;
import org.progreso.client.gui.minecraft.ProgresoPluginsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unchecked")
@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(
        method = "init",
        at = @At("TAIL")
    )
    public void initHook(CallbackInfo callbackInfo) {
        addDrawableChild(
            ButtonWidget.builder(Text.of(I18n.i18n("gui.alts.title")), b -> showAltsScreen())
                .dimensions(5, 5, 70, 20)
                .build()
        );

        addDrawableChild(
            ButtonWidget.builder(Text.of(I18n.i18n("gui.plugins.title")), b -> showPluginsScreen())
                .dimensions(5, 28, 70, 20)
                .build()
        );
    }

    private void showAltsScreen() {
        if (client == null) return;
        client.setScreen(new ProgresoAltsScreen(AltManager.INSTANCE.getAlts()));
    }

    private void showPluginsScreen() {
        if (client == null) return;
        client.setScreen(new ProgresoPluginsScreen(PluginManager.INSTANCE.getPlugins()));
    }
}
