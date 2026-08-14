package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class KeystrokesModule implements IHudModule {

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.keystrokes;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.keystrokes = enabled;
        CaeserConfig.save();
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (!isEnabled()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) return;

        int x = CaeserConfig.INSTANCE.keystrokesX;
        int y = CaeserConfig.INSTANCE.keystrokesY;
        
        // Key sizes
        int size = 20;
        int gap = 2;
        int width = size * 3 + gap * 2;
        int height = size * 3 + gap * 2; // W, S, Space row

        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)x, (float)y);
        context.getMatrices().scale(CaeserConfig.INSTANCE.keystrokesScale, CaeserConfig.INSTANCE.keystrokesScale);

        IHudModule.drawBackground(context, this, width, height, 
            CaeserConfig.INSTANCE.keystrokesBgType, 
            CaeserConfig.INSTANCE.keystrokesBgColor, 
            CaeserConfig.INSTANCE.keystrokesOutlineColor,
            CaeserConfig.INSTANCE.keystrokesBgCornerRadius);

        // Draw Keys
        // Row 1: W
        drawKey(context, client, client.options.forwardKey.isPressed(), "W", size + gap, 0, size, size);
        
        // Row 2: A S D
        drawKey(context, client, client.options.leftKey.isPressed(), "A", 0, size + gap, size, size);
        drawKey(context, client, client.options.backKey.isPressed(), "S", size + gap, size + gap, size, size);
        drawKey(context, client, client.options.rightKey.isPressed(), "D", (size + gap) * 2, size + gap, size, size);

        // Row 3: LMB RMB
        int mouseWidth = (width - gap) / 2;
        drawKey(context, client, client.options.attackKey.isPressed(), "LMB", 0, (size + gap) * 2, mouseWidth, size);
        drawKey(context, client, client.options.useKey.isPressed(), "RMB", mouseWidth + gap, (size + gap) * 2, width - mouseWidth - gap, size);

        // Row 4: Space
        drawKey(context, client, client.options.jumpKey.isPressed(), "----", 0, (size + gap) * 3, width, size / 2);

        context.getMatrices().popMatrix();
    }

    private void drawKey(DrawContext context, MinecraftClient client, boolean pressed, String text, int x, int y, int w, int h) {
        int color = pressed ? 0x80FFFFFF : 0x40000000;
        int textColor = pressed ? 0xFF000000 : 0xFFFFFFFF;
        
        context.fill(x, y, x + w, y + h, color);
        
        int textWidth = client.textRenderer.getWidth(text);
        context.drawTextWithShadow(client.textRenderer, text, x + (w - textWidth) / 2, y + (h - 8) / 2, textColor);
    }

    @Override
    public String getName() {
        return "Keystrokes";
    }

    @Override
    public int getX() { return CaeserConfig.INSTANCE.keystrokesX; }

    @Override
    public int getY() { return CaeserConfig.INSTANCE.keystrokesY; }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.keystrokesX = x;
        CaeserConfig.save();
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.keystrokesY = y;
        CaeserConfig.save();
    }

    @Override
    public int getWidth() {
        return (20 * 3 + 2 * 2);
    }

    @Override
    public int getHeight() {
        return (20 * 3 + 2 * 2) + 10 + 2; // Extra for spacebar
    }

    @Override
    public float getScale() { return CaeserConfig.INSTANCE.keystrokesScale; }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.keystrokesScale = scale;
        CaeserConfig.save();
    }

    public void toggle() {
        CaeserConfig.INSTANCE.keystrokes = !CaeserConfig.INSTANCE.keystrokes;
        CaeserConfig.save();
    }

    public void openSettingsMenu(MinecraftClient client) {
        client.setScreen(new com.caeser.mod.gui.HudBackgroundCategoryScreen(
            client.currentScreen instanceof com.caeser.mod.gui.CaeserSettingsScreen ? (com.caeser.mod.gui.CaeserSettingsScreen)client.currentScreen : new com.caeser.mod.gui.CaeserSettingsScreen(null),
            Text.literal("Keystrokes"), this,
            () -> CaeserConfig.INSTANCE.keystrokesBgType, val -> CaeserConfig.INSTANCE.keystrokesBgType = val,
            () -> CaeserConfig.INSTANCE.keystrokesBgColor, val -> CaeserConfig.INSTANCE.keystrokesBgColor = val,
            () -> CaeserConfig.INSTANCE.keystrokesOutlineColor, val -> CaeserConfig.INSTANCE.keystrokesOutlineColor = val,
            () -> CaeserConfig.INSTANCE.keystrokesBgCornerRadius, val -> CaeserConfig.INSTANCE.keystrokesBgCornerRadius = val
        ));
    }
}
