package org.progreso.client.mixins.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener extends ClientCommonPacketListenerImpl {
    protected MixinClientPacketListener(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(
        method = "sendChat",
        at = @At("HEAD"),
        cancellable = true
    )
    public void sendChatHook(String message, CallbackInfo callbackInfo) {
        if (message.startsWith(CommandManager.PREFIX)) {
            CommandManager.INSTANCE.dispatch(message);

            this.minecraft.gui.getChat().addRecentChat(message);

            callbackInfo.cancel();
        }
    }
}