package com.caeser.mod.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CaeserModuleWidget extends ClickableWidget {
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;
    private final Runnable onSettingsClick;
    private final String description;
    private final net.minecraft.util.Identifier iconId;

    public CaeserModuleWidget(int x, int y, int width, int height, Text title, String description, net.minecraft.util.Identifier iconId, Supplier<Boolean> getter, Consumer<Boolean> setter, Runnable onSettingsClick) {
        super(x, y, width, height, title);
        this.getter = getter;
        this.setter = setter;
        this.description = description;
        this.iconId = iconId;
        this.onSettingsClick = onSettingsClick;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean hovered = this.isHovered();
        boolean active = this.getter.get();

        // Colors
        int bgColor = hovered ? 0xFF2A2E3D : 0xFF1E212A;
        int iconBgColor = 0xFF282C3A;
        int activeIconColor = 0xFF3B82F6; // Blue when active
        
        // Main Background
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
        
        // Inner Border
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, 0xFF363B4F); // Top
        context.fill(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, 0xFF363B4F); // Bottom
        context.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, 0xFF363B4F); // Left
        context.fill(this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF363B4F); // Right

        // Icon Box (Left)
        int iconBoxSize = this.height - 10;
        int iconX = this.getX() + 5;
        int iconY = this.getY() + 5;
        context.fill(iconX, iconY, iconX + iconBoxSize, iconY + iconBoxSize, iconBgColor);
        // Icon Border
        int iconBorderColor = active ? activeIconColor : 0xFF505565;
        context.fill(iconX, iconY, iconX + iconBoxSize, iconY + 1, iconBorderColor);
        context.fill(iconX, iconY + iconBoxSize - 1, iconX + iconBoxSize, iconY + iconBoxSize, iconBorderColor);
        context.fill(iconX, iconY, iconX + 1, iconY + iconBoxSize, iconBorderColor);
        context.fill(iconX + iconBoxSize - 1, iconY, iconX + iconBoxSize, iconY + iconBoxSize, iconBorderColor);
        
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        
        // Draw Icon Texture
        if (iconId != null) {
            context.drawTexture(net.minecraft.client.gl.RenderPipelines.GUI_TEXTURED, iconId, iconX + (iconBoxSize - 20) / 2, iconY + (iconBoxSize - 20) / 2, 0.0F, 0.0F, 20, 20, 20, 20);
        }

        // Draw Title
        context.drawTextWithShadow(textRenderer, this.getMessage(), iconX + iconBoxSize + 8, this.getY() + 8, 0xFFFFFFFF);
        
        // Draw Description (Scaled down)
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)(iconX + iconBoxSize + 8), (float)(this.getY() + 22));
        context.getMatrices().scale(0.8f, 0.8f);
        
        int maxDescWidth = (int)((this.width - iconBoxSize - 50) / 0.8f);
        java.util.List<net.minecraft.text.OrderedText> lines = textRenderer.wrapLines(Text.literal(description), maxDescWidth);
        for (int i = 0; i < Math.min(2, lines.size()); i++) {
            context.drawTextWithShadow(textRenderer, lines.get(i), 0, i * textRenderer.fontHeight + 2, 0xFFAAAAAA);
        }
        context.getMatrices().popMatrix();

        // Right side controls
        int rightMargin = this.getX() + this.width - 5;
        int switchWidth = 24;
        int switchHeight = 14;
        int switchX = rightMargin - 35; // Fixed position for all toggles
        int switchY = this.getY() + (this.height - switchHeight) / 2;
        
        // Settings Button (...)
        if (this.onSettingsClick != null) {
            int dotWidth = textRenderer.getWidth("...");
            int dotColor = (hovered && mouseX >= rightMargin - 15 && mouseX <= rightMargin) ? 0xFFFFFFFF : 0xFFAAAAAA;
            context.drawTextWithShadow(textRenderer, "...", rightMargin - dotWidth - 5, this.getY() + (this.height - textRenderer.fontHeight) / 2, dotColor);
        }
        
        // Toggle Switch Background
        context.fill(switchX, switchY, switchX + switchWidth, switchY + switchHeight, 0xFF050505);
        
        // Switch Handle
        int handleWidth = 10;
        int handleX = active ? (switchX + switchWidth - handleWidth - 2) : (switchX + 2);
        int handleColor = active ? 0xFF3B82F6 : 0xFF2A2E3D;
        context.fill(handleX, switchY + 2, handleX + handleWidth, switchY + switchHeight - 2, handleColor);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        if (!this.active || !this.visible || !this.isHovered()) {
            return false;
        }
        
        double mouseX = click.x();
        double mouseY = click.y();
        
        int rightMargin = this.getX() + this.width - 5;
        int switchWidth = 24;
        int switchX = rightMargin - 35;
            
        if (click.button() == 1) { // Right click
            if (this.onSettingsClick != null) {
                this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                this.onSettingsClick.run();
                return true;
            }
        } else if (click.button() == 0) { // Left click
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            
            // Check if switch was clicked
            if (mouseX >= switchX && mouseX <= switchX + switchWidth) {
                this.setter.accept(!this.getter.get());
                return true;
            }
            
            // If they clicked the `...` exactly (if present)
            if (this.onSettingsClick != null && mouseX >= rightMargin - 20) {
                this.onSettingsClick.run();
                return true;
            }
            
            // If they clicked the main body of the button
            if (this.onSettingsClick != null) {
                this.onSettingsClick.run();
                return true;
            } else {
                // Fallback to toggle if no settings menu
                this.setter.accept(!this.getter.get());
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onClick(net.minecraft.client.gui.Click click, boolean bl) {}

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }
}
