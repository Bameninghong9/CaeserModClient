package com.caeser.mod.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class CaeserButtonWidget extends ClickableWidget {
    private final Runnable onPress;

    public CaeserButtonWidget(int x, int y, int width, int height, Text message, Runnable onPress) {
        super(x, y, width, height, message);
        this.onPress = onPress;
    }

    public static Builder builder(Text message, Runnable onPress) {
        return new Builder(message, onPress);
    }

    public static class Builder {
        private final Text message;
        private final Runnable onPress;
        private int x, y, width = 150, height = 20;

        public Builder(Text message, Runnable onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public Builder dimensions(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public CaeserButtonWidget build() {
            return new CaeserButtonWidget(x, y, width, height, message, onPress);
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int color = this.isHovered() ? 0xFF3B82F6 : 0xFF1E293B; // Blue when hovered, dark slate when not
        if (!this.active) {
            color = 0xFF111827; // Very dark gray if disabled
        }
        
        // Draw background
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, color);
        // Draw border (top/bottom/left/right manual lines since drawBorder signature changed)
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, 0xFF3B82F6); // top
        context.fill(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, 0xFF3B82F6); // bottom
        context.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, 0xFF3B82F6); // left
        context.fill(this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF3B82F6); // right

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int textColor = this.active ? 0xFFFFFFFF : 0xFFAAAAAA;
        int textX = this.getX() + (this.width - textRenderer.getWidth(this.getMessage())) / 2;
        int textY = this.getY() + (this.height - 8) / 2;
        
        context.drawTextWithShadow(textRenderer, this.getMessage(), textX, textY, textColor);
    }

    @Override
    public void onClick(net.minecraft.client.gui.Click click, boolean bl) {
        this.onPress.run();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }
}
