package com.caeser.mod.emote;

import java.util.HashMap;
import java.util.Map;

public class Emote {
    public String name;
    public float length; // in seconds
    public boolean loop;
    public Map<String, BoneAnimation> bones = new HashMap<>();

    public Emote(String name, float length, boolean loop) {
        this.name = name;
        this.length = length;
        this.loop = loop;
    }
}
