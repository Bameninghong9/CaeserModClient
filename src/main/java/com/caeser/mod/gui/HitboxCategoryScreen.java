package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import com.caeser.mod.gui.widget.CaeserSliderWidget;
import com.caeser.mod.gui.widget.CaeserToggleWidget;
import com.caeser.mod.gui.widget.ColorBoxWidget;
import com.caeser.mod.gui.widget.ColorPickerPopup;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HitboxCategoryScreen extends CaeserModalScreen {

    public HitboxCategoryScreen(Screen parent) {
        super(parent, Text.literal("Hitboxes"), () -> CaeserConfig.INSTANCE.hitboxes, val -> {
            CaeserConfig.INSTANCE.hitboxes = val;
            CaeserConfig.save();
        });
    }

    @Override
    protected void initModal() {
        int currentY = this.startY + 40;
        int contentX = this.startX + 20;
        
        // Thickness Slider
        this.addDrawableChild(new CaeserSliderWidget(contentX, currentY, 360, 20, Text.literal("Line Thickness"), CaeserConfig.INSTANCE.hitboxThickness, 1.0f, 10.0f, val -> {
            CaeserConfig.INSTANCE.hitboxThickness = val.floatValue();
            CaeserConfig.save();
        }));
        currentY += 30;
        
        // Look Vector Toggle
        this.addDrawableChild(new CaeserToggleWidget(contentX, currentY, 360, 20, Text.literal("Show Look Vector"), () -> CaeserConfig.INSTANCE.hitboxLookVector, val -> {
            CaeserConfig.INSTANCE.hitboxLookVector = val;
            CaeserConfig.save();
        }));
        currentY += 40;
        
        // Color Boxes
        int boxSpacing = 110;
        
        this.addDrawableChild(new ColorBoxWidget(contentX, currentY, 20, CaeserConfig.INSTANCE.hitboxColorMonster, Text.literal("Monster Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                CaeserConfig.INSTANCE.hitboxColorMonster = color;
                CaeserConfig.save();
            });
        }));
        
        this.addDrawableChild(new ColorBoxWidget(contentX + boxSpacing, currentY, 20, CaeserConfig.INSTANCE.hitboxColorAnimal, Text.literal("Animal Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                CaeserConfig.INSTANCE.hitboxColorAnimal = color;
                CaeserConfig.save();
            });
        }));

        this.addDrawableChild(new ColorBoxWidget(contentX + boxSpacing * 2, currentY, 20, CaeserConfig.INSTANCE.hitboxColorPlayer, Text.literal("Player Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                CaeserConfig.INSTANCE.hitboxColorPlayer = color;
                CaeserConfig.save();
            });
        }));
    }
    
    @Override
    protected void renderModalForeground(DrawContext context, int mouseX, int mouseY, float delta) {
        int contentX = this.startX + 20;
        int currentY = this.startY + 110;
        int textY = currentY + 6;
        int boxSpacing = 110;
        
        context.drawTextWithShadow(this.textRenderer, Text.literal("Monster"), contentX + 25, textY, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Animal"), contentX + boxSpacing + 25, textY, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Player"), contentX + boxSpacing * 2 + 25, textY, 0xFFFFFFFF);
    }
}
