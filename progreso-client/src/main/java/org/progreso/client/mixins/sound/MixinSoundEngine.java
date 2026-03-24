package org.progreso.client.mixins.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.progreso.client.Client;
import org.progreso.client.events.misc.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public abstract class MixinSoundEngine {
    @Inject(
        method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;",
        at = @At("HEAD"),
        cancellable = true
    )
    public void playHook(SoundInstance sound, CallbackInfoReturnable<SoundEngine.PlayResult> callbackInfoReturnable) {
        if (Client.EVENT_BUS.post(new SoundEvent(sound))) {
            callbackInfoReturnable.setReturnValue(SoundEngine.PlayResult.NOT_STARTED);
        }
    }
}
