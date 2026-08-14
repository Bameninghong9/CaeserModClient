package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayDeque;
import java.util.Queue;

public class CpsModule implements IHudModule {
    private static final Queue<Long> clicks = new ArrayDeque<>();
    
    public static void addClick() {
        if (!CaeserConfig.INSTANCE.cpsDisplay) return;
        clicks.add(System.currentTimeMillis());
    }

    private int getCps() {
        long time = System.currentTimeMillis();
        while (!clicks.isEmpty() && time - clicks.peek() > 1000) {
            clicks.poll();
        }
        return clicks.size();
    }

    @Override
    public String getName() {
        return "CPS Display";
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.cpsX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.cpsY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.cpsX = x;
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.cpsY = y;
    }

    @Override
    public int getWidth() {
        return 50;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.cpsScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.cpsScale = scale;
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.cpsDisplay;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.cpsDisplay = enabled;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        long time = System.currentTimeMillis();
        while (!clicks.isEmpty() && time - clicks.peek() > 1000) {
            clicks.poll();
        }
        String text = "CPS: " + clicks.size();
        
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());
        
        IHudModule.drawBackground(context, this, getWidth(), getHeight(),
            CaeserConfig.INSTANCE.cpsBgType, CaeserConfig.INSTANCE.cpsBgColor, CaeserConfig.INSTANCE.cpsOutlineColor, CaeserConfig.INSTANCE.cpsBgCornerRadius);
            
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, 4, 3, 0xFFFFFFFF);
        
        context.getMatrices().popMatrix();
    }
}
