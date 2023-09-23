package org.progreso.client.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.progreso.client.Client;
import org.progreso.client.events.network.PacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class MixinClientConnection {
    @Inject(
        method = "send(Lnet/minecraft/network/packet/Packet;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void sendHook(Packet<?> packet, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new PacketEvent.Send<>(packet))) {
            callbackInfo.cancel();
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void channelReadHook(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        if (Client.EVENT_BUS.post(new PacketEvent.Receive<>(packet))) {
            callbackInfo.cancel();
        }
    }
}
