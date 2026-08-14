package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.ColorPickerWidget;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import com.caeser.mod.util.ICustomOverlayTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class HitColorCategoryScreen extends Screen {
    private final Screen parent;
    
    private boolean isAllExpanded = true;

    private ColorPickerWidget allColorPicker;
    
    // Layout constants
    private final int CATEGORY_HEIGHT = 20;
    private final int PICKER_HEIGHT = 104; // Derived from ColorPickerWidget
    private final int PADDING = 10;

    public HitColorCategoryScreen(Screen parent) {
        super(Text.literal("HitColor Categories"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 150;
        int y = 50;

        this.addDrawableChild(new CaeserButtonWidget(x + 8, y - 22, 40, 20, Text.literal("< Back"), () -> {
            this.client.setScreen(this.parent);
        }));

        // "All" HitColor Picker
        allColorPicker = new ColorPickerWidget(x + 10, y + CATEGORY_HEIGHT + PADDING, Text.literal("HitColor"), CaeserConfig.INSTANCE.hitColor, color -> {
            CaeserConfig.INSTANCE.hitColor = color;
            updateTextures();
        });
        
        this.addDrawableChild(allColorPicker);
        
        updateWidgetVisibility();
    }

    private void updateTextures() {
        if (MinecraftClient.getInstance().gameRenderer != null && MinecraftClient.getInstance().gameRenderer.getOverlayTexture() instanceof ICustomOverlayTexture) {
            ((ICustomOverlayTexture) MinecraftClient.getInstance().gameRenderer.getOverlayTexture()).updateCustomOverlay();
        }
    }

    private void updateWidgetVisibility() {
        allColorPicker.visible = isAllExpanded;
        allColorPicker.active = isAllExpanded;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw Glass Background
        context.fill(0, 0, this.width, this.height, 0x80030712);
        
        int x = this.width / 2 - 150;
        int y = 50;
        int w = 300;
        
        // Draw Modal Background
        int panelHeight = isAllExpanded ? CATEGORY_HEIGHT + 130 : CATEGORY_HEIGHT;
        context.fill(x, y - 30, x + w, y + panelHeight, 0xB20A0F1D);
        
        // Draw Outline
        int outlineColor = 0xFF3B82F6; // Blue outline
        context.fill(x, y - 30, x + w, y - 29, outlineColor); // Top
        context.fill(x, y + panelHeight - 1, x + w, y + panelHeight, outlineColor); // Bottom
        context.fill(x, y - 30, x + 1, y + panelHeight, outlineColor); // Left
        context.fill(x + w - 1, y - 30, x + w, y + panelHeight, outlineColor); // Right
        
        // Draw Header background and line
        context.fill(x + 1, y - 29, x + w - 1, y - 1, 0xFF0F172A);
        context.fill(x, y - 1, x + w, y, 0xFF1E293B);
        
        super.render(context, mouseX, mouseY, delta);

        // Draw Title
        context.drawTextWithShadow(this.textRenderer, this.title, this.width / 2 - this.textRenderer.getWidth(this.title) / 2, y - 20, 0xFFFFFFFF);

        // Draw "All" Category Header
        boolean allHover = mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + CATEGORY_HEIGHT;
        context.fill(x, y, x + w, y + CATEGORY_HEIGHT, allHover ? 0xB21A1F2D : 0xB20A0F1D);
        context.drawTextWithShadow(this.textRenderer, Text.literal("All"), x + 10, y + 6, 0xFFFFFFFF);
        String allIcon = isAllExpanded ? "V" : ">";
        context.drawTextWithShadow(this.textRenderer, Text.literal(allIcon), x + w - 15, y + 6, 0xFF3B82F6);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        if (click.button() == 0) {
            int x = this.width / 2 - 150;
            int y = 50;
            int w = 300;

            // "All" Header Click
            if (click.x() >= x && click.x() <= x + w && click.y() >= y && click.y() <= y + CATEGORY_HEIGHT) {
                isAllExpanded = !isAllExpanded;
                updateWidgetVisibility();
                return true;
            }
        }
        return super.mouseClicked(click, bl);
    }

    @Override
    public void close() {
        CaeserConfig.save();
        this.client.setScreen(parent);
    }
}
