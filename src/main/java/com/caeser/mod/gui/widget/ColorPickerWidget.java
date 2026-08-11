package com.caeser.mod.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.awt.Color;
import java.util.function.Consumer;

public class ColorPickerWidget extends ClickableWidget {
    private float hue = 0.0f;
    private float saturation = 1.0f;
    private float brightness = 1.0f;
    private float alpha = 1.0f;

    private final Consumer<Integer> onColorChange;
    
    private final TextFieldWidget hexField;

    private boolean draggingSV = false;
    private boolean draggingHue = false;
    private boolean draggingAlpha = false;

    // Layout constants
    private final int previewSize = 40;
    private final int svWidth = 100;
    private final int svHeight = 40;
    private final int sliderHeight = 10;
    private final int padding = 4;

    public ColorPickerWidget(int x, int y, Text message, int initialColor, Consumer<Integer> onColorChange) {
        super(x, y, 40 + 4 + 100, 104, message);
        this.onColorChange = onColorChange;
        
        setColor(initialColor);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        this.hexField = new TextFieldWidget(textRenderer, x, y + svHeight + sliderHeight * 2 + padding * 3, this.width, 20, Text.literal("Hex"));
        this.hexField.setMaxLength(9); // #AARRGGBB
        this.hexField.setChangedListener(this::onHexChanged);
        updateHexField();
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        if (this.hexField != null) this.hexField.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        if (this.hexField != null) this.hexField.setY(y + svHeight + sliderHeight * 2 + padding * 3);
    }

    private void setColor(int color) {
        this.alpha = ((color >> 24) & 0xFF) / 255.0f;
        float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, null);
        this.hue = hsb[0];
        this.saturation = hsb[1];
        this.brightness = hsb[2];
    }

    public int getColor() {
        int rgb = Color.HSBtoRGB(hue, saturation, brightness) & 0xFFFFFF;
        int a = (int)(alpha * 255.0f);
        return (a << 24) | rgb;
    }

    private void updateHexField() {
        String hex = String.format("#%08X", getColor());
        if (!hexField.getText().equalsIgnoreCase(hex)) {
            hexField.setText(hex);
        }
    }

    private void onHexChanged(String text) {
        if (text.startsWith("#") && (text.length() == 7 || text.length() == 9)) {
            try {
                long c = Long.parseLong(text.substring(1), 16);
                if (text.length() == 7) {
                    c |= 0xFF000000L; // default full alpha if AARRGGBB not provided
                }
                setColor((int)c);
                if (onColorChange != null) {
                    onColorChange.accept(getColor());
                }
            } catch (NumberFormatException ignored) {}
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw title
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.getMessage(), this.getX(), this.getY() - 12, 0xFFFFFFFF);

        int currentY = this.getY();
        
        // Draw Preview Box Outline & Fill
        context.fill(this.getX() - 1, currentY - 1, this.getX() + previewSize + 1, currentY + previewSize + 1, 0xFFFFFFFF);
        context.fill(this.getX(), currentY, this.getX() + previewSize, currentY + previewSize, getColor());
        
        // Draw SV Box
        int svX = this.getX() + previewSize + padding;
        context.fill(svX - 1, currentY - 1, svX + svWidth + 1, currentY + svHeight + 1, 0xFFFFFFFF);
        drawSVBox(context, svX, currentY, svWidth, svHeight);

        // Draw SV Cursor
        int cursorX = svX + (int)(saturation * svWidth);
        int cursorY = currentY + (int)((1.0f - brightness) * svHeight);
        
        context.fill(cursorX - 2, cursorY - 2, cursorX + 2, cursorY - 1, 0xFFFFFFFF); // Top
        context.fill(cursorX - 2, cursorY + 1, cursorX + 2, cursorY + 2, 0xFFFFFFFF); // Bottom
        context.fill(cursorX - 2, cursorY - 1, cursorX - 1, cursorY + 1, 0xFFFFFFFF); // Left
        context.fill(cursorX + 1, cursorY - 1, cursorX + 2, cursorY + 1, 0xFFFFFFFF); // Right
        context.fill(cursorX - 1, cursorY - 1, cursorX + 1, cursorY + 1, 0xFF000000); // inner black

        currentY += svHeight + padding;

        // Draw Hue Slider
        context.fill(svX - 1, currentY - 1, svX + svWidth + 1, currentY + sliderHeight + 1, 0xFFFFFFFF);
        drawHueSlider(context, svX, currentY, svWidth, sliderHeight);
        
        // Draw Hue Cursor
        int hueCursorX = svX + (int)(hue * svWidth);
        
        context.fill(hueCursorX - 2, currentY - 1, hueCursorX + 2, currentY, 0xFFFFFFFF); // Top
        context.fill(hueCursorX - 2, currentY + sliderHeight, hueCursorX + 2, currentY + sliderHeight + 1, 0xFFFFFFFF); // Bottom
        context.fill(hueCursorX - 2, currentY, hueCursorX - 1, currentY + sliderHeight, 0xFFFFFFFF); // Left
        context.fill(hueCursorX + 1, currentY, hueCursorX + 2, currentY + sliderHeight, 0xFFFFFFFF); // Right

        currentY += sliderHeight + padding;

        // Draw Alpha Slider
        context.fill(svX - 1, currentY - 1, svX + svWidth + 1, currentY + sliderHeight + 1, 0xFFFFFFFF);
        drawAlphaSlider(context, svX, currentY, svWidth, sliderHeight);

        // Draw Alpha Cursor
        int alphaCursorX = svX + (int)(alpha * svWidth);
        context.fill(alphaCursorX - 2, currentY - 1, alphaCursorX + 2, currentY, 0xFFFFFFFF); // Top
        context.fill(alphaCursorX - 2, currentY + sliderHeight, alphaCursorX + 2, currentY + sliderHeight + 1, 0xFFFFFFFF); // Bottom
        context.fill(alphaCursorX - 2, currentY, alphaCursorX - 1, currentY + sliderHeight, 0xFFFFFFFF); // Left
        context.fill(alphaCursorX + 1, currentY, alphaCursorX + 2, currentY + sliderHeight, 0xFFFFFFFF); // Right

        // Render hex field
        this.hexField.render(context, mouseX, mouseY, delta);
    }

    private void drawSVBox(DrawContext context, int x, int y, int width, int height) {
        int baseColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        int baseR = (baseColor >> 16) & 0xFF;
        int baseG = (baseColor >> 8) & 0xFF;
        int baseB = baseColor & 0xFF;

        for (int i = 0; i < width; i++) {
            float f = (float) i / width;
            int r = (int) (255 + (baseR - 255) * f);
            int g = (int) (255 + (baseG - 255) * f);
            int b = (int) (255 + (baseB - 255) * f);
            int topColor = 0xFF000000 | (r << 16) | (g << 8) | b;
            
            context.fillGradient(x + i, y, x + i + 1, y + height, topColor, 0xFF000000);
        }
    }

    private void drawHueSlider(DrawContext context, int x, int y, int width, int height) {
        for (int i = 0; i < width; i++) {
            float h = (float) i / width;
            int color = Color.HSBtoRGB(h, 1.0f, 1.0f) | 0xFF000000;
            context.fill(x + i, y, x + i + 1, y + height, color);
        }
    }

    private void drawAlphaSlider(DrawContext context, int x, int y, int width, int height) {
        int rgb = Color.HSBtoRGB(hue, saturation, brightness) & 0xFFFFFF;
        for (int i = 0; i < width; i++) {
            float a = (float) i / width;
            int alphaInt = (int)(a * 255.0f);
            int color = (alphaInt << 24) | rgb;
            context.fill(x + i, y, x + i + 1, y + height, color);
        }
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        if (!this.active || !this.visible) return false;
        
        if (this.hexField.mouseClicked(click, bl)) {
            return true;
        }
        
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        
        if (button == 0) {
            int svX = this.getX() + previewSize + padding;
            int svY = this.getY();
            
            if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= svY && mouseY <= svY + svHeight) {
                draggingSV = true;
                updateColorFromMouse(mouseX, mouseY);
                return true;
            }
            
            int hueY = svY + svHeight + padding;
            if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= hueY && mouseY <= hueY + sliderHeight) {
                draggingHue = true;
                updateColorFromMouse(mouseX, mouseY);
                return true;
            }

            int alphaY = hueY + sliderHeight + padding;
            if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= alphaY && mouseY <= alphaY + sliderHeight) {
                draggingAlpha = true;
                updateColorFromMouse(mouseX, mouseY);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        if (draggingSV || draggingHue || draggingAlpha) {
            updateColorFromMouse(click.x(), click.y());
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        if (draggingSV || draggingHue || draggingAlpha) {
            draggingSV = false;
            draggingHue = false;
            draggingAlpha = false;
            updateHexField();
            return true;
        }
        return false;
    }

    private void updateColorFromMouse(double mouseX, double mouseY) {
        int svX = this.getX() + previewSize + padding;
        int svY = this.getY();
        
        if (draggingSV) {
            float s = (float)(mouseX - svX) / svWidth;
            float b = 1.0f - (float)(mouseY - svY) / svHeight;
            this.saturation = Math.max(0.0f, Math.min(1.0f, s));
            this.brightness = Math.max(0.0f, Math.min(1.0f, b));
        } else if (draggingHue) {
            float h = (float)(mouseX - svX) / svWidth;
            this.hue = Math.max(0.0f, Math.min(1.0f, h));
        } else if (draggingAlpha) {
            float a = (float)(mouseX - svX) / svWidth;
            this.alpha = Math.max(0.0f, Math.min(1.0f, a));
        }
        
        updateHexField();

        if (onColorChange != null) {
            onColorChange.accept(getColor());
        }
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }
}
