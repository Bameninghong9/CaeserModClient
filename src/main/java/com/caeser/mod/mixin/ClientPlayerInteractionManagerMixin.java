package com.caeser.mod.mixin;

import com.caeser.mod.gui.hud.ComboModule;
import com.caeser.mod.gui.hud.ReachModule;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (player != null && target != null) {
            double distance = player.distanceTo(target);
            ReachModule.updateReach(distance);
            ComboModule.addCombo();
        }
    }
}
