package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserSliderWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class LowFireCategoryScreen extends CaeserModalScreen {
    public LowFireCategoryScreen(Screen parent) {
        super(parent, Text.literal("Low Fire Settings"));
        
    }

    @Override
    protected void initModal() {
        int x = this.startX + 60;
        int y = this.height / 2 - 50;
        
        // Add a slider for low fire height
        this.addDrawableChild(new CaeserSliderWidget(
            x, y, 300, 20, 
            Text.literal("Fire Height Translation"),
            CaeserConfig.INSTANCE.lowFireHeight, 
            -1.0, 1.0, 
            val -> {
                CaeserConfig.INSTANCE.lowFireHeight = val.floatValue();
                CaeserConfig.save();
            }
        ));
    }
}
