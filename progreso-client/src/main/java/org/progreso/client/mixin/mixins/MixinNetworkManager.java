package org.progreso.client.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.progreso.client.Client;
import org.progreso.client.events.network.PacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
    @Inject(
        method = "sendPacket(Lnet/minecraft/network/Packet;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void sendPacketHook(Packet<?> packet, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new PacketEvent.Send<>(packet))) {
            callbackInfo.cancel();
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void receivePacketHook(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new PacketEvent.Receive<>(packet))) {
            callbackInfo.cancel();
        }
    }
}
