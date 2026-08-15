package com.caeser.mod.emote;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class EmoteParser {

    public static Emote parse(InputStream is) {
        try {
            JsonObject root = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            if (!root.has("animations")) return null;
            
            JsonObject animations = root.getAsJsonObject("animations");
            if (animations.size() == 0) return null;
            
            String animName = animations.keySet().iterator().next();
            JsonObject animObj = animations.getAsJsonObject(animName);
            
            float length = animObj.has("animation_length") ? animObj.get("animation_length").getAsFloat() : 1.0f;
            boolean loop = animObj.has("loop") && animObj.get("loop").getAsBoolean();
            
            Emote emote = new Emote(animName, length, loop);
            
            if (animObj.has("bones")) {
                JsonObject bonesObj = animObj.getAsJsonObject("bones");
                for (Map.Entry<String, JsonElement> entry : bonesObj.entrySet()) {
                    String boneName = entry.getKey();
                    if (boneName.equals("bipedHead")) boneName = "head";
                    else if (boneName.equals("bipedBody")) boneName = "body";
                    else if (boneName.equals("bipedRightArm")) boneName = "right_arm";
                    else if (boneName.equals("bipedLeftArm")) boneName = "left_arm";
                    else if (boneName.equals("bipedRightLeg")) boneName = "right_leg";
                    else if (boneName.equals("bipedLeftLeg")) boneName = "left_leg";
                    
                    JsonObject boneObj = entry.getValue().getAsJsonObject();
                    
                    BoneAnimation boneAnim = new BoneAnimation(boneName);
                    
                    if (boneObj.has("rotation")) {
                        parseTransform(boneObj.get("rotation"), boneAnim.rotationKeyframes);
                    }
                    if (boneObj.has("position")) {
                        parseTransform(boneObj.get("position"), boneAnim.positionKeyframes);
                    }
                    
                    emote.bones.put(boneName, boneAnim);
                }
            }
            return emote;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void parseTransform(JsonElement transformEl, java.util.List<Keyframe> keyframes) {
        if (transformEl.isJsonObject()) {
            JsonObject obj = transformEl.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                float time = Float.parseFloat(entry.getKey());
                JsonElement val = entry.getValue();
                
                if (val.isJsonArray()) {
                    com.google.gson.JsonArray arr = val.getAsJsonArray();
                    float x = arr.get(0).getAsFloat();
                    float y = arr.get(1).getAsFloat();
                    float z = arr.get(2).getAsFloat();
                    keyframes.add(new Keyframe(time, x, y, z));
                } else if (val.isJsonObject()) {
                    JsonObject valObj = val.getAsJsonObject();
                    if (valObj.has("post")) {
                        JsonElement post = valObj.get("post");
                        if (post.isJsonArray()) {
                            com.google.gson.JsonArray arr = post.getAsJsonArray();
                            keyframes.add(new Keyframe(time, arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat()));
                        } else if (post.isJsonObject() && post.getAsJsonObject().has("vector")) {
                            com.google.gson.JsonArray arr = post.getAsJsonObject().getAsJsonArray("vector");
                            keyframes.add(new Keyframe(time, arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat()));
                        }
                    } else if (valObj.has("vector")) {
                        com.google.gson.JsonArray arr = valObj.getAsJsonArray("vector");
                        keyframes.add(new Keyframe(time, arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat()));
                    }
                }
            }
        } else if (transformEl.isJsonArray()) {
            // single keyframe at 0.0
            com.google.gson.JsonArray arr = transformEl.getAsJsonArray();
            float x = arr.get(0).getAsFloat();
            float y = arr.get(1).getAsFloat();
            float z = arr.get(2).getAsFloat();
            keyframes.add(new Keyframe(0.0f, x, y, z));
        }
        
        // sort by time
        keyframes.sort((a, b) -> Float.compare(a.time, b.time));
    }
}
