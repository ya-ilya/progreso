package org.progreso.client.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(
        method = "sendChatMessage",
        at = @At("HEAD"),
        cancellable = true
    )
    public void onSendChatMessage(String message, CallbackInfo callbackInfo) {
        if (message.startsWith(CommandManager.PREFIX)) {
            CommandManager.INSTANCE.dispatch(message);
            client.inGameHud.getChatHud().addToMessageHistory(message);
            callbackInfo.cancel();
        }
    }
}
