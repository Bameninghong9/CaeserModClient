package com.caeser.mod;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.Screen;

public class CaeserModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("----- START REFLECTION -----");
        for (java.lang.reflect.Method m : Screen.class.getMethods()) {
            if (m.getName().toLowerCase().contains("mouse")) {
                System.out.println("FOUND METHOD: " + m.getName() + " -> " + m);
            }
        }
        System.out.println("----- END REFLECTION -----");
        System.exit(0);
    }
}
