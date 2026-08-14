package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class UptimeCategoryScreen extends HudBackgroundCategoryScreen {

    public UptimeCategoryScreen(Screen parent, com.caeser.mod.gui.hud.IHudModule module) {
        super(parent, Text.literal("Uptime"), module,
            () -> CaeserConfig.INSTANCE.uptimeBgType, val -> CaeserConfig.INSTANCE.uptimeBgType = val,
            () -> CaeserConfig.INSTANCE.uptimeBgColor, val -> CaeserConfig.INSTANCE.uptimeBgColor = val,
            () -> CaeserConfig.INSTANCE.uptimeOutlineColor, val -> CaeserConfig.INSTANCE.uptimeOutlineColor = val);
    }

    @Override
    protected void addCustomWidgets(int x) {
        this.addDrawableChild(new CaeserButtonWidget(x, currentY, 200, 20, Text.literal("Format: " + CaeserConfig.INSTANCE.uptimeFormat.name()), () -> {
            CaeserConfig.INSTANCE.uptimeFormat = CaeserConfig.INSTANCE.uptimeFormat == CaeserConfig.UptimeFormat.TEXT ? CaeserConfig.UptimeFormat.DIGITAL : CaeserConfig.UptimeFormat.TEXT;
            CaeserConfig.save();
            this.client.setScreen(new UptimeCategoryScreen(parent, module));
        }));
        currentY += 24;
    }
}
