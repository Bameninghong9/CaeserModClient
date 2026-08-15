package com.caeser.mod.emote;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import net.minecraft.resource.Resource;
import java.util.Optional;

public class EmoteManager {
    public static final EmoteManager INSTANCE = new EmoteManager();
    
    private Map<String, Emote> emotes = new HashMap<>();
    
    private Emote currentEmote = null;
    private float currentEmoteTime = 0; // in seconds
    private boolean isPlaying = false;
    
    public void loadEmote(Identifier id) {
        try {
            Optional<Resource> resource = MinecraftClient.getInstance().getResourceManager().getResource(id);
            if (resource.isPresent()) {
                InputStream is = resource.get().getInputStream();
                Emote emote = EmoteParser.parse(is);
                if (emote != null) {
                    emotes.put(id.getPath(), emote);
                    System.out.println("Loaded emote: " + id.getPath() + " (" + emote.name + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Emote getEmote(String path) {
        if (!emotes.containsKey(path)) loadEmote(net.minecraft.util.Identifier.of("caeserclient", path));
        return emotes.get(path);
    }

    public void playEmote(String path) {
        if (!emotes.containsKey(path)) {
            loadEmote(net.minecraft.util.Identifier.of("caeserclient", path));
        }
        if (emotes.containsKey(path)) {
            currentEmote = emotes.get(path);
            currentEmoteTime = 0;
            isPlaying = true;
            if (path.contains("sit")) GamingChairSpawner.spawn(MinecraftClient.getInstance());
        }
    }
    
    public void stopEmote() {
        isPlaying = false;
        currentEmote = null;
        GamingChairSpawner.remove(MinecraftClient.getInstance());
    }
    
    public void tick() {
        if (isPlaying && currentEmote != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {
                if (client.options.sneakKey.isPressed() && client.options.forwardKey.isPressed()) {
                    stopEmote();
                    return;
                }
            }
            
            com.caeser.mod.emote.GamingChairSpawner.tick(client);
            
            // 20 ticks per second -> 0.05 seconds per tick
            currentEmoteTime += 0.05f;
            if (currentEmoteTime >= currentEmote.length) {
                if (currentEmote.loop) {
                    currentEmoteTime = currentEmoteTime % currentEmote.length;
                } else {
                    stopEmote();
                }
            }
        }
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
    
    public float[] getInterpolatedRotation(String boneName, float partialTicks) {
        if (!isPlaying || currentEmote == null) return null;
        BoneAnimation boneAnim = currentEmote.bones.get(boneName);
        if (boneAnim == null || boneAnim.rotationKeyframes.isEmpty()) return null;
        
        float time = currentEmoteTime + (partialTicks * 0.05f); // approximate rendering time
        if (currentEmote.loop) time = time % currentEmote.length;
        
        return interpolate(boneAnim.rotationKeyframes, time);
    }
    
    public float[] getInterpolatedPosition(String boneName, float partialTicks) {
        if (!isPlaying || currentEmote == null) return null;
        BoneAnimation boneAnim = currentEmote.bones.get(boneName);
        if (boneAnim == null || boneAnim.positionKeyframes.isEmpty()) return null;
        
        float time = currentEmoteTime + (partialTicks * 0.05f);
        if (currentEmote.loop) time = time % currentEmote.length;
        
        return interpolate(boneAnim.positionKeyframes, time);
    }
    
    private float[] interpolate(java.util.List<Keyframe> frames, float time) {
        if (frames.size() == 1) {
            Keyframe f = frames.get(0);
            return new float[]{f.x, f.y, f.z};
        }
        
        Keyframe prev = frames.get(0);
        Keyframe next = frames.get(frames.size() - 1);
        
        for (int i = 0; i < frames.size() - 1; i++) {
            if (time >= frames.get(i).time && time <= frames.get(i+1).time) {
                prev = frames.get(i);
                next = frames.get(i+1);
                break;
            }
        }
        
        if (time <= prev.time) return new float[]{prev.x, prev.y, prev.z};
        if (time >= next.time) return new float[]{next.x, next.y, next.z};
        
        float diff = next.time - prev.time;
        float progress = (time - prev.time) / diff;
        
        float x = prev.x + (next.x - prev.x) * progress;
        float y = prev.y + (next.y - prev.y) * progress;
        float z = prev.z + (next.z - prev.z) * progress;
        
        return new float[]{x, y, z};
    }
}
