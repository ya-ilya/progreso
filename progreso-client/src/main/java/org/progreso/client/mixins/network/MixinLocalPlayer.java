package org.progreso.client.mixins.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;
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

import java.util.Optional;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {
    @Shadow
    @Final
    protected Minecraft minecraft;

    @Unique
    private Screen cachedScreen;

    public MixinLocalPlayer(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Inject(
        method = "tick",
        at = @At("HEAD")
    )
    public void tickHook(CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new TickEvent());
    }

    @Inject(
        method = "handlePortalTransitionEffect",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/player/LocalPlayer;portalEffectIntensity:F",
            opcode = Opcodes.GETFIELD,
            ordinal = 1
        )
    )
    private void afterPortalTransition(boolean active, CallbackInfo callbackInfo) {
        if (cachedScreen != null) {
            minecraft.screen = cachedScreen;
            cachedScreen = null;
        }
    }

    @Override
    public boolean hasEffect(@NotNull Holder<MobEffect> effect) {
        Optional<ResourceKey<MobEffect>> nightVisionKey = MobEffects.NIGHT_VISION.unwrapKey();

        if (nightVisionKey.isEmpty()) {
            return super.hasEffect(effect);
        }

        if (effect.is(nightVisionKey.get()) && FullBright.INSTANCE.getEnabled()) {
            return true;
        }

        return super.hasEffect(effect);
    }

    @Override
    public double blockInteractionRange() {
        if (Reach.INSTANCE.getEnabled()) {
            return Reach.INSTANCE.getRange();
        }

        return super.blockInteractionRange();
    }

    @Override
    public double entityInteractionRange() {
        if (Reach.INSTANCE.getEnabled()) {
            return Reach.INSTANCE.getRange();
        }

        return super.entityInteractionRange();
    }
}