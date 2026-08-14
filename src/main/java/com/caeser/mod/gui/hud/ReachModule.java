package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ReachModule implements IHudModule {
    private static double lastReach = 0.0;
    private static long lastHitTime = 0;

    public static void updateReach(double reach) {
        lastReach = reach;
        lastHitTime = System.currentTimeMillis();
    }

    @Override
    public String getName() {
        return "Reach Display";
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.reachX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.reachY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.reachX = x;
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.reachY = y;
    }

    @Override
    public int getWidth() {
        return 80;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.reachScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.reachScale = scale;
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.reachDisplay;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.reachDisplay = enabled;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (System.currentTimeMillis() - lastHitTime > 2000) {
            lastReach = 0.0; // Reset after 2 seconds
        }
        
        String text = "Reach: " + (lastReach > 0 ? String.format("%.2f", lastReach) : "0.00");
        
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());
        
        IHudModule.drawBackground(context, this, getWidth(), getHeight(),
            CaeserConfig.INSTANCE.reachBgType, CaeserConfig.INSTANCE.reachBgColor, CaeserConfig.INSTANCE.reachOutlineColor);
            
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, 4, 3, 0xFFFFFFFF);
        
        context.getMatrices().popMatrix();
    }
}
