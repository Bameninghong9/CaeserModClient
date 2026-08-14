package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.ColorPickerWidget;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class HitboxCategoryScreen extends Screen {
    private final Screen parent;
    private boolean playersExpanded = false;
    private boolean mobsExpanded = false;

    private ColorPickerWidget playerBasePicker;
    private ColorPickerWidget playerHoverPicker;
    private SliderWidget playerThicknessSlider;

    private ColorPickerWidget mobBasePicker;
    private ColorPickerWidget mobHoverPicker;
    private SliderWidget mobThicknessSlider;
    private CaeserButtonWidget toggleBtn;
    private CaeserButtonWidget backBtn;

    public HitboxCategoryScreen(Screen parent) {
        super(Text.literal("Hitbox Categories"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        toggleBtn = new CaeserButtonWidget(0, 0, 40, 20, Text.literal(CaeserConfig.INSTANCE.hitboxes ? "ON" : "OFF"), () -> {
            CaeserConfig.INSTANCE.hitboxes = !CaeserConfig.INSTANCE.hitboxes;
            CaeserConfig.save();
            this.client.setScreen(new HitboxCategoryScreen(parent));
        });
        this.addDrawableChild(toggleBtn);
        
        backBtn = new CaeserButtonWidget(0, 0, 40, 20, Text.literal("< Back"), () -> {
            this.client.setScreen(this.parent);
        });
        this.addDrawableChild(backBtn);
        
        // Player Widgets
        playerBasePicker = new ColorPickerWidget(0, 0, Text.literal("Base Color"), CaeserConfig.INSTANCE.playerHitboxColor, color -> {
            CaeserConfig.INSTANCE.playerHitboxColor = color; CaeserConfig.save();
        });
        playerHoverPicker = new ColorPickerWidget(0, 0, Text.literal("Hover Color"), CaeserConfig.INSTANCE.playerHitboxHoverColor, color -> {
            CaeserConfig.INSTANCE.playerHitboxHoverColor = color; CaeserConfig.save();
        });
        playerThicknessSlider = new SliderWidget(0, 0, 300, 20, Text.literal("Thickness: " + String.format("%.1f", CaeserConfig.INSTANCE.playerHitboxThickness)), CaeserConfig.INSTANCE.playerHitboxThickness / 10.0) {
            @Override protected void updateMessage() { this.setMessage(Text.literal("Thickness: " + String.format("%.1f", CaeserConfig.INSTANCE.playerHitboxThickness))); }
            @Override protected void applyValue() {
                CaeserConfig.INSTANCE.playerHitboxThickness = (float)Math.max(0.1, this.value * 10.0);
                CaeserConfig.save();
            }
        };

        // Mob Widgets
        mobBasePicker = new ColorPickerWidget(0, 0, Text.literal("Base Color"), CaeserConfig.INSTANCE.mobHitboxColor, color -> {
            CaeserConfig.INSTANCE.mobHitboxColor = color; CaeserConfig.save();
        });
        mobHoverPicker = new ColorPickerWidget(0, 0, Text.literal("Hover Color"), CaeserConfig.INSTANCE.mobHitboxHoverColor, color -> {
            CaeserConfig.INSTANCE.mobHitboxHoverColor = color; CaeserConfig.save();
        });
        mobThicknessSlider = new SliderWidget(0, 0, 300, 20, Text.literal("Thickness: " + String.format("%.1f", CaeserConfig.INSTANCE.mobHitboxThickness)), CaeserConfig.INSTANCE.mobHitboxThickness / 10.0) {
            @Override protected void updateMessage() { this.setMessage(Text.literal("Thickness: " + String.format("%.1f", CaeserConfig.INSTANCE.mobHitboxThickness))); }
            @Override protected void applyValue() {
                CaeserConfig.INSTANCE.mobHitboxThickness = (float)Math.max(0.1, this.value * 10.0);
                CaeserConfig.save();
            }
        };

        this.addDrawableChild(playerBasePicker);
        this.addDrawableChild(playerHoverPicker);
        this.addDrawableChild(playerThicknessSlider);
        this.addDrawableChild(mobBasePicker);
        this.addDrawableChild(mobHoverPicker);
        this.addDrawableChild(mobThicknessSlider);

        updateLayout();
    }

    private void updateLayout() {
        int panelWidth = 340; // Needs to be wide enough for the two side-by-side pickers
        int baseHeight = 100;
        int expandedHeight = 160;
        int panelHeight = baseHeight + (playersExpanded ? expandedHeight : 0) + (mobsExpanded ? expandedHeight : 0);

        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        if (toggleBtn != null) {
            toggleBtn.setX(startX + panelWidth - 50);
            toggleBtn.setY(startY + 8);
        }
        if (backBtn != null) {
            backBtn.setX(startX + 8);
            backBtn.setY(startY + 8);
        }

        int currentY = startY + 40;

        // Player section
        if (playersExpanded) {
            playerBasePicker.setX(startX + 10);
            playerBasePicker.setY(currentY + 20);
            playerBasePicker.visible = true;

            playerHoverPicker.setX(startX + 170); // 160 gap
            playerHoverPicker.setY(currentY + 20);
            playerHoverPicker.visible = true;

            playerThicknessSlider.setX(startX + 20);
            playerThicknessSlider.setY(currentY + 130);
            playerThicknessSlider.visible = true;
            
            currentY += expandedHeight;
        } else {
            playerBasePicker.visible = false;
            playerHoverPicker.visible = false;
            playerThicknessSlider.visible = false;
        }

        currentY += 30; // space for Mobs > text

        // Mob section
        if (mobsExpanded) {
            mobBasePicker.setX(startX + 10);
            mobBasePicker.setY(currentY + 20);
            mobBasePicker.visible = true;

            mobHoverPicker.setX(startX + 170);
            mobHoverPicker.setY(currentY + 20);
            mobHoverPicker.visible = true;

            mobThicknessSlider.setX(startX + 20);
            mobThicknessSlider.setY(currentY + 130);
            mobThicknessSlider.visible = true;
        } else {
            mobBasePicker.visible = false;
            mobHoverPicker.visible = false;
            mobThicknessSlider.visible = false;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dark background overlay (more transparent glass bg)
        context.fill(0, 0, this.width, this.height, 0x80030712);

        int panelWidth = 340;
        int baseHeight = 100;
        int expandedHeight = 160;
        int panelHeight = baseHeight + (playersExpanded ? expandedHeight : 0) + (mobsExpanded ? expandedHeight : 0);
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        // Draw main panel background
        context.fill(startX, startY, startX + panelWidth, startY + panelHeight, 0xB20A0F1D);
        
        // Draw Outline
        int outlineColor = 0xFF3B82F6; // Blue outline
        context.fill(startX, startY, startX + panelWidth, startY + 1, outlineColor); // Top
        context.fill(startX, startY + panelHeight - 1, startX + panelWidth, startY + panelHeight, outlineColor); // Bottom
        context.fill(startX, startY, startX + 1, startY + panelHeight, outlineColor); // Left
        context.fill(startX + panelWidth - 1, startY, startX + panelWidth, startY + panelHeight, outlineColor); // Right
        
        // Draw Header background and line
        context.fill(startX + 1, startY + 1, startX + panelWidth - 1, startY + 35, 0xFF0F172A);
        context.fill(startX, startY + 35, startX + panelWidth, startY + 36, 0xFF1E293B);
        
        // Draw title
        context.drawTextWithShadow(this.textRenderer, this.title, startX + (panelWidth - this.textRenderer.getWidth(this.title)) / 2, startY + 14, 0xFFFFFFFF);

        // Draw "Players"
        int playerY = startY + 40;
        String playerText = playersExpanded ? "Players V" : "Players >";
        int playerColor = (mouseX >= startX + 20 && mouseX <= startX + panelWidth - 20 && mouseY >= playerY && mouseY <= playerY + 12) ? 0xFF3B82F6 : 0xFFFFFFFF;
        context.drawTextWithShadow(this.textRenderer, Text.literal(playerText), startX + 20, playerY, playerColor);

        // Draw "Mobs"
        int currentY = playerY + (playersExpanded ? expandedHeight : 0) + 30;
        String mobText = mobsExpanded ? "Mobs V" : "Mobs >";
        int mobColor = (mouseX >= startX + 20 && mouseX <= startX + panelWidth - 20 && mouseY >= currentY && mouseY <= currentY + 12) ? 0xFF3B82F6 : 0xFFFFFFFF;
        context.drawTextWithShadow(this.textRenderer, Text.literal(mobText), startX + 20, currentY, mobColor);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int panelWidth = 340;
        int baseHeight = 100;
        int expandedHeight = 160;
        int panelHeight = baseHeight + (playersExpanded ? expandedHeight : 0) + (mobsExpanded ? expandedHeight : 0);
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        // Players toggle
        int playerY = startY + 40;
        if (mouseX >= startX + 20 && mouseX <= startX + panelWidth - 20 && mouseY >= playerY && mouseY <= playerY + 12) {
            playersExpanded = !playersExpanded;
            updateLayout();
            return true;
        }

        // Mobs toggle
        int mobY = playerY + (playersExpanded ? expandedHeight : 0) + 30;
        if (mouseX >= startX + 20 && mouseX <= startX + panelWidth - 20 && mouseY >= mobY && mouseY <= mobY + 12) {
            mobsExpanded = !mobsExpanded;
            updateLayout();
            return true;
        }

        return super.mouseClicked(click, bl);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
