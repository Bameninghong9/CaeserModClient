package com.caeser.mod.gui;

import com.caeser.mod.gui.widget.CaeserButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CaeserModalScreen extends Screen {
    protected final Screen parent;
    protected int panelWidth = 320;
    protected int panelHeight = 220;
    protected int startX;
    protected int startY;
    
    protected Supplier<Boolean> toggleGetter;
    protected Consumer<Boolean> toggleSetter;

    public CaeserModalScreen(Screen parent, Text title) {
        this(parent, title, null, null);
    }
    
    public CaeserModalScreen(Screen parent, Text title, Supplier<Boolean> toggleGetter, Consumer<Boolean> toggleSetter) {
        super(title);
        this.parent = parent;
        this.toggleGetter = toggleGetter;
        this.toggleSetter = toggleSetter;
    }

    @Override
    protected void init() {
        this.startX = (this.width - this.panelWidth) / 2;
        this.startY = (this.height - this.panelHeight) / 2;

        // Back button
        this.addDrawableChild(new CaeserButtonWidget(this.startX + 8, this.startY + 8, 20, 20, Text.literal("<"), () -> {
            this.client.setScreen(this.parent);
        }));

        if (this.toggleGetter != null && this.toggleSetter != null) {
            this.addDrawableChild(new CaeserButtonWidget(this.startX + this.panelWidth - 40, this.startY + 8, 32, 20, Text.literal(this.toggleGetter.get() ? "ON" : "OFF"), () -> {
                this.toggleSetter.accept(!this.toggleGetter.get());
                this.client.setScreen(this); // refresh UI for button state
            }));
        }

        initModal();
    }
    
    protected abstract void initModal();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render modal background (Deep Slate with some alpha)
        context.fill(startX, startY, startX + panelWidth, startY + panelHeight, 0xE60F172A);
        
        // Draw Outline (Indigo 500)
        int outlineColor = 0xFF6366F1; 
        context.fill(startX, startY, startX + panelWidth, startY + 1, outlineColor); // Top
        context.fill(startX, startY + panelHeight - 1, startX + panelWidth, startY + panelHeight, outlineColor); // Bottom
        context.fill(startX, startY, startX + 1, startY + panelHeight, outlineColor); // Left
        context.fill(startX + panelWidth - 1, startY, startX + panelWidth, startY + panelHeight, outlineColor); // Right
        
        // Draw Header background and line
        context.fill(startX + 1, startY + 1, startX + panelWidth - 1, startY + 35, 0xFF1E293B);
        context.fill(startX, startY + 35, startX + panelWidth, startY + 36, 0xFF334155);

        // Draw Title
        context.drawTextWithShadow(this.textRenderer, Text.literal(this.title.getString().toUpperCase()), startX + 36, startY + 14, 0xFFF8FAFC);

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
