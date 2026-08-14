package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class KeystrokesModule implements IHudModule {

    private final List<KeyElement> keys = new ArrayList<>();

    public KeystrokesModule() {
    }

    private void initKeys() {
        MinecraftClient client = MinecraftClient.getInstance();
        int size = 20;
        int gap = 2;
        int mouseWidth = (size * 3 + gap * 2 - gap) / 2;
        
        // Default positions
        addKey("W", client.options.forwardKey, size + gap, 0, size, size);
        addKey("A", client.options.leftKey, 0, size + gap, size, size);
        addKey("S", client.options.backKey, size + gap, size + gap, size, size);
        addKey("D", client.options.rightKey, (size + gap) * 2, size + gap, size, size);
        addKey("LMB", client.options.attackKey, 0, (size + gap) * 2, mouseWidth, size);
        addKey("RMB", client.options.useKey, mouseWidth + gap, (size + gap) * 2, mouseWidth, size);
        addKey("Space", client.options.jumpKey, 0, (size + gap) * 3, size * 3 + gap * 2, size / 2);
    }

    private void addKey(String name, KeyBinding binding, int x, int y, int w, int h) {
        KeyElement element = new KeyElement(name, binding, x, y, w, h);
        if (CaeserConfig.INSTANCE.keystrokesLayout.containsKey(name)) {
            int[] layout = CaeserConfig.INSTANCE.keystrokesLayout.get(name);
            element.offsetX = layout[0];
            element.offsetY = layout[1];
            element.visible = layout[2] == 1;
        }
        keys.add(element);
    }

    public List<KeyElement> getKeys() {
        if (keys.isEmpty() && MinecraftClient.getInstance().options != null) {
            initKeys();
        }
        return keys;
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
    public void setX(int x) { CaeserConfig.INSTANCE.keystrokesX = x; CaeserConfig.save(); }

    @Override
    public void setY(int y) { CaeserConfig.INSTANCE.keystrokesY = y; CaeserConfig.save(); }

    @Override
    public int getWidth() {
        int maxX = 0;
        for (KeyElement key : getKeys()) {
            if (key.visible && key.offsetX + key.width > maxX) {
                maxX = key.offsetX + key.width;
            }
        }
        return Math.max(20, maxX);
    }

    @Override
    public int getHeight() {
        int maxY = 0;
        for (KeyElement key : getKeys()) {
            if (key.visible && key.offsetY + key.height > maxY) {
                maxY = key.offsetY + key.height;
            }
        }
        return Math.max(20, maxY);
    }

    @Override
    public float getScale() { return CaeserConfig.INSTANCE.keystrokesScale; }

    @Override
    public void setScale(float scale) { CaeserConfig.INSTANCE.keystrokesScale = scale; CaeserConfig.save(); }

    @Override
    public boolean isEnabled() { return CaeserConfig.INSTANCE.keystrokes; }

    @Override
    public void setEnabled(boolean enabled) { CaeserConfig.INSTANCE.keystrokes = enabled; CaeserConfig.save(); }

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || !isEnabled()) return;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());

        IHudModule.drawBackground(context, this, getWidth(), getHeight(), 
            CaeserConfig.INSTANCE.keystrokesBgType, 
            CaeserConfig.INSTANCE.keystrokesBgColor, 
            CaeserConfig.INSTANCE.keystrokesOutlineColor,
            CaeserConfig.INSTANCE.keystrokesCornerRadius);

        for (KeyElement key : getKeys()) {
            if (key.visible) {
                drawKey(context, client, key.keyBinding.isPressed(), key.name, key.offsetX, key.offsetY, key.width, key.height);
            }
        }

        context.getMatrices().popMatrix();
    }

    private void drawKey(DrawContext context, MinecraftClient client, boolean isPressed, String name, int x, int y, int width, int height) {
        int color = isPressed ? 0x80FFFFFF : 0x80000000;
        com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, width, height, CaeserConfig.INSTANCE.keystrokesCornerRadius, color);
        
        int textWidth = client.textRenderer.getWidth(name);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - client.textRenderer.fontHeight) / 2;
        
        context.drawTextWithShadow(client.textRenderer, name, textX, textY, 0xFFFFFFFF);
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
            () -> CaeserConfig.INSTANCE.keystrokesCornerRadius, val -> CaeserConfig.INSTANCE.keystrokesCornerRadius = val
        ));
    }
}


