package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public class PingModule implements IHudModule {
    private int x = 30;
    private int y = 50;
    private float scale = 1.0f;
    private boolean enabled = false;

    @Override
    public String getName() {
        return "Ping";
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.pingX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.pingY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.pingX = x;
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.pingY = y;
    }

    @Override
    public int getWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(getPingText());
    }

    @Override
    public int getHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.pingScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.pingScale = scale;
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.pingEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.pingEnabled = enabled;
    }

    private String getPingText() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
            if (entry != null) {
                return entry.getLatency() + " ms";
            }
        }
        return "0 ms";
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());

        String text = getPingText();
        int width = client.textRenderer.getWidth(text);
        int height = client.textRenderer.fontHeight;

        IHudModule.drawBackground(context, this, width, height, 
            CaeserConfig.INSTANCE.pingBgType, 
            CaeserConfig.INSTANCE.pingBgColor, 
            CaeserConfig.INSTANCE.pingOutlineColor, 
            CaeserConfig.INSTANCE.pingCornerRadius);

        context.drawTextWithShadow(client.textRenderer, text, 0, 0, CaeserConfig.INSTANCE.pingTextColor);

        context.getMatrices().popMatrix();
    }
}

