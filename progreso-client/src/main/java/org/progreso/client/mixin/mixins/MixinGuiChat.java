package org.progreso.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import org.progreso.api.managers.CommandManager;
import org.progreso.client.gui.mc.ProgresoGuiChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public class MixinGuiChat {
    private static final String PREFIX_STRING = String.valueOf(CommandManager.PREFIX);
    @Shadow
    protected GuiTextField inputField;
    @Shadow
    private String historyBuffer;
    @Shadow
    private int sentHistoryCursor;

    @Inject(
        method = "keyTyped",
        at = @At("HEAD")
    )
    public void keyTypedHook(char typedChar, int keyCode, CallbackInfo callbackInfo) {
        if (getThis() instanceof ProgresoGuiChat) return;
        if (inputField.getText().isEmpty() && typedChar == CommandManager.PREFIX) {
            displayProgresoGuiChat(PREFIX_STRING);
        }
    }

    private GuiChat getThis() {
        return (GuiChat) (Object) this;
    }

    @SuppressWarnings("SameParameterValue")
    private void displayProgresoGuiChat(String defaultText) {
        Minecraft.getMinecraft().displayGuiScreen(new ProgresoGuiChat(
            defaultText,
            historyBuffer,
            sentHistoryCursor
        ));
    }
}
