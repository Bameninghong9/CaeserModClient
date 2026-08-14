package com.caeser.mod.mixin;

import com.caeser.mod.gui.hud.HudManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        HudManager.INSTANCE.render(context, tickCounter.getTickProgress(true));
    }
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (!com.caeser.mod.config.CaeserConfig.INSTANCE.customCrosshair) return;
        
        net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
        int scaledWidth = context.getScaledWindowWidth();
        int scaledHeight = context.getScaledWindowHeight();
        int centerX = scaledWidth / 2;
        int centerY = scaledHeight / 2;
        
        boolean isTargeting = false;
        if (com.caeser.mod.config.CaeserConfig.INSTANCE.customCrosshairTargetColor && client.targetedEntity instanceof net.minecraft.entity.LivingEntity) {
            isTargeting = true;
        }

        if (com.caeser.mod.config.CaeserConfig.INSTANCE.customCrosshairVanilla) {
            // A simple colored plus for "Vanilla style"
            int color = isTargeting ? com.caeser.mod.config.CaeserConfig.INSTANCE.customCrosshairTargetColorHex : com.caeser.mod.config.CaeserConfig.INSTANCE.customCrosshairVanillaColor;
            context.fill(centerX - 4, centerY - 1, centerX + 5, centerY, color);
            context.fill(centerX - 1, centerY - 4, centerX, centerY + 5, color);
            ci.cancel();
            return;
        }

        // Draw 15x15 grid custom crosshair
        int[][] pixels = com.caeser.mod.config.CaeserConfig.INSTANCE.customCrosshairPixels;
        int startX = centerX - 7;
        int startY = centerY - 7;
        
        int targetColor = com.caeser.mod.config.CaeserConfig.INSTANCE.customCrosshairTargetColorHex;

        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                int color = pixels[x][y];
                if (color != 0) { // If not transparent
                    if (isTargeting) {
                        color = targetColor;
                    }
                    context.fill(startX + x, startY + y, startX + x + 1, startY + y + 1, color);
                }
            }
        }
        ci.cancel();
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("HEAD"))
    private void beforeRenderScoreboard(DrawContext context, net.minecraft.scoreboard.ScoreboardObjective objective, CallbackInfo ci) {
        com.caeser.mod.gui.hud.ScoreboardModule.lastScoreboardRenderTime = System.currentTimeMillis();
        
        // Transfer captured bounds from previous frame
        if (com.caeser.mod.gui.hud.ScoreboardModule.capturedMinX != Integer.MAX_VALUE) {
            com.caeser.mod.gui.hud.ScoreboardModule.lastMinX = com.caeser.mod.gui.hud.ScoreboardModule.capturedMinX;
            com.caeser.mod.gui.hud.ScoreboardModule.lastMinY = com.caeser.mod.gui.hud.ScoreboardModule.capturedMinY;
            com.caeser.mod.gui.hud.ScoreboardModule.lastMaxX = com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxX;
            com.caeser.mod.gui.hud.ScoreboardModule.lastMaxY = com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxY;
        }
        
        // Reset capture bounds for current frame
        com.caeser.mod.gui.hud.ScoreboardModule.capturedMinX = Integer.MAX_VALUE;
        com.caeser.mod.gui.hud.ScoreboardModule.capturedMinY = Integer.MAX_VALUE;
        com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxX = Integer.MIN_VALUE;
        com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxY = Integer.MIN_VALUE;
        com.caeser.mod.gui.hud.ScoreboardModule.capturing = true;
        
        if (!com.caeser.mod.config.CaeserConfig.INSTANCE.customScoreboard) {
            return;
        }

        int configX = com.caeser.mod.config.CaeserConfig.INSTANCE.scoreboardX;
        int configY = com.caeser.mod.config.CaeserConfig.INSTANCE.scoreboardY;
        float scale = com.caeser.mod.config.CaeserConfig.INSTANCE.scoreboardScale;

        // If we don't have last bounds yet, fallback to a rough estimate
        int vanillaX = com.caeser.mod.gui.hud.ScoreboardModule.lastMinX != -1 ? com.caeser.mod.gui.hud.ScoreboardModule.lastMinX : context.getScaledWindowWidth() - 100 - 2;
        int vanillaY = com.caeser.mod.gui.hud.ScoreboardModule.lastMinY != -1 ? com.caeser.mod.gui.hud.ScoreboardModule.lastMinY : (context.getScaledWindowHeight() / 2) - 60;

        int targetX = configX != -1 ? configX : vanillaX;
        int targetY = configY != -1 ? configY : vanillaY;

        context.getMatrices().pushMatrix();
        
        // Only draw custom background and translate if we have valid bounds
        if (com.caeser.mod.gui.hud.ScoreboardModule.lastMinX != -1) {
            context.getMatrices().translate((float)targetX, (float)targetY);
            context.getMatrices().scale(scale, scale);
            
            com.caeser.mod.gui.hud.ScoreboardModule sm = com.caeser.mod.gui.hud.HudManager.INSTANCE.getModule(com.caeser.mod.gui.hud.ScoreboardModule.class);
            if (sm != null) {
                // Background covers exactly what vanilla filled
                int bw = com.caeser.mod.gui.hud.ScoreboardModule.lastMaxX - com.caeser.mod.gui.hud.ScoreboardModule.lastMinX;
                int bh = com.caeser.mod.gui.hud.ScoreboardModule.lastMaxY - com.caeser.mod.gui.hud.ScoreboardModule.lastMinY;
                com.caeser.mod.gui.hud.IHudModule.drawBackground(context, sm, bw, bh,
                    com.caeser.mod.config.CaeserConfig.INSTANCE.scoreboardBgType,
                    com.caeser.mod.config.CaeserConfig.INSTANCE.scoreboardBgColor,
                    com.caeser.mod.config.CaeserConfig.INSTANCE.scoreboardOutlineColor,
                    com.caeser.mod.config.CaeserConfig.INSTANCE.scoreboardBgCornerRadius);
            }
            
            // Re-center so vanilla text renders exactly over our background
            context.getMatrices().translate((float)-vanillaX, (float)-vanillaY);
        }
    }
    
    @WrapOperation(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void wrapScoreboardFill(DrawContext context, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        if (com.caeser.mod.gui.hud.ScoreboardModule.capturing) {
            com.caeser.mod.gui.hud.ScoreboardModule.capturedMinX = Math.min(com.caeser.mod.gui.hud.ScoreboardModule.capturedMinX, Math.min(x1, x2));
            com.caeser.mod.gui.hud.ScoreboardModule.capturedMinY = Math.min(com.caeser.mod.gui.hud.ScoreboardModule.capturedMinY, Math.min(y1, y2));
            com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxX = Math.max(com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxX, Math.max(x1, x2));
            com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxY = Math.max(com.caeser.mod.gui.hud.ScoreboardModule.capturedMaxY, Math.max(y1, y2));
        }
        if (!com.caeser.mod.config.CaeserConfig.INSTANCE.customScoreboard) {
            original.call(context, x1, y1, x2, y2, color);
        }
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("RETURN"))
    private void afterRenderScoreboard(DrawContext context, net.minecraft.scoreboard.ScoreboardObjective objective, CallbackInfo ci) {
        com.caeser.mod.gui.hud.ScoreboardModule.capturing = false;
        if (!com.caeser.mod.config.CaeserConfig.INSTANCE.customScoreboard) return;
        
        if (com.caeser.mod.gui.hud.ScoreboardModule.lastMinX != -1) {
            context.getMatrices().popMatrix();
        }
    }
}
