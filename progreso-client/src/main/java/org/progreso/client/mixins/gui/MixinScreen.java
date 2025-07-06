package org.progreso.client.mixins.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Inject(
        method = "handleBasicClickEvent",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V",
            remap = false
        )
    )
    private static void handleBasicClickEventHook(ClickEvent event, MinecraftClient client, Screen screenAfterRun, CallbackInfo callbackInfo) {
        if (event instanceof ClickEvent.RunCommand(String command)) {
            if (command.startsWith(CommandManager.PREFIX)) {
                CommandManager.INSTANCE.dispatch(command);
            }
        }
    }
}
