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

import net.minecraft.client.util.InputUtil;
import java.util.HashSet;
import java.util.Set;
import com.caeser.mod.config.AutoTextEntry;

public class CaeserMod implements ClientModInitializer {
    public static final String MOD_ID = "caeserclient";
    private static KeyBinding settingsKeyBinding;
    private static final Set<Integer> pressedKeys = new HashSet<>();

    @Override
    public void onInitializeClient() {
        System.out.println("Initializing Caeser Client...");
        
        // Load configuration
        CaeserConfig.load();
        
        // Register client commands
        CaeserCommand.register();

        com.caeser.mod.gui.hud.HudManager.INSTANCE.init();

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
            
            // Check AutoText keys
            if (client.currentScreen == null && client.player != null && client.getWindow() != null) {
                net.minecraft.client.util.Window window = client.getWindow();
                for (AutoTextEntry entry : CaeserConfig.INSTANCE.autoTexts) {
                    if (entry.keyCode != GLFW.GLFW_KEY_UNKNOWN) {
                        boolean isPressed = InputUtil.isKeyPressed(window, entry.keyCode);
                        if (isPressed && !pressedKeys.contains(entry.keyCode)) {
                            pressedKeys.add(entry.keyCode);
                            // Execute command
                            if (entry.text.startsWith("/")) {
                                client.player.networkHandler.sendChatCommand(entry.text.substring(1));
                            } else {
                                client.player.networkHandler.sendChatMessage(entry.text);
                            }
                        } else if (!isPressed && pressedKeys.contains(entry.keyCode)) {
                            pressedKeys.remove(entry.keyCode);
                        }
                    }
                }
            }
        });
    }
}
