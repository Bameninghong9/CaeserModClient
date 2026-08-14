package com.caeser.mod.config;

import org.lwjgl.glfw.GLFW;

public class AutoTextEntry {
    public int keyCode;
    public String text;

    public AutoTextEntry(int keyCode, String text) {
        this.keyCode = keyCode;
        this.text = text;
    }

    public AutoTextEntry() {
        this.keyCode = GLFW.GLFW_KEY_UNKNOWN;
        this.text = "";
    }
}
