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

public class CaeserToggleWidget extends ClickableWidget {
    private final Supplier<Boolean> getter;
    private final Consumer<Boolean> setter;
    private final Runnable onRightClick;

    public CaeserToggleWidget(int x, int y, int width, int height, Text message, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        this(x, y, width, height, message, getter, setter, null);
    }

    public CaeserToggleWidget(int x, int y, int width, int height, Text message, Supplier<Boolean> getter, Consumer<Boolean> setter, Runnable onRightClick) {
        super(x, y, width, height, message);
        this.getter = getter;
        this.setter = setter;
        this.onRightClick = onRightClick;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean hovered = this.isHovered();
        boolean active = this.getter.get();

        // Colors based on the requested tailwind theme
        int bgColor = hovered ? 0xFF1E293B : 0xFF0F172A;
        int accentColor = active ? 0xFF3B82F6 : 0xFFEF4444; // Accent if ON, Danger if OFF

        // Draw flat background (simulating rounded corners by just drawing a rect for now)
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);

        // Draw the toggle "switch" block
        int switchWidth = 14;
        int switchHeight = this.height - 4;
        int switchX = active ? (this.getX() + this.width - switchWidth - 2) : (this.getX() + this.width - switchWidth * 2 - 2);
        
        // Background for the switch area
        context.fill(this.getX() + this.width - switchWidth * 2 - 4, this.getY() + 2, this.getX() + this.width - 2, this.getY() + this.height - 2, 0xFF030712);
        // Switch handle
        context.fill(switchX, this.getY() + 2, switchX + switchWidth, this.getY() + 2 + switchHeight, accentColor);

        // Draw text
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textY = this.getY() + (this.height - textRenderer.fontHeight) / 2 + 1;
        context.drawTextWithShadow(textRenderer, this.getMessage(), this.getX() + 6, textY, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        if (!this.active || !this.visible || !this.isHovered()) {
            return false;
        }
        
        if (click.button() == 1) { // Right click
            if (this.onRightClick != null) {
                this.playDownSound(MinecraftClient.getInstance().getSoundManager());
                this.onRightClick.run();
                return true;
            }
        } else if (click.button() == 0) { // Left click
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.onClick(click, bl);
            return true;
        }
        
        return false;
    }

    @Override
    public void onClick(net.minecraft.client.gui.Click click, boolean bl) {
        this.setter.accept(!this.getter.get());
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }
}
