package com.caeser.mod.mixin;

import com.caeser.mod.config.AutoTextEntry;
import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void caeserOnKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        if (action == 1) { // 1 = Press
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.currentScreen == null) {
                if (CaeserConfig.INSTANCE.autoTextEnabled) {
                    for (AutoTextEntry entry : CaeserConfig.INSTANCE.autoTexts) {
                        if (entry.keyCode == key) {
                            if (entry.text.startsWith("/")) {
                                client.player.networkHandler.sendChatCommand(entry.text.substring(1));
                            } else {
                                client.player.networkHandler.sendChatMessage(entry.text);
                            }
                        }
                    }
                }
            }
        }
    }
}
