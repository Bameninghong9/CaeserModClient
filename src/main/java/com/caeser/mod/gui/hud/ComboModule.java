package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ComboModule implements IHudModule {
    private static int combo = 0;
    private static long lastHitTime = 0;

    public static void addCombo() {
        combo++;
        lastHitTime = System.currentTimeMillis();
    }

    public static void resetCombo() {
        combo = 0;
    }

    @Override
    public String getName() {
        return "Combo Counter";
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.comboX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.comboY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.comboX = x;
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.comboY = y;
    }

    @Override
    public int getWidth() {
        return 70;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.comboScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.comboScale = scale;
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.comboCounter;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.comboCounter = enabled;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (System.currentTimeMillis() - lastHitTime > 3000) {
            combo = 0; // Reset after 3 seconds of no hits
        }
        
        String text = "Combo: " + combo;
        
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());
        
        IHudModule.drawBackground(context, this, getWidth(), getHeight(),
            CaeserConfig.INSTANCE.comboBgType, CaeserConfig.INSTANCE.comboBgColor, CaeserConfig.INSTANCE.comboOutlineColor);
            
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, 4, 3, 0xFFFFFFFF);
        
        context.getMatrices().popMatrix();
    }
}
