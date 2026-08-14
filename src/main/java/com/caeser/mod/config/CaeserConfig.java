package com.caeser.mod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import com.caeser.mod.gui.hud.HudBackgroundType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CaeserConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "caeserclient.json");

    
    // HitColor Settings
    public boolean hitColors = false;
    public int hitColor = 0xB2FF0000; // Semi-transparent Red
    
    // Additional Combat Settings
    public boolean comboCounter = false;
    public boolean reachDisplay = false;
    public boolean cpsDisplay = false;
    public boolean targetHud = false;
    public boolean lowFire = false;
    public float lowFireHeight = -0.3f; // Default translated down by 0.3
    
    // HUD Layout
    public int cpsX = 10, cpsY = 10;
    public float cpsScale = 1.0f;
    public HudBackgroundType cpsBgType = HudBackgroundType.TRANSPARENT;
    public int cpsBgColor = 0x80000000;
    public int cpsOutlineColor = 0xFF000000;
    public float cpsBgCornerRadius = 0.0f;

    public int comboX = 10, comboY = 25;
    public float comboScale = 1.0f;
    public HudBackgroundType comboBgType = HudBackgroundType.TRANSPARENT;
    public int comboBgColor = 0x80000000;
    public int comboOutlineColor = 0xFF000000;
    public float comboBgCornerRadius = 0.0f;

    public int reachX = 10, reachY = 40;
    public float reachScale = 1.0f;
    public HudBackgroundType reachBgType = HudBackgroundType.TRANSPARENT;
    public int reachBgColor = 0x80000000;
    public int reachOutlineColor = 0xFF000000;
    public float reachBgCornerRadius = 0.0f;

    public int targetHudX = 200, targetHudY = 100;
    public float targetHudScale = 1.0f;
    public HudBackgroundType targetHudBgType = HudBackgroundType.TRANSPARENT;
    public int targetHudBgColor = 0x80000000;
    public int targetHudOutlineColor = 0xFF000000;
    public float targetHudBgCornerRadius = 0.0f;
    
    // Vanilla HUD elements
    public boolean customBossbar = true;
    public int bossbarX = -1, bossbarY = -1;
    public float bossbarScale = 1.0f;
    public HudBackgroundType bossbarBgType = HudBackgroundType.TRANSPARENT;
    public int bossbarBgColor = 0x80000000;
    public int bossbarOutlineColor = 0xFF000000;
    public float bossbarBgCornerRadius = 0.0f;
    
    public boolean customScoreboard = true;
    public int scoreboardX = -1, scoreboardY = -1;
    public float scoreboardScale = 1.0f;
    public HudBackgroundType scoreboardBgType = HudBackgroundType.TRANSPARENT;
    public int scoreboardBgColor = 0x80000000;
    public int scoreboardOutlineColor = 0xFF000000;
    public float scoreboardBgCornerRadius = 0.0f;

    // Uptime Module
    public boolean uptime = false;
    public enum UptimeFormat { TEXT, DIGITAL }
    public UptimeFormat uptimeFormat = UptimeFormat.TEXT;
    public int uptimeX = 10, uptimeY = 10;
    public float uptimeScale = 1.0f;
    public HudBackgroundType uptimeBgType = HudBackgroundType.TRANSPARENT;
    public int uptimeBgColor = 0x80000000;
    public int uptimeOutlineColor = 0xFF000000;
    public float uptimeBgCornerRadius = 0.0f;

    // Coordinates Module
    public boolean coordinates = false;
    public boolean coordsShowX = true;
    public boolean coordsShowY = true;
    public boolean coordsShowZ = true;
    public boolean coordsShowBiome = true;
    public boolean coordsShowDirection = true;
    public int coordsX = 10, coordsY = 25;
    public float coordsScale = 1.0f;
    public HudBackgroundType coordsBgType = HudBackgroundType.TRANSPARENT;
    public int coordsBgColor = 0x80000000;
    public int coordsOutlineColor = 0xFF000000;
    public float coordsBgCornerRadius = 0.0f;

    // FPS Module
    public boolean fps = false;
    public int fpsX = 10, fpsY = 55;
    public float fpsScale = 1.0f;
    public HudBackgroundType fpsBgType = HudBackgroundType.TRANSPARENT;
    public int fpsBgColor = 0x80000000;
    public int fpsOutlineColor = 0xFF000000;
    public float fpsBgCornerRadius = 0.0f;

    // Gameplay Settings
    public boolean noFog = false;
    public boolean noFogLava = true;
    public boolean noFogWater = true;
    public boolean noFogPowderSnow = true;
    public boolean noFogTerrain = true;
    public boolean noFogDimension = true;

    public boolean fullbright = false;

    // Hitboxes
    public boolean hitboxes = false;
    public float hitboxThickness = 2.0f;
    public int hitboxColorMonster = 0xFFFF0000;
    public int hitboxColorAnimal = 0xFF00FF00;
    public int hitboxColorPlayer = 0xFFFFFFFF;
    public int hitboxColorOther = 0xFFFFFFFF;
    public boolean hitboxLookVector = true;


    // Time Changer
    public boolean timeChanger = false;
    public int customTime = 6000; // 0-24000

    // Custom Crosshair
    public boolean customCrosshair = false;
    public boolean customCrosshairVanilla = true; // If true, uses vanilla crosshair but changes color
    public int customCrosshairVanillaColor = 0xFFFFFFFF;
    public boolean customCrosshairTargetColor = false;
    public int customCrosshairTargetColorHex = 0xFFFF0000;
    public int[][] customCrosshairPixels = new int[16][16]; // The drawn pixels
    
    // Chat Settings
    public boolean chatHeads = false;
    public boolean chatHeadsBeforeName = true;
    
    public boolean stackMessages = false;
    public int maxMessageStack = 5;
    
    public boolean autoTextEnabled = false;
    public java.util.List<AutoTextEntry> autoTexts = new java.util.ArrayList<>();

    // Ping
    public boolean pingEnabled = true;
    public int pingX = 10;
    public int pingY = 90;
    public float pingScale = 1.0f;
    public int pingTextColor = 0xFFFFFFFF;
    public HudBackgroundType pingBgType = HudBackgroundType.VANILLA;
    public int pingBgColor = 0x80000000;
    public int pingOutlineColor = 0xFFFFFFFF;
    public float pingCornerRadius = 0.0f;

    // Keystrokes
    public boolean keystrokes = true;
    public int keystrokesX = 10;
    public int keystrokesY = 110;
    public float keystrokesScale = 1.0f;
    public HudBackgroundType keystrokesBgType = HudBackgroundType.VANILLA;
    public int keystrokesBgColor = 0x80000000;
    public int keystrokesOutlineColor = 0xFFFFFFFF;
    public float keystrokesCornerRadius = 0.0f;
    public java.util.Map<String, int[]> keystrokesLayout = new java.util.HashMap<>();

    // Keyboard
    public boolean keyboardEnabled = false;
    public int keyboardX = 10;
    public int keyboardY = 170;
    public float keyboardScale = 1.0f;
    public HudBackgroundType keyboardBgType = HudBackgroundType.VANILLA;
    public int keyboardBgColor = 0x80000000;
    public int keyboardOutlineColor = 0xFFFFFFFF;
    public float keyboardCornerRadius = 0.0f;
    public java.util.Map<String, int[]> fullKeyboardLayout = new java.util.HashMap<>();

    public static CaeserConfig INSTANCE = new CaeserConfig();

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (java.io.FileReader reader = new java.io.FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, CaeserConfig.class);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (java.io.FileWriter writer = new java.io.FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}


