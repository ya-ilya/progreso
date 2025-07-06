package org.progreso.client.mixins.sound;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import org.progreso.client.Client;
import org.progreso.client.events.misc.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundSystem.class)
public abstract class MixinSoundSystem {
    @Inject(
        method = "play(Lnet/minecraft/client/sound/SoundInstance;)Lnet/minecraft/client/sound/SoundSystem$PlayResult;",
        at = @At("HEAD"),
        cancellable = true
    )
    public void playHook(SoundInstance sound, CallbackInfoReturnable<SoundSystem.PlayResult> callbackInfoReturnable) {
        if (Client.EVENT_BUS.post(new SoundEvent(sound))) {
            callbackInfoReturnable.setReturnValue(SoundSystem.PlayResult.NOT_STARTED);
        }
    }
}
