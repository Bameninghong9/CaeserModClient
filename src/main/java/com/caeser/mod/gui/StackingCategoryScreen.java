package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class StackingCategoryScreen extends CaeserModalScreen {
    private TextFieldWidget stackField;

    public StackingCategoryScreen(Screen parent) {
        super(parent, Text.literal("Stack Messages Settings"));
        
    }

    @Override
    protected void initModal() {
        

        
        
        
        

        this.stackField = new TextFieldWidget(this.textRenderer, startX + 20, startY + 40, panelWidth - 40, 20, Text.literal("Max Stack"));
        this.stackField.setText(String.valueOf(CaeserConfig.INSTANCE.maxMessageStack));
        this.stackField.setChangedListener(text -> {
            try {
                int val = Integer.parseInt(text);
                if (val > 0) {
                    CaeserConfig.INSTANCE.maxMessageStack = val;
                    CaeserConfig.save();
                }
            } catch (NumberFormatException ignored) {}
        });
        this.addDrawableChild(this.stackField);

        this.addDrawableChild(CaeserButtonWidget.builder(Text.literal("Back"), () -> {
            this.client.setScreen(this.parent);
        }).dimensions(startX + 20, startY + 75, panelWidth - 40, 20).build());
    }

    }
