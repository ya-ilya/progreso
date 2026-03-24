package org.progreso.client.mixins.network;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.progreso.client.Client;
import org.progreso.client.events.network.PacketEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class MixinConnection {
    @Inject(
        method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void sendHook(
        Packet<?> packet,
        ChannelFutureListener listener,
        boolean flush,
        CallbackInfo callbackInfo
    ) {
        if (Client.EVENT_BUS.post(new PacketEvent.Send<>(packet))) {
            callbackInfo.cancel();
        }
    }

    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    public void channelReadHook(
        ChannelHandlerContext ctx,
        Packet<?> packet,
        CallbackInfo callbackInfo
    ) {
        if (Client.EVENT_BUS.post(new PacketEvent.Receive<>(packet))) {
            callbackInfo.cancel();
        }
    }
}