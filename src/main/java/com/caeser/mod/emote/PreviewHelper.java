package com.caeser.mod.emote;

import java.util.List;

public class PreviewHelper {
    public static String previewEmoteName = null;
    public static float previewTime = 0;
    
    public static float[] getPreviewRotation(String boneName) {
        if (previewEmoteName == null) return null;
        
        Emote emote = EmoteManager.INSTANCE.getEmote("emotes/" + previewEmoteName + ".json");
        if (emote == null) {
            System.out.println("[PreviewHelper] Emote is null for: " + previewEmoteName);
            return null;
        }
        
        BoneAnimation anim = emote.bones.get(boneName);
        if (anim == null) {
            // System.out.println("[PreviewHelper] Bone anim is null for bone: " + boneName + " in emote: " + previewEmoteName);
            return null;
        }
        if (anim.rotationKeyframes.isEmpty()) {
            System.out.println("[PreviewHelper] Rotation keyframes empty for bone: " + boneName + " in emote: " + previewEmoteName);
            return null;
        }
        
        float time = previewTime;
        if (emote.loop) time = time % emote.length;
        if (time > emote.length) time = emote.length;
        return interpolate(anim.rotationKeyframes, time);
    }
    
    public static float[] getPreviewPosition(String boneName) {
        if (previewEmoteName == null) return null;
        Emote emote = EmoteManager.INSTANCE.getEmote("emotes/" + previewEmoteName + ".json");
        if (emote == null) return null;
        BoneAnimation anim = emote.bones.get(boneName);
        if (anim == null || anim.positionKeyframes.isEmpty()) return null;
        
        float time = previewTime;
        if (emote.loop) time = time % emote.length;
        if (time > emote.length) time = emote.length;
        return interpolate(anim.positionKeyframes, time);
    }

    private static float[] interpolate(List<Keyframe> frames, float time) {
        if (frames.size() == 1) return new float[]{frames.get(0).x, frames.get(0).y, frames.get(0).z};
        
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
        
        return new float[]{
            prev.x + (next.x - prev.x) * progress,
            prev.y + (next.y - prev.y) * progress,
            prev.z + (next.z - prev.z) * progress
        };
    }
}
