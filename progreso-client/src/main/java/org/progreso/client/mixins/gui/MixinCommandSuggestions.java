package org.progreso.client.mixins.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.commands.SharedSuggestionProvider;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(CommandSuggestions.class)
public abstract class MixinCommandSuggestions {
    @Shadow
    @Final
    EditBox input;

    @Shadow
    boolean keepSuggestions;

    @Shadow
    private ParseResults<SharedSuggestionProvider> currentParse;

    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;

    @Shadow
    private CommandSuggestions.SuggestionsList suggestions;

    @Shadow
    protected abstract void showSuggestions(boolean b);

    @Inject(
        method = "updateCommandInfo",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
            remap = false
        ),
        cancellable = true
    )
    @SuppressWarnings("unchecked")
    public void onUpdateCommandInfo(CallbackInfo ci, @Local(argsOnly = true) StringReader reader) {
        String prefix = CommandManager.PREFIX;
        int length = prefix.length();

        if (reader.canRead(length) && reader.getString().startsWith(prefix, reader.getCursor())) {
            reader.setCursor(reader.getCursor() + length);

            if (this.currentParse == null) {
                this.currentParse = (ParseResults<SharedSuggestionProvider>) (Object) CommandManager.DISPATCHER.parse(reader, CommandManager.INSTANCE.getSUGGESTION_PROVIDER());
            }

            int cursor = input.getCursorPosition();
            if (cursor >= 1 && (this.suggestions == null || !this.keepSuggestions)) {
                this.pendingSuggestions = CommandManager.DISPATCHER.getCompletionSuggestions((ParseResults<Object>) (Object) this.currentParse, cursor);
                this.pendingSuggestions.thenRun(() -> {
                    if (this.pendingSuggestions.isDone()) {
                        this.showSuggestions(false);
                    }
                });
            }

            ci.cancel();
        }
    }
}