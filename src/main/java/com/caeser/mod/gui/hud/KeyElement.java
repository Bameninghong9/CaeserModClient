package com.caeser.mod.gui.hud;

import net.minecraft.client.option.KeyBinding;

public class KeyElement {
    public String name;
    public KeyBinding keyBinding;
    public int offsetX;
    public int offsetY;
    public int width;
    public int height;
    public boolean visible;

    public int glfwKey = -1;

    public KeyElement(String name, KeyBinding keyBinding, int offsetX, int offsetY, int width, int height) {
        this.name = name;
        this.keyBinding = keyBinding;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.visible = true;
    }

    public KeyElement(String name, int glfwKey, int offsetX, int offsetY, int width, int height) {
        this.name = name;
        this.glfwKey = glfwKey;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.visible = true;
    }
}
