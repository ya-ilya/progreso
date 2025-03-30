package org.progreso.client.mixins.network;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.progreso.client.Client;
import org.progreso.client.events.misc.TickEvent;
import org.progreso.client.modules.render.FullBright;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class MixinClientPlayerEntity extends AbstractClientPlayerEntity {
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

    @Override
    public boolean hasStatusEffect(RegistryEntry<StatusEffect> effect) {
        if (effect == StatusEffects.NIGHT_VISION && FullBright.INSTANCE.getEnabled()) {
            return true;
        }

        return super.hasStatusEffect(effect);
    }
}
