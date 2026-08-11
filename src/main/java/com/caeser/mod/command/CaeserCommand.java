package com.caeser.mod.command;

import com.caeser.mod.gui.CaeserSettingsScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
//? if <=1.21.11 {
import net.minecraft.text.Text;
//?} else {
/*import net.minecraft.network.chat.Component;
*///?}

public class CaeserCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("caeser")
                    .then(ClientCommandManager.literal("settings")
                            .executes(context -> {
                                //? if <=1.21.11 {
                                context.getSource().sendFeedback(Text.literal("Opening Caeser Settings..."));
                                //?} else {
                                /*context.getSource().sendFeedback(Component.literal("Opening Caeser Settings..."));
                                *///?}
                                
                                // Open screen on next tick
                                //? if <=1.21.11 {
                                MinecraftClient.getInstance().send(() -> {
                                    MinecraftClient.getInstance().setScreen(new CaeserSettingsScreen(MinecraftClient.getInstance().currentScreen));
                                });
                                //?} else {
                                /*MinecraftClient.getInstance().tell(() -> {
                                    MinecraftClient.getInstance().setScreen(new CaeserSettingsScreen(MinecraftClient.getInstance().screen));
                                });
                                *///?}
                                return 1;
                            })
                    ));
        });
    }
}
