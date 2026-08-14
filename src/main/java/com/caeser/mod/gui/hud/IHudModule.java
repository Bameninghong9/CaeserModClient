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
    
    static void drawBackground(DrawContext context, IHudModule module, int width, int height, HudBackgroundType type, int bgColor, int outlineColor) {
        if (type == HudBackgroundType.TRANSPARENT) {
            return; // Draw nothing
        }
        
        int padding = 2;
        int x = -padding;
        int y = -padding;
        int w = width + padding * 2;
        int h = height + padding * 2;

        switch (type) {
            case VANILLA:
            case BLUR: // Semi-transparent dark (Blur is too complex for UI)
                context.fill(x, y, x + w, y + h, 0x80000000);
                break;
            case COLOR:
                context.fill(x, y, x + w, y + h, bgColor);
                break;
            case OUTLINE:
                context.fill(x, y, x + w, y + h, 0x80000000); // Inner bg
                // Draw outline
                context.fill(x, y, x + w, y + 1, outlineColor); // Top
                context.fill(x, y + h - 1, x + w, y + h, outlineColor); // Bottom
                context.fill(x, y, x + 1, y + h, outlineColor); // Left
                context.fill(x + w - 1, y, x + w, y + h, outlineColor); // Right
                break;
            case TOOLTIP:
                context.fill(x, y, x + w, y + h, 0xF0100010);
                context.fill(x, y, x + w, y + 1, 0xFF5000FF);
                context.fill(x, y + h - 1, x + w, y + h, 0xFF5000FF);
                context.fill(x, y, x + 1, y + h, 0xFF5000FF);
                context.fill(x + w - 1, y, x + w, y + h, 0xFF5000FF);
                break;
        }
    }
}
