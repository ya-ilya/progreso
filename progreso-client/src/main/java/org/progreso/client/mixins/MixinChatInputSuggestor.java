package org.progreso.client.mixins;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import org.progreso.api.managers.CommandManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(ChatInputSuggestor.class)
public abstract class MixinChatInputSuggestor {
    @Shadow
    @Final
    TextFieldWidget textField;
    @Shadow
    boolean completingSuggestions;
    @Shadow
    private ParseResults<CommandSource> parse;
    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow
    private ChatInputSuggestor.SuggestionWindow window;

    @Shadow
    protected abstract void showCommandSuggestions();

    @Inject(
        method = "refresh",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/StringReader;canRead()Z",
            remap = false
        ),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    @SuppressWarnings("unchecked")
    public void onRefresh(CallbackInfo callbackInfo, String string, StringReader reader) {
        String prefix = CommandManager.PREFIX;
        int length = prefix.length();

        if (reader.canRead(length) && reader.getString().startsWith(prefix, reader.getCursor())) {
            reader.setCursor(reader.getCursor() + length);

            if (this.parse == null) {
                this.parse = (ParseResults<CommandSource>) (Object) CommandManager.DISPATCHER.parse(reader, CommandManager.SOURCE);
            }

            int cursor = textField.getCursor();
            if (cursor >= 1 && (this.window == null || !this.completingSuggestions)) {
                this.pendingSuggestions = CommandManager.DISPATCHER.getCompletionSuggestions((ParseResults<Object>) (Object) this.parse, cursor);
                this.pendingSuggestions.thenRun(() -> {
                    if (this.pendingSuggestions.isDone()) {
                        this.showCommandSuggestions();
                    }
                });
            }

            callbackInfo.cancel();
        }
    }
}
