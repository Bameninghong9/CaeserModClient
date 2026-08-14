package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionStrength(LivingEntity entity, float f, CallbackInfoReturnable<Float> cir) {
        if (CaeserConfig.INSTANCE.fullbright) {
            cir.setReturnValue(1.0f);
        }
    }
}
