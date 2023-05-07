package org.progreso.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.progreso.client.Client;
import org.progreso.client.events.input.KeyboardEvent;
import org.progreso.client.events.input.MouseEvent;
import org.progreso.client.events.misc.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Inject(
        method = "init",
        at = @At("RETURN")
    )
    public void initHook(CallbackInfo ci) {
        Client.initialize();
    }

    @Inject(
        method = "runTickKeyboard",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z",
            remap = false
        )
    )
    public void runTickKeyboardHook(CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new KeyboardEvent(
            Keyboard.getEventKeyState(),
            Keyboard.getEventKey(),
            Keyboard.getEventCharacter()
        ));
    }

    @Inject(
        method = "runTickMouse",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/input/Mouse;getEventButton()I",
            remap = false
        )
    )
    public void runTickMouseHook(CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new MouseEvent(
            Mouse.getEventButtonState(),
            Mouse.getEventButton()
        ));
    }


    @Inject(
        method = "runTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V",
            ordinal = 0,
            shift = At.Shift.BEFORE
        )
    )
    public void runTickHook(CallbackInfo callbackInfo) {
        Client.EVENT_BUS.post(new TickEvent());
    }
}
