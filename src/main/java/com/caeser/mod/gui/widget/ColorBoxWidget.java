package com.caeser.mod.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class ColorBoxWidget extends ClickableWidget {
    private int color;
    private final Consumer<ColorBoxWidget> onClick;

    public ColorBoxWidget(int x, int y, int size, int initialColor, Text message, Consumer<ColorBoxWidget> onClick) {
        super(x, y, size, size, message);
        this.color = initialColor;
        this.onClick = onClick;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw outline
        context.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 1, 0xFF334155);
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF000000); // Inner background
        
        // Draw checkerboard for alpha
        int cb1 = 0xFF888888;
        int cb2 = 0xFF444444;
        int cx = this.getX() + this.width / 2;
        int cy = this.getY() + this.height / 2;
        context.fill(this.getX(), this.getY(), cx, cy, cb1);
        context.fill(cx, this.getY(), this.getX() + this.width, cy, cb2);
        context.fill(this.getX(), cy, cx, this.getY() + this.height, cb2);
        context.fill(cx, cy, this.getX() + this.width, this.getY() + this.height, cb1);

        // Draw color
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, color);
        
        // Draw hover outline
        if (this.isMouseOver(mouseX, mouseY)) {
            context.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY(), 0xFFFFFFFF);
            context.fill(this.getX() - 1, this.getY() + this.height, this.getX() + this.width + 1, this.getY() + this.height + 1, 0xFFFFFFFF);
            context.fill(this.getX() - 1, this.getY(), this.getX(), this.getY() + this.height, 0xFFFFFFFF);
            context.fill(this.getX() + this.width, this.getY(), this.getX() + this.width + 1, this.getY() + this.height, 0xFFFFFFFF);
        }
    }

    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (this.active && this.visible && this.isMouseOver(mouseX, mouseY)) {
            if (this.onClick != null) {
                this.onClick.accept(this);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
