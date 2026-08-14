package com.caeser.mod.gui.hud;

import net.minecraft.client.gui.DrawContext;

public interface IHudModule {
    String getName();
    int getX();
    int getY();
    void setX(int x);
    void setY(int y);
    int getWidth();
    int getHeight();
    float getScale();
    void setScale(float scale);
    boolean isEnabled();
    void setEnabled(boolean enabled);
    void render(DrawContext context, float tickDelta);
    
    static void drawBackground(DrawContext context, IHudModule module, int width, int height, HudBackgroundType type, int bgColor, int outlineColor, float cornerRadius) {
        if (type == HudBackgroundType.TRANSPARENT) {
            return; // Draw nothing
        }
        
        int padding = 3;
        float x = -padding;
        float y = -padding;
        float w = width + padding * 2;
        float h = height + padding * 2;

        switch (type) {
            case VANILLA:
                // Clean vanilla-like transparent black
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, w, h, cornerRadius, 0x80000000);
                break;
            case BLUR:
                // Slightly darker blur with a subtle bright inner edge
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, w, h, cornerRadius, 0x90050505);
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x + 0.5f, y + 0.5f, w - 1, h - 1, cornerRadius > 0 ? cornerRadius - 0.5f : 0, 0x20FFFFFF);
                break;
            case COLOR:
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, w, h, cornerRadius, bgColor);
                break;
            case OUTLINE:
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, w, h, cornerRadius, outlineColor);
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x + 1.5f, y + 1.5f, w - 3, h - 3, cornerRadius > 0 ? cornerRadius - 1.5f : 0, 0xA0000000);
                break;
            case TOOLTIP:
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, w, h, cornerRadius, 0xFF2A0054);
                com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x + 1, y + 1, w - 2, h - 2, cornerRadius > 0 ? cornerRadius - 1 : 0, 0xD010001A);
                break;
        }
    }
}
