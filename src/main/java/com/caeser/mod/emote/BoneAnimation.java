package com.caeser.mod.emote;

import java.util.ArrayList;
import java.util.List;

public class BoneAnimation {
    public String boneName;
    public List<Keyframe> rotationKeyframes = new ArrayList<>();
    public List<Keyframe> positionKeyframes = new ArrayList<>();

    public BoneAnimation(String boneName) {
        this.boneName = boneName;
    }
}
