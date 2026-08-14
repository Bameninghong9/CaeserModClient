package com.caeser.mod.gui;

import com.caeser.mod.gui.widget.CaeserButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class CaeserModalScreen extends Screen {
    protected final Screen parent;
    protected int panelWidth = 320;
    protected int panelHeight = 220;
    protected int startX;
    protected int startY;
    
    public CaeserModalScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.startX = (this.width - this.panelWidth) / 2;
        this.startY = (this.height - this.panelHeight) / 2;

        // Back button
        this.addDrawableChild(new CaeserButtonWidget(this.startX + 8, this.startY + 8, 20, 20, Text.literal("<"), () -> {
            this.client.setScreen(this.parent);
        }));

        initModal();
    }
    
    protected abstract void initModal();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render modal background
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

        // Draw Title
        context.drawTextWithShadow(this.textRenderer, Text.literal(this.title.getString().toUpperCase()), startX + 36, startY + 14, 0xFFFFFFFF);

        renderModalBackground(context, mouseX, mouseY, delta);
        
        super.render(context, mouseX, mouseY, delta);
        
        renderModalForeground(context, mouseX, mouseY, delta);
    }
    
    protected void renderModalBackground(DrawContext context, int mouseX, int mouseY, float delta) {}
    protected void renderModalForeground(DrawContext context, int mouseX, int mouseY, float delta) {}

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
