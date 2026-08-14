package com.caeser.mod.mixin;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;)V", at = @At("HEAD"), cancellable = true)
    private void beforeRender(DrawContext context, CallbackInfo ci) {
        if (!CaeserConfig.INSTANCE.customBossbar) {
            ci.cancel();
            return;
        }

        int configX = CaeserConfig.INSTANCE.bossbarX;
        int configY = CaeserConfig.INSTANCE.bossbarY;
        float scale = CaeserConfig.INSTANCE.bossbarScale;

        int vanillaX = (context.getScaledWindowWidth() / 2) - 91;
        int vanillaY = 12;

        int targetX = configX != -1 ? configX : vanillaX;
        int targetY = configY != -1 ? configY : vanillaY;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)targetX, (float)targetY);
        context.getMatrices().scale(scale, scale);
        context.getMatrices().translate((float)-vanillaX, (float)-vanillaY);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;)V", at = @At("RETURN"))
    private void afterRender(DrawContext context, CallbackInfo ci) {
        if (!CaeserConfig.INSTANCE.customBossbar) return;
        context.getMatrices().popMatrix();
    }

    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V", at = @At("HEAD"))
    private void onRenderBossBar(DrawContext context, int x, int y, net.minecraft.entity.boss.BossBar bossBar, CallbackInfo ci) {
        com.caeser.mod.gui.hud.BossbarModule.lastBossbarRenderTime = System.currentTimeMillis();
    }
}
