package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import java.util.ArrayList;
import java.util.List;

public class KeyboardModule implements IHudModule {

    private final List<KeyElement> keys = new ArrayList<>();

    public KeyboardModule() {
    }

    private void initKeys() {
        int size = 20;
        int gap = 2;
        int currentY = 0;
        int currentX = 0;
        
        // Row 1: Esc, F-keys (skip or include? User says "full keyboard", let's include Esc and Numbers for now, maybe standard 60% layout is enough)
        // Let's do a 60% layout
        // Row 1
        int[] r1Keys = {org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE, org.lwjgl.glfw.GLFW.GLFW_KEY_1, org.lwjgl.glfw.GLFW.GLFW_KEY_2, org.lwjgl.glfw.GLFW.GLFW_KEY_3, org.lwjgl.glfw.GLFW.GLFW_KEY_4, org.lwjgl.glfw.GLFW.GLFW_KEY_5, org.lwjgl.glfw.GLFW.GLFW_KEY_6, org.lwjgl.glfw.GLFW.GLFW_KEY_7, org.lwjgl.glfw.GLFW.GLFW_KEY_8, org.lwjgl.glfw.GLFW.GLFW_KEY_9, org.lwjgl.glfw.GLFW.GLFW_KEY_0, org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS, org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL, org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE};
        String[] r1Names = {"Esc", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "Back"};
        for (int i = 0; i < r1Keys.length; i++) {
            int w = (i == r1Keys.length - 1) ? (size * 2) : size;
            addKey(r1Names[i], r1Keys[i], currentX, currentY, w, size);
            currentX += w + gap;
        }

        // Row 2
        currentX = 0;
        currentY += size + gap;
        int[] r2Keys = {org.lwjgl.glfw.GLFW.GLFW_KEY_TAB, org.lwjgl.glfw.GLFW.GLFW_KEY_Q, org.lwjgl.glfw.GLFW.GLFW_KEY_W, org.lwjgl.glfw.GLFW.GLFW_KEY_E, org.lwjgl.glfw.GLFW.GLFW_KEY_R, org.lwjgl.glfw.GLFW.GLFW_KEY_T, org.lwjgl.glfw.GLFW.GLFW_KEY_Z, org.lwjgl.glfw.GLFW.GLFW_KEY_U, org.lwjgl.glfw.GLFW.GLFW_KEY_I, org.lwjgl.glfw.GLFW.GLFW_KEY_O, org.lwjgl.glfw.GLFW.GLFW_KEY_P, org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_BRACKET, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_BRACKET, org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER};
        String[] r2Names = {"Tab", "Q", "W", "E", "R", "T", "Z", "U", "I", "O", "P", "[", "]", "Enter"};
        for (int i = 0; i < r2Keys.length; i++) {
            int w = (i == 0) ? (int)(size * 1.5) : (i == r2Keys.length - 1) ? (int)(size * 1.5) + gap : size;
            addKey(r2Names[i], r2Keys[i], currentX, currentY, w, size);
            currentX += w + gap;
        }

        // Row 3
        currentX = 0;
        currentY += size + gap;
        int[] r3Keys = {org.lwjgl.glfw.GLFW.GLFW_KEY_CAPS_LOCK, org.lwjgl.glfw.GLFW.GLFW_KEY_A, org.lwjgl.glfw.GLFW.GLFW_KEY_S, org.lwjgl.glfw.GLFW.GLFW_KEY_D, org.lwjgl.glfw.GLFW.GLFW_KEY_F, org.lwjgl.glfw.GLFW.GLFW_KEY_G, org.lwjgl.glfw.GLFW.GLFW_KEY_H, org.lwjgl.glfw.GLFW.GLFW_KEY_J, org.lwjgl.glfw.GLFW.GLFW_KEY_K, org.lwjgl.glfw.GLFW.GLFW_KEY_L, org.lwjgl.glfw.GLFW.GLFW_KEY_SEMICOLON, org.lwjgl.glfw.GLFW.GLFW_KEY_APOSTROPHE, org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSLASH};
        String[] r3Names = {"Caps", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "'", "\\"};
        for (int i = 0; i < r3Keys.length; i++) {
            int w = (i == 0) ? (int)(size * 1.8) : (i == r3Keys.length - 1) ? (int)(size * 2.2) : size;
            addKey(r3Names[i], r3Keys[i], currentX, currentY, w, size);
            currentX += w + gap;
        }

        // Row 4
        currentX = 0;
        currentY += size + gap;
        int[] r4Keys = {org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT, org.lwjgl.glfw.GLFW.GLFW_KEY_Y, org.lwjgl.glfw.GLFW.GLFW_KEY_X, org.lwjgl.glfw.GLFW.GLFW_KEY_C, org.lwjgl.glfw.GLFW.GLFW_KEY_V, org.lwjgl.glfw.GLFW.GLFW_KEY_B, org.lwjgl.glfw.GLFW.GLFW_KEY_N, org.lwjgl.glfw.GLFW.GLFW_KEY_M, org.lwjgl.glfw.GLFW.GLFW_KEY_COMMA, org.lwjgl.glfw.GLFW.GLFW_KEY_PERIOD, org.lwjgl.glfw.GLFW.GLFW_KEY_SLASH, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT};
        String[] r4Names = {"Shift", "Y", "X", "C", "V", "B", "N", "M", ",", ".", "/", "RShift"};
        for (int i = 0; i < r4Keys.length; i++) {
            int w = (i == 0) ? (int)(size * 2.4) : (i == r4Keys.length - 1) ? (int)(size * 2.6) : size;
            addKey(r4Names[i], r4Keys[i], currentX, currentY, w, size);
            currentX += w + gap;
        }

        // Row 5
        currentX = 0;
        currentY += size + gap;
        int[] r5Keys = {org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL, org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SUPER, org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_ALT, org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_ALT, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SUPER, org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL};
        String[] r5Names = {"Ctrl", "Win", "Alt", "Space", "RAlt", "Win", "RCtrl"};
        for (int i = 0; i < r5Keys.length; i++) {
            int w = (i == 3) ? (int)(size * 6.1) : (int)(size * 1.3);
            addKey(r5Names[i], r5Keys[i], currentX, currentY, w, size);
            currentX += w + gap;
        }
    }

    private void addKey(String name, int glfwKey, int x, int y, int w, int h) {
        KeyElement element = new KeyElement(name, glfwKey, x, y, w, h);
        if (CaeserConfig.INSTANCE.fullKeyboardLayout.containsKey(name)) {
            int[] layout = CaeserConfig.INSTANCE.fullKeyboardLayout.get(name);
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
    public String getName() { return "Keyboard"; }

    @Override
    public int getX() { return CaeserConfig.INSTANCE.keyboardX; }

    @Override
    public int getY() { return CaeserConfig.INSTANCE.keyboardY; }

    @Override
    public void setX(int x) { CaeserConfig.INSTANCE.keyboardX = x; CaeserConfig.save(); }

    @Override
    public void setY(int y) { CaeserConfig.INSTANCE.keyboardY = y; CaeserConfig.save(); }

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
    public float getScale() { return CaeserConfig.INSTANCE.keyboardScale; }

    @Override
    public void setScale(float scale) { CaeserConfig.INSTANCE.keyboardScale = scale; CaeserConfig.save(); }

    @Override
    public boolean isEnabled() { return CaeserConfig.INSTANCE.keyboardEnabled; }

    @Override
    public void setEnabled(boolean enabled) { CaeserConfig.INSTANCE.keyboardEnabled = enabled; CaeserConfig.save(); }

    @Override
    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || !isEnabled()) return;

        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());

        IHudModule.drawBackground(context, this, getWidth(), getHeight(), 
            CaeserConfig.INSTANCE.keyboardBgType, 
            CaeserConfig.INSTANCE.keyboardBgColor, 
            CaeserConfig.INSTANCE.keyboardOutlineColor,
            CaeserConfig.INSTANCE.keyboardCornerRadius);

        for (KeyElement key : getKeys()) {
            if (key.visible) {
                boolean pressed = false;
                if (key.keyBinding != null) {
                    pressed = key.keyBinding.isPressed();
                } else if (key.glfwKey != -1) {
                    pressed = org.lwjgl.glfw.GLFW.glfwGetKey(client.getWindow().getHandle(), key.glfwKey) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
                }
                drawKey(context, client, pressed, key.name, key.offsetX, key.offsetY, key.width, key.height);
            }
        }

        context.getMatrices().popMatrix();
    }

    private void drawKey(DrawContext context, MinecraftClient client, boolean isPressed, String name, int x, int y, int width, int height) {
        int color = isPressed ? 0x80FFFFFF : 0x80000000;
        com.caeser.mod.util.RenderUtils.drawRoundedRect(context, x, y, width, height, CaeserConfig.INSTANCE.keyboardCornerRadius, color);
        
        int textWidth = client.textRenderer.getWidth(name);
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - client.textRenderer.fontHeight) / 2;
        
        context.drawTextWithShadow(client.textRenderer, name, textX, textY, 0xFFFFFFFF);
    }
}





