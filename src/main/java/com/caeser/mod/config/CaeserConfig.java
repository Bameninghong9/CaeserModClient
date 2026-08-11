package com.caeser.mod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CaeserConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "caeserclient.json");

    // Hitbox settings
    public boolean hitboxes = false;
    
    // Player Hitboxes
    public float playerHitboxThickness = 2.0f;
    public int playerHitboxColor = 0xFFFFFFFF; // White
    public int playerHitboxHoverColor = 0xFFFF0000; // Red
    
    // Mob Hitboxes
    public float mobHitboxThickness = 2.0f;
    public int mobHitboxColor = 0xFFFFFFFF; // White
    public int mobHitboxHoverColor = 0xFFFF0000; // Red
    
    // HitColor Settings
    public boolean hitColors = false;
    public int hitColor = 0xB2FF0000; // Semi-transparent Red
    
    // Add additional settings as needed

    public static CaeserConfig INSTANCE = new CaeserConfig();

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, CaeserConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
