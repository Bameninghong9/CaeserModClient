package com.caeser.mod.gui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

public class CaeserSliderWidget extends SliderWidget {
    private final Consumer<Double> onValueChanged;
    private final String prefix;
    private final double min;
    private final double max;

    public CaeserSliderWidget(int x, int y, int width, int height, Text text, double value, double min, double max, Consumer<Double> onValueChanged) {
        super(x, y, width, height, text, (value - min) / (max - min));
        this.prefix = text.getString();
        this.min = min;
        this.max = max;
        this.onValueChanged = onValueChanged;
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        double val = min + this.value * (max - min);
        this.setMessage(Text.literal(prefix + ": " + String.format("%.2f", val)));
    }

    @Override
    protected void applyValue() {
        if (this.onValueChanged != null) {
            double val = min + this.value * (max - min);
            this.onValueChanged.accept(val);
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw glassy background track
        context.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 0xB20A0F1D);
        int color = 0xFF1E293B;
        int x = this.getX(), y = this.getY(), w = this.getWidth(), h = this.getHeight();
        context.fill(x, y, x + w, y + 1, color);
        context.fill(x, y + h - 1, x + w, y + h, color);
        context.fill(x, y, x + 1, y + h, color);
        context.fill(x + w - 1, y, x + w, y + h, color);

        // Draw slider handle
        int handleWidth = 8;
        int handleX = this.getX() + (int)(this.value * (double)(this.getWidth() - handleWidth));
        
        boolean hovered = mouseX >= handleX && mouseX <= handleX + handleWidth && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight();
        int handleColor = hovered || this.sliderFocused ? 0xFF60A5FA : 0xFF3B82F6;

        context.fill(handleX, this.getY() + 1, handleX + handleWidth, this.getY() + this.getHeight() - 1, handleColor);
        
        // Draw text
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(this.getMessage());
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.getMessage(), this.getX() + (this.getWidth() - textWidth) / 2, this.getY() + (this.getHeight() - 8) / 2, 0xFFFFFFFF);
    }
}
