package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @ModifyArgs(
        method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V")
    )
    private void modifyFogData(Args args, Camera camera, int i, RenderTickCounter tickCounter, float f, ClientWorld clientWorld) {
        if (!CaeserConfig.INSTANCE.noFog) return;

        CameraSubmersionType type = camera.getSubmersionType();
        boolean disable = false;

        if (type == CameraSubmersionType.LAVA && CaeserConfig.INSTANCE.noFogLava) disable = true;
        else if (type == CameraSubmersionType.WATER && CaeserConfig.INSTANCE.noFogWater) disable = true;
        else if (type == CameraSubmersionType.POWDER_SNOW && CaeserConfig.INSTANCE.noFogPowderSnow) disable = true;
        else if (type == CameraSubmersionType.NONE || type == CameraSubmersionType.ATMOSPHERIC) {
            // Disable atmospheric/terrain/dimension fog
            if (CaeserConfig.INSTANCE.noFogTerrain || CaeserConfig.INSTANCE.noFogDimension) {
                disable = true;
            }
        }

        if (disable) {
            args.set(3, Float.MAX_VALUE); // environmentalStart
            args.set(4, Float.MAX_VALUE); // environmentalEnd
            args.set(5, Float.MAX_VALUE); // renderDistanceStart
            args.set(6, Float.MAX_VALUE); // renderDistanceEnd
            args.set(7, Float.MAX_VALUE); // skyEnd
            args.set(8, Float.MAX_VALUE); // cloudEnd
        }
    }
}
