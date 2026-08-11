package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.gui.hud.debug.DebugHudProfile;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DebugHudProfile.class)
public class DebugHudProfileMixin {

    @Inject(method = "isEntryVisible", at = @At("HEAD"), cancellable = true)
    private void forceHitboxesVisible(Identifier id, CallbackInfoReturnable<Boolean> cir) {
        if (CaeserConfig.INSTANCE.hitboxes && id.toString().equals("minecraft:entity_hitboxes")) {
            cir.setReturnValue(true);
        }
    }
}
