package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "getOverlay", at = @At("HEAD"), cancellable = true)
    private static void onGetOverlay(LivingEntityRenderState state, float whiteOverlayProgress, CallbackInfoReturnable<Integer> cir) {
        if (!CaeserConfig.INSTANCE.hitColors) return;

        // If it's the white overlay flash, don't change it (V=10)
        if (whiteOverlayProgress > 0.0f) {
            return; // let default logic handle it
        }

        // We only modify the red damage flash
        if (state.hurt || state.deathTime > 0.0f) {
            int u = (int)(Math.sin(state.ageScale * 0.1f) * 15.0f); // just some arbitrary U animation or 0? 
            // Vanilla does: (int)(Math.sin(state.ageScale * 0.1f) * 15.0f) or something similar if we look at getOverlay, wait, getU takes the white overlay progress...
            // Let's just use 0 for U, because our texture is solid color anyway!
            int uPack = 0;
            int vPack = 1;
            cir.setReturnValue(net.minecraft.client.render.OverlayTexture.packUv(uPack, vPack));
        }
    }
}
