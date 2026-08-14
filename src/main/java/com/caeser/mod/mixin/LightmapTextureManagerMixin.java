package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

    @Redirect(
        method = "update",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z")
    )
    private boolean redirectHasStatusEffect(ClientPlayerEntity player, RegistryEntry effect) {
        if (CaeserConfig.INSTANCE.fullbright && effect == StatusEffects.NIGHT_VISION) {
            return true;
        }
        return player.hasStatusEffect(effect);
    }

    @Inject(method = "getBrightness(Lnet/minecraft/world/dimension/DimensionType;I)F", at = @At("HEAD"), cancellable = true)
    private static void onGetBrightness(net.minecraft.world.dimension.DimensionType type, int lightLevel, org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Float> cir) {
        if (CaeserConfig.INSTANCE.fullbright) {
            cir.setReturnValue(1.0f);
        }
    }
}
