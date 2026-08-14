package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.CaeserSettingsScreen;
import com.caeser.mod.gui.UptimeCategoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import java.lang.management.ManagementFactory;

public class UptimeModule implements IHudModule {

    private long startTimeMillis;

    public UptimeModule() {
        this.startTimeMillis = ManagementFactory.getRuntimeMXBean().getStartTime();
    }

    @Override
    public String getName() {
        return "Uptime";
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.uptime;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.uptime = enabled;
        CaeserConfig.save();
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.uptimeX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.uptimeY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.uptimeX = x;
        CaeserConfig.save();
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.uptimeY = y;
        CaeserConfig.save();
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.uptimeScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.uptimeScale = scale;
        CaeserConfig.save();
    }

    @Override
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getUptimeString());
    }

    @Override
    public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());
        
        IHudModule.drawBackground(context, this, getWidth(), getHeight(),
            CaeserConfig.INSTANCE.uptimeBgType, CaeserConfig.INSTANCE.uptimeBgColor, CaeserConfig.INSTANCE.uptimeOutlineColor);
            
        context.drawTextWithShadow(textRenderer, getUptimeString(), 0, 0, 0xFFFFFFFF);
        
        context.getMatrices().popMatrix();
    }

    public void openSettingsMenu(MinecraftClient client) {
        client.setScreen(new com.caeser.mod.gui.UptimeCategoryScreen(client.currentScreen instanceof com.caeser.mod.gui.CaeserSettingsScreen ? (com.caeser.mod.gui.CaeserSettingsScreen)client.currentScreen : new com.caeser.mod.gui.CaeserSettingsScreen(null), this));
    }

    private String getUptimeString() {
        long current = System.currentTimeMillis();
        long diff = current - startTimeMillis;
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        
        if (CaeserConfig.INSTANCE.uptimeFormat == CaeserConfig.UptimeFormat.TEXT) {
            if (diffHours > 0) {
                return diffHours + "h " + diffMinutes + "m";
            }
            return diffMinutes + "m " + diffSeconds + "s";
        } else {
            return String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
        }
    }
}
