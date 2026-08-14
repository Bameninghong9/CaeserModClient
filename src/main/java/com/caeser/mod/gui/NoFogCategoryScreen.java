package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserToggleWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class NoFogCategoryScreen extends CaeserModalScreen {
    public NoFogCategoryScreen(Screen parent) {
        super(parent, Text.literal("No Fog Types"), 
              () -> CaeserConfig.INSTANCE.noFog, 
              val -> { CaeserConfig.INSTANCE.noFog = val; CaeserConfig.save(); });
    }

    @Override
    protected void initModal() {
        int x = this.startX + 60;
        int y = this.startY + 45;
        int w = 200;

        this.addDrawableChild(new CaeserToggleWidget(x, y, w, 20, Text.literal("Disable Lava Fog"),
            () -> CaeserConfig.INSTANCE.noFogLava,
            val -> { CaeserConfig.INSTANCE.noFogLava = val; CaeserConfig.save(); }, null));
        y += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, y, w, 20, Text.literal("Disable Water Fog"),
            () -> CaeserConfig.INSTANCE.noFogWater,
            val -> { CaeserConfig.INSTANCE.noFogWater = val; CaeserConfig.save(); }, null));
        y += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, y, w, 20, Text.literal("Disable Powder Snow Fog"),
            () -> CaeserConfig.INSTANCE.noFogPowderSnow,
            val -> { CaeserConfig.INSTANCE.noFogPowderSnow = val; CaeserConfig.save(); }, null));
        y += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, y, w, 20, Text.literal("Disable Terrain Fog"),
            () -> CaeserConfig.INSTANCE.noFogTerrain,
            val -> { CaeserConfig.INSTANCE.noFogTerrain = val; CaeserConfig.save(); }, null));
        y += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, y, w, 20, Text.literal("Disable Dimension Fog"),
            () -> CaeserConfig.INSTANCE.noFogDimension,
            val -> { CaeserConfig.INSTANCE.noFogDimension = val; CaeserConfig.save(); }, null));
    }
}
