package org.progreso.client.mixins.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.objectweb.asm.Opcodes;
import org.progreso.client.Client;
import org.progreso.client.events.misc.TickEvent;
import org.progreso.client.modules.misc.Reach;
import org.progreso.client.modules.render.FullBright;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
    @Shadow @Final protected MinecraftClient client;

    @Unique
    private Screen cachedScreen;

    public MixinClientPlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void tickHook(CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new TickEvent());
    }

    @Inject(
        method = "tickNausea(Z)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;nauseaIntensity:F",
            opcode = Opcodes.GETFIELD,
            ordinal = 1
        )
    )
    private void afterTickNausea(boolean fromPortalEffect, CallbackInfo ci)
    {
        if (cachedScreen != null) {
            client.currentScreen = cachedScreen;
            cachedScreen = null;
        }
    }

    @Override
    public boolean hasStatusEffect(RegistryEntry<StatusEffect> effect) {
        if (effect == StatusEffects.NIGHT_VISION && FullBright.INSTANCE.getEnabled()) {
            return true;
        }

        return super.hasStatusEffect(effect);
    }

    @Override
    public double getBlockInteractionRange() {
        if (Reach.INSTANCE.getEnabled()) {
            return Reach.INSTANCE.getRange();
        }

        return super.getBlockInteractionRange();
    }

    @Override
    public double getEntityInteractionRange() {
        if (Reach.INSTANCE.getEnabled()) {
            return Reach.INSTANCE.getRange();
        }

        return super.getEntityInteractionRange();
    }
}
