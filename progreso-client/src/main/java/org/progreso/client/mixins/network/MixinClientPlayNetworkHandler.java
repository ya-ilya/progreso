package org.progreso.client.mixins.network;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler extends ClientCommonNetworkHandler {
    protected MixinClientPlayNetworkHandler(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(
        method = "sendChatMessage",
        at = @At("HEAD"),
        cancellable = true
    )
    public void sendChatMessageHook(String message, CallbackInfo callbackInfo) {
        if (message.startsWith(CommandManager.PREFIX)) {
            CommandManager.INSTANCE.dispatch(message);
            client.inGameHud.getChatHud().addToMessageHistory(message);
            callbackInfo.cancel();
        }
    }
}
