package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class TargetHudModule implements IHudModule {

    @Override
    public String getName() {
        return "Target HUD";
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.targetHudX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.targetHudY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.targetHudX = x;
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.targetHudY = y;
    }

    @Override
    public int getWidth() {
        return 140;
    }

    @Override
    public int getHeight() {
        return 40;
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.targetHudScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.targetHudScale = scale;
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.targetHud;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.targetHud = enabled;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        HitResult hit = client.crosshairTarget;
        
        if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) hit).getEntity();
            
            if (entity instanceof LivingEntity living) {
                context.getMatrices().pushMatrix();
                context.getMatrices().translate((float)getX(), (float)getY());
                context.getMatrices().scale(getScale(), getScale());

                // Draw background
                IHudModule.drawBackground(context, this, 140, 40,
                    CaeserConfig.INSTANCE.targetHudBgType, CaeserConfig.INSTANCE.targetHudBgColor, CaeserConfig.INSTANCE.targetHudOutlineColor);
                
                // Draw name
                context.drawTextWithShadow(client.textRenderer, living.getName(), 35, 5, 0xFFFFFFFF);
                
                // Draw health text
                float health = living.getHealth();
                float maxHealth = living.getMaxHealth();
                String hpText = String.format("%.1f", health) + " \u2764"; // Heart symbol
                context.drawTextWithShadow(client.textRenderer, hpText, 35, 18, 0xFFFF5555);
                
                // Draw health bar
                float healthPercent = Math.min(1.0f, Math.max(0.0f, health / maxHealth));
                int barWidth = 100;
                context.fill(35, 30, 35 + barWidth, 34, 0xFF555555);
                context.fill(35, 30, 35 + (int)(barWidth * healthPercent), 34, 0xFF55FF55);

                context.getMatrices().popMatrix();
            }
        }
    }
}
