package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.util.ICustomOverlayTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin implements ICustomOverlayTexture {
    @Shadow @Final private NativeImageBackedTexture texture;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        updateCustomOverlay();
    }

    public void updateCustomOverlay() {
        NativeImage nativeImage = this.texture.getImage();
        if (nativeImage == null) return;

        int hitColor = CaeserConfig.INSTANCE.hitColors ? CaeserConfig.INSTANCE.hitColor : 0xB2FF0000;
        int alpha = (hitColor >> 24) & 0xFF;
        int r = (hitColor >> 16) & 0xFF;
        int g = (hitColor >> 8) & 0xFF;
        int b = hitColor & 0xFF;
        int packed = (alpha << 24) | (b << 16) | (g << 8) | r; // NativeImage expects ABGR

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 16; j++) {
                nativeImage.setColor(j, i, packed);
            }
        }

        this.texture.upload();
    }
}
