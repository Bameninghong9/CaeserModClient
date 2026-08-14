package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.CaeserMainMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class BossbarModule implements IHudModule {
    public static long lastBossbarRenderTime = 0;

    @Override
    public String getName() {
        return "Bossbar";
    }

    @Override
    public int getX() {
        int configX = CaeserConfig.INSTANCE.bossbarX;
        if (configX == -1) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getWindow() != null) {
                return (client.getWindow().getScaledWidth() / 2) - (getWidth() / 2);
            }
            return 100;
        }
        return configX;
    }

    @Override
    public int getY() {
        int configY = CaeserConfig.INSTANCE.bossbarY;
        if (configY == -1) {
            return 12;
        }
        return configY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.bossbarX = x;
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.bossbarY = y;
    }

    @Override
    public int getWidth() {
        return 182;
    }

    @Override
    public int getHeight() {
        return 15; // 5 for the bar, but let's make it 15 to include text
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.bossbarScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.bossbarScale = scale;
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.customBossbar;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.customBossbar = enabled;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        // Only render placeholder if we are in the HUD Editor
        if (MinecraftClient.getInstance().currentScreen instanceof CaeserMainMenuScreen) {
            boolean hasReal = (System.currentTimeMillis() - lastBossbarRenderTime) < 500;
            if (hasReal) {
                MinecraftClient.getInstance().inGameHud.getBossBarHud().render(context);
            } else {
                context.getMatrices().pushMatrix();
                context.getMatrices().translate((float)getX(), (float)getY());
                context.getMatrices().scale(getScale(), getScale());
                
                context.fill(0, 0, 182, 15, 0x80800080); // Purple translucent
                context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, "Bossbar", 70, 4, 0xFFFFFFFF);
                
                context.getMatrices().popMatrix();
            }
        }
    }
}
