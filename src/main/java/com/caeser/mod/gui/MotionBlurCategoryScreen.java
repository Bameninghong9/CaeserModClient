package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserSliderWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MotionBlurCategoryScreen extends CaeserModalScreen {
    public MotionBlurCategoryScreen(Screen parent) {
        super(parent, Text.literal("Motion Blur Settings"));
        
    }

    @Override
    protected void initModal() {
        int x = this.startX + 60;
        int y = this.startY + 45;
        int w = 200;

        this.addDrawableChild(new CaeserSliderWidget(x, y, w, 20, Text.literal("Blur Strength"), 
            CaeserConfig.INSTANCE.motionBlurStrength, 0.0, 1.0, 
            val -> {
                CaeserConfig.INSTANCE.motionBlurStrength = val.floatValue();
                CaeserConfig.save();
            }));
    }

    }
