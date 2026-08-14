package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import net.minecraft.text.Text;

public class ChatHeadsCategoryScreen extends CaeserModalScreen {
    public ChatHeadsCategoryScreen(Screen parent) {
        super(parent, Text.literal("Chat Heads Settings"), 
              () -> CaeserConfig.INSTANCE.chatHeads, 
              val -> { CaeserConfig.INSTANCE.chatHeads = val; CaeserConfig.save(); });
        this.panelWidth = 240;
        this.panelHeight = 120;
    }

    @Override
    protected void initModal() {
        CaeserButtonWidget posBtn = CaeserButtonWidget.builder(Text.literal("Position: " + (CaeserConfig.INSTANCE.chatHeadsBeforeName ? "Before Name" : "After Name")), () -> {
            CaeserConfig.INSTANCE.chatHeadsBeforeName = !CaeserConfig.INSTANCE.chatHeadsBeforeName;
            CaeserConfig.save();
            this.client.setScreen(new ChatHeadsCategoryScreen(parent)); // Refresh button text
        }).dimensions(startX + 20, startY + 50, panelWidth - 40, 20).build();
        this.addDrawableChild(posBtn);
    }
}
