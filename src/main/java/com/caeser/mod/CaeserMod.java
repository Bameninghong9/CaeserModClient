package com.caeser.mod;

import com.caeser.mod.command.CaeserCommand;
import com.caeser.mod.config.CaeserConfig;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import com.caeser.mod.gui.CaeserSettingsScreen;

public class CaeserMod implements ClientModInitializer {
    public static final String MOD_ID = "caeserclient";
    private static KeyBinding settingsKeyBinding;

    @Override
    public void onInitializeClient() {
        System.out.println("Initializing Caeser Client...");
        
        // Load configuration
        CaeserConfig.load();
        
        // Register client commands
        CaeserCommand.register();

        // Register Keybind (Right Shift)
        settingsKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.caeserclient.settings",
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
        ));

        // Tick event for keybind
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (settingsKeyBinding.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new CaeserSettingsScreen(null));
                }
            }
        });
    }
}
