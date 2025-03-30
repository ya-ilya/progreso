package org.progreso.client.mixins.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Inject(
        method = "handleTextClick",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V",
            ordinal = 1,
            remap = false
        ),
        cancellable = true
    )
    private void handleTextClickHook(Style style, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ClickEvent event = style.getClickEvent();
        if (event instanceof ClickEvent.RunCommand runCommand) {
            if (runCommand.command().startsWith(CommandManager.PREFIX)) {
                CommandManager.INSTANCE.dispatch(runCommand.command());
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }
}
