package org.progreso.client.mixins.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.jspecify.annotations.Nullable;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Inject(
        method = "clickCommandAction",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void onClickCommandAction(
        LocalPlayer player,
        String command,
        @Nullable Screen screen,
        CallbackInfo callbackInfo
    ) {
        if (command.startsWith(CommandManager.PREFIX)) {
            CommandManager.INSTANCE.dispatch(command);

            callbackInfo.cancel();
        }
    }
}