package com.caeser.mod.gui;

import net.minecraft.client.gui.screen.Screen;

public class TestGui {
    public static void test() {
        for (java.lang.reflect.Method m : Screen.class.getMethods()) {
            if (m.getName().toLowerCase().contains("mouse")) {
                System.out.println("FOUND: " + m);
            }
        }
    }
}
