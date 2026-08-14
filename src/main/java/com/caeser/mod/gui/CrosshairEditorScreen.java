package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import com.caeser.mod.gui.widget.CaeserToggleWidget;
import com.caeser.mod.gui.widget.ColorBoxWidget;
import com.caeser.mod.gui.widget.ColorPickerPopup;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CrosshairEditorScreen extends CaeserModalScreen {

    private int currentColor = 0xFFFFFFFF; // Selected color for painting
    private ColorBoxWidget paintColorBox;
    private ColorBoxWidget vanillaColorBox;
    private ColorBoxWidget targetColorBox;

    public CrosshairEditorScreen(Screen parent) {
        super(parent, Text.literal("Custom Crosshair"), () -> CaeserConfig.INSTANCE.customCrosshair, val -> {
            CaeserConfig.INSTANCE.customCrosshair = val;
            CaeserConfig.save();
        });
        this.panelWidth = 400;
        this.panelHeight = 280;
    }

    @Override
    protected void initModal() {
        int gridX = this.startX + 20;
        int rightColumnX = this.startX + 190;
        int currentY = this.startY + 40;
        
        // Vanilla Mode Toggle (Right Column)
        this.addDrawableChild(new CaeserToggleWidget(rightColumnX, currentY, 190, 20, Text.literal("Use Vanilla Crosshair"), () -> CaeserConfig.INSTANCE.customCrosshairVanilla, val -> {
            CaeserConfig.INSTANCE.customCrosshairVanilla = val;
            CaeserConfig.save();
        }));
        
        // Target Color Toggle (Right Column)
        this.addDrawableChild(new CaeserToggleWidget(rightColumnX, currentY + 30, 190, 20, Text.literal("Entity Target Color"), () -> CaeserConfig.INSTANCE.customCrosshairTargetColor, val -> {
            CaeserConfig.INSTANCE.customCrosshairTargetColor = val;
            CaeserConfig.save();
        }));
        
        // Color Boxes Row
        int boxY = currentY + 70;
        
        vanillaColorBox = new ColorBoxWidget(rightColumnX, boxY, 20, CaeserConfig.INSTANCE.customCrosshairVanillaColor, Text.literal("Vanilla Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                CaeserConfig.INSTANCE.customCrosshairVanillaColor = color;
                CaeserConfig.save();
            });
        });
        this.addDrawableChild(vanillaColorBox);
        
        targetColorBox = new ColorBoxWidget(rightColumnX + 60, boxY, 20, CaeserConfig.INSTANCE.customCrosshairTargetColorHex, Text.literal("Target Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                CaeserConfig.INSTANCE.customCrosshairTargetColorHex = color;
                CaeserConfig.save();
            });
        });
        this.addDrawableChild(targetColorBox);

        paintColorBox = new ColorBoxWidget(rightColumnX + 120, boxY, 20, currentColor, Text.literal("Paint Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                this.currentColor = color;
            });
        });
        this.addDrawableChild(paintColorBox);
        
        // Clear Button (Underneath Grid)
        this.addDrawableChild(new CaeserButtonWidget(gridX, this.startY + 200, 150, 20, Text.literal("Clear All"), () -> {
            for (int x = 0; x < 15; x++) {
                for (int y = 0; y < 15; y++) {
                    CaeserConfig.INSTANCE.customCrosshairPixels[x][y] = 0;
                }
            }
            CaeserConfig.save();
        }));
    }

    @Override
    protected void renderModalForeground(DrawContext context, int mouseX, int mouseY, float delta) {
        int rightColumnX = this.startX + 190;
        int currentY = this.startY + 40;
        int boxY = currentY + 70;
        
        context.drawTextWithShadow(this.textRenderer, Text.literal("Vanilla"), rightColumnX + 25, boxY + 6, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Target"), rightColumnX + 85, boxY + 6, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Paint"), rightColumnX + 145, boxY + 6, 0xFFFFFFFF);
    }

    @Override
    protected void renderModalBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int gridX = this.startX + 20;
        int gridY = this.startY + 40;
        
        // Draw 15x15 grid
        int cellSize = 10;
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                int px = gridX + x * cellSize;
                int py = gridY + y * cellSize;
                
                int color = CaeserConfig.INSTANCE.customCrosshairPixels[x][y];
                if (color == 0) {
                    // Draw checkerboard for transparency
                    int cb = ((x + y) % 2 == 0) ? 0xFF888888 : 0xFF444444;
                    context.fill(px, py, px + cellSize, py + cellSize, cb);
                } else {
                    context.fill(px, py, px + cellSize, py + cellSize, color);
                }
                
                // Draw grid lines
                context.fill(px, py, px + cellSize, py + 1, 0x80000000);
                context.fill(px, py, px + 1, py + cellSize, 0x80000000);
                
                // Highlight center pixel (7, 7) slightly if it's empty
                if (x == 7 && y == 7 && color == 0) {
                    context.fill(px + 4, py + 4, px + 6, py + 6, 0x55FFFFFF);
                }
            }
        }
        
        // Draw right/bottom edge of grid
        context.fill(gridX, gridY + 15 * cellSize, gridX + 15 * cellSize + 1, gridY + 15 * cellSize + 1, 0x80000000);
        context.fill(gridX + 15 * cellSize, gridY, gridX + 15 * cellSize + 1, gridY + 15 * cellSize + 1, 0x80000000);
    }
    
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (handleGridClick(mouseX, mouseY, button)) return true;
        return super.mouseClicked(click, bl);
    }
    
    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (handleGridClick(mouseX, mouseY, button)) return true;
        return super.mouseDragged(click, deltaX, deltaY);
    }
    
    private boolean handleGridClick(double mouseX, double mouseY, int button) {
        int gridX = this.startX + 20;
        int gridY = this.startY + 40;
        int cellSize = 10;
        
        if (mouseX >= gridX && mouseX < gridX + 15 * cellSize && mouseY >= gridY && mouseY < gridY + 15 * cellSize) {
            int x = (int) ((mouseX - gridX) / cellSize);
            int y = (int) ((mouseY - gridY) / cellSize);
            
            if (x >= 0 && x < 15 && y >= 0 && y < 15) {
                if (button == 0) {
                    CaeserConfig.INSTANCE.customCrosshairPixels[x][y] = currentColor;
                } else if (button == 1) {
                    CaeserConfig.INSTANCE.customCrosshairPixels[x][y] = 0;
                }
                CaeserConfig.save();
                return true;
            }
        }
        return false;
    }
}
