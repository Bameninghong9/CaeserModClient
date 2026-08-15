package com.caeser.mod.emote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnlockedEmotes {
    private static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "caeser_unlocked_emotes.json");
    private static final Gson GSON = new Gson();
    
    // Default emotes everyone gets
    public static List<String> unlocked = new ArrayList<>(Arrays.asList("wave", "pray"));
    
    // All possible emotes to show in GUI if unlocked
    public static final List<String> ALL_EMOTES = Arrays.asList(
        "tpose", "pray", "ballettspin",
        "wave", "ausrutschen", "highcortisol",
        "i_came_to_loop"
    );
    // Note: new_sit, storytime, chilling, holding_head, spin are REMOVED entirely as requested.

    public static void load() {
        if (FILE.exists()) {
            try (FileReader reader = new FileReader(FILE)) {
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> loaded = GSON.fromJson(reader, type);
                if (loaded != null) {
                    unlocked = loaded;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            GSON.toJson(unlocked, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isUnlocked(String emote) {
        return unlocked.contains(emote);
    }
}
