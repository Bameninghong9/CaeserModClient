package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserSliderWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class TimeCategoryScreen extends CaeserModalScreen {

    public TimeCategoryScreen(Screen parent) {
        super(parent, Text.literal("Time Changer"), () -> CaeserConfig.INSTANCE.timeChanger, val -> {
            CaeserConfig.INSTANCE.timeChanger = val;
            CaeserConfig.save();
        });
    }

    @Override
    protected void initModal() {
        int currentY = this.startY + 40;
        int contentX = this.startX + 20;
        
        this.addDrawableChild(new CaeserSliderWidget(contentX, currentY, 360, 20, Text.literal("Time"), CaeserConfig.INSTANCE.customTime, 0.0, 24000.0, val -> {
            CaeserConfig.INSTANCE.customTime = val.intValue();
            CaeserConfig.save();
        }));
    }
}
