package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.CaeserSettingsScreen;
import com.caeser.mod.gui.HudBackgroundCategoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class FpsModule implements IHudModule {

    @Override
    public String getName() {
        return "FPS";
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.fps;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.fps = enabled;
        CaeserConfig.save();
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.fpsX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.fpsY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.fpsX = x;
        CaeserConfig.save();
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.fpsY = y;
        CaeserConfig.save();
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.fpsScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.fpsScale = scale;
        CaeserConfig.save();
    }

    @Override
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getFpsString());
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
        
        // Background will be drawn via HudManager looping through IHudModule or here
        IHudModule.drawBackground(context, this, getWidth(), getHeight(), 
            CaeserConfig.INSTANCE.fpsBgType, CaeserConfig.INSTANCE.fpsBgColor, CaeserConfig.INSTANCE.fpsOutlineColor, CaeserConfig.INSTANCE.fpsBgCornerRadius);
            
        context.drawTextWithShadow(textRenderer, getFpsString(), 0, 0, 0xFFFFFFFF);
        
        context.getMatrices().popMatrix();
    }

    public void openSettingsMenu(MinecraftClient client) {
        client.setScreen(new com.caeser.mod.gui.HudBackgroundCategoryScreen(
            client.currentScreen instanceof com.caeser.mod.gui.CaeserSettingsScreen ? (com.caeser.mod.gui.CaeserSettingsScreen)client.currentScreen : new com.caeser.mod.gui.CaeserSettingsScreen(null),
            net.minecraft.text.Text.literal("FPS Display"), this,
            () -> CaeserConfig.INSTANCE.fpsBgType, val -> CaeserConfig.INSTANCE.fpsBgType = val,
            () -> CaeserConfig.INSTANCE.fpsBgColor, val -> CaeserConfig.INSTANCE.fpsBgColor = val,
            () -> CaeserConfig.INSTANCE.fpsOutlineColor, val -> CaeserConfig.INSTANCE.fpsOutlineColor = val,
            () -> CaeserConfig.INSTANCE.fpsBgCornerRadius, val -> CaeserConfig.INSTANCE.fpsBgCornerRadius = val
        ));
    }

    private String getFpsString() {
        return MinecraftClient.getInstance().getCurrentFps() + " FPS";
    }
}
