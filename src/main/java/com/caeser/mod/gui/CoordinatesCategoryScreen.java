package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserToggleWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CoordinatesCategoryScreen extends HudBackgroundCategoryScreen {

    public CoordinatesCategoryScreen(Screen parent, com.caeser.mod.gui.hud.IHudModule module) {
        super(parent, Text.literal("Coordinates"), module,
            () -> CaeserConfig.INSTANCE.coordsBgType, val -> CaeserConfig.INSTANCE.coordsBgType = val,
            () -> CaeserConfig.INSTANCE.coordsBgColor, val -> CaeserConfig.INSTANCE.coordsBgColor = val,
            () -> CaeserConfig.INSTANCE.coordsOutlineColor, val -> CaeserConfig.INSTANCE.coordsOutlineColor = val,
            () -> CaeserConfig.INSTANCE.coordsBgCornerRadius, val -> CaeserConfig.INSTANCE.coordsBgCornerRadius = val);
    }

    @Override
    protected void addCustomWidgets(int x) {
        this.addDrawableChild(new CaeserToggleWidget(x, currentY, 200, 20, Text.literal("Show X"),
            () -> CaeserConfig.INSTANCE.coordsShowX,
            val -> { CaeserConfig.INSTANCE.coordsShowX = val; CaeserConfig.save(); }, null));
        currentY += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, currentY, 200, 20, Text.literal("Show Y"),
            () -> CaeserConfig.INSTANCE.coordsShowY,
            val -> { CaeserConfig.INSTANCE.coordsShowY = val; CaeserConfig.save(); }, null));
        currentY += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, currentY, 200, 20, Text.literal("Show Z"),
            () -> CaeserConfig.INSTANCE.coordsShowZ,
            val -> { CaeserConfig.INSTANCE.coordsShowZ = val; CaeserConfig.save(); }, null));
        currentY += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, currentY, 200, 20, Text.literal("Show Biome"),
            () -> CaeserConfig.INSTANCE.coordsShowBiome,
            val -> { CaeserConfig.INSTANCE.coordsShowBiome = val; CaeserConfig.save(); }, null));
        currentY += 24;

        this.addDrawableChild(new CaeserToggleWidget(x, currentY, 200, 20, Text.literal("Show Direction"),
            () -> CaeserConfig.INSTANCE.coordsShowDirection,
            val -> { CaeserConfig.INSTANCE.coordsShowDirection = val; CaeserConfig.save(); }, null));
        currentY += 24;
    }
}
