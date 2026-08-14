package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.CaeserMainMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class ScoreboardModule implements IHudModule {
    public static long lastScoreboardRenderTime = 0;
    
    public static int capturedMinX = Integer.MAX_VALUE;
    public static int capturedMinY = Integer.MAX_VALUE;
    public static int capturedMaxX = Integer.MIN_VALUE;
    public static int capturedMaxY = Integer.MIN_VALUE;
    public static boolean capturing = false;
    
    public static int lastMinX = -1;
    public static int lastMinY = -1;
    public static int lastMaxX = -1;
    public static int lastMaxY = -1;

    @Override
    public String getName() {
        return "Scoreboard";
    }

    @Override
    public int getX() {
        int configX = CaeserConfig.INSTANCE.scoreboardX;
        if (configX == -1) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getWindow() != null) {
                return client.getWindow().getScaledWidth() - (int)(getWidth() * getScale()) - 2;
            }
            return 300;
        }
        return configX;
    }

    @Override
    public int getY() {
        int configY = CaeserConfig.INSTANCE.scoreboardY;
        if (configY == -1) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.getWindow() != null) {
                return (client.getWindow().getScaledHeight() / 2) - (int)(getHeight() * getScale() / 2);
            }
            return 100;
        }
        return configY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.scoreboardX = x;
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.scoreboardY = y;
    }

    @Override
    public int getWidth() {
        if (lastMinX != -1 && lastMaxX != -1) {
            return (lastMaxX - lastMinX);
        }
        return 100;
    }

    @Override
    public int getHeight() {
        if (lastMinY != -1 && lastMaxY != -1) {
            return (lastMaxY - lastMinY);
        }
        return 120;
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.scoreboardScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.scoreboardScale = scale;
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.customScoreboard;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.customScoreboard = enabled;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        // Only render placeholder if we are in the HUD Editor
        if (client.currentScreen instanceof CaeserMainMenuScreen) {
            boolean hasReal = (System.currentTimeMillis() - lastScoreboardRenderTime) < 500;
            if (hasReal) {
                net.minecraft.scoreboard.Scoreboard scoreboard = client.world.getScoreboard();
                net.minecraft.scoreboard.ScoreboardObjective objective = scoreboard.getObjectiveForSlot(net.minecraft.scoreboard.ScoreboardDisplaySlot.SIDEBAR);
                if (objective != null) {
                    ((com.caeser.mod.mixin.InGameHudAccessor)client.inGameHud).invokeRenderScoreboardSidebar(context, objective);
                }
            } else {
                context.getMatrices().pushMatrix();
                context.getMatrices().translate((float)getX(), (float)getY());
                context.getMatrices().scale(getScale(), getScale());
                
                IHudModule.drawBackground(context, this, 100, 120,
                    CaeserConfig.INSTANCE.scoreboardBgType, CaeserConfig.INSTANCE.scoreboardBgColor, CaeserConfig.INSTANCE.scoreboardOutlineColor);
                    
                context.drawTextWithShadow(client.textRenderer, "Scoreboard", 20, 5, 0xFFFFFFFF);
                
                context.getMatrices().popMatrix();
            }
        }
    }
}
