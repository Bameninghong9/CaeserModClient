package com.caeser.mod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    public void onGetPlayerName(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Currently we only know if the LOCAL player is using the client,
        // because there is no backend server to tell us about other players.
        if (client.player != null && entry.getProfile().id().equals(client.player.getUuid())) {
            Text original = cir.getReturnValue();
            MutableText newText = Text.literal("§fⒸ ").append(original);
            cir.setReturnValue(newText);
        }
    }
}
