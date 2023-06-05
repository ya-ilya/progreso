package org.progreso.client.mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.progreso.api.managers.AltManager;
import org.progreso.client.gui.minecraft.ProgresoAltsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
            ButtonWidget.builder(Text.of("Alts"), b -> showAltsScreen())
                .dimensions(5, 5, 70, 20)
                .build()
        );
    }

    private void showAltsScreen() {
        if (client == null) return;
        client.setScreen(new ProgresoAltsScreen(AltManager.INSTANCE.getAlts()));
    }
}
