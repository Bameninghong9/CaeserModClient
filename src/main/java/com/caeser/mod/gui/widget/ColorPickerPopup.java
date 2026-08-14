package com.caeser.mod.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.awt.Color;
import java.util.function.Consumer;

public class ColorPickerPopup extends ClickableWidget {
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
    private final int borderPadding = 8;
    
    // Popup total dimensions: width 160, height 120
    private final int popupWidth = previewSize + padding + svWidth + borderPadding * 2;
    private final int popupHeight = 20 + svHeight + sliderHeight * 2 + 20 + padding * 3 + borderPadding * 2;

    public ColorPickerPopup(int screenWidth, int screenHeight, int initialColor, Consumer<Integer> onColorChange) {
        super(0, 0, 160, 130, Text.literal("COLOR PICKER"));
        
        // Center the popup
        this.setX((screenWidth - popupWidth) / 2);
        this.setY((screenHeight - popupHeight) / 2);
        this.width = popupWidth;
        this.height = popupHeight;
        
        this.onColorChange = onColorChange;
        setColor(initialColor);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int hexY = this.getY() + borderPadding + 20 + svHeight + sliderHeight * 2 + padding * 3;
        this.hexField = new TextFieldWidget(textRenderer, this.getX() + borderPadding, hexY, popupWidth - borderPadding * 2, 20, Text.literal("Hex"));
        this.hexField.setMaxLength(9); // #AARRGGBB
        this.hexField.setChangedListener(this::onHexChanged);
        updateHexField();
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
                    c |= 0xFF000000L; // default full alpha
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
        // Draw popup background
        context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF1E293B);
        
        // Draw outline
        int outlineColor = 0xFF6366F1;
        context.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY(), outlineColor);
        context.fill(this.getX() - 1, this.getY() + this.height, this.getX() + this.width + 1, this.getY() + this.height + 1, outlineColor);
        context.fill(this.getX() - 1, this.getY(), this.getX(), this.getY() + this.height, outlineColor);
        context.fill(this.getX() + this.width, this.getY(), this.getX() + this.width + 1, this.getY() + this.height, outlineColor);

        // Draw title
        context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, this.getMessage(), this.getX() + borderPadding, this.getY() + borderPadding, 0xFFFFFFFF);

        int currentY = this.getY() + borderPadding + 20; // 20px for title
        int currentX = this.getX() + borderPadding;

        // Draw Preview Box
        context.fill(currentX - 1, currentY - 1, currentX + previewSize + 1, currentY + previewSize + 1, 0xFFFFFFFF);
        context.fill(currentX, currentY, currentX + previewSize, currentY + previewSize, 0xFF000000); // base for alpha
        context.fill(currentX, currentY, currentX + previewSize, currentY + previewSize, getColor());
        
        // Draw SV Box
        int svX = currentX + previewSize + padding;
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
        context.fill(currentX - 1, currentY - 1, currentX + popupWidth - borderPadding * 2 + 1, currentY + sliderHeight + 1, 0xFFFFFFFF);
        drawHueSlider(context, currentX, currentY, popupWidth - borderPadding * 2, sliderHeight);
        
        // Draw Hue Cursor
        int sliderFullWidth = popupWidth - borderPadding * 2;
        int hueCursorX = currentX + (int)(hue * sliderFullWidth);
        
        context.fill(hueCursorX - 2, currentY - 1, hueCursorX + 2, currentY, 0xFFFFFFFF); 
        context.fill(hueCursorX - 2, currentY + sliderHeight, hueCursorX + 2, currentY + sliderHeight + 1, 0xFFFFFFFF);
        context.fill(hueCursorX - 2, currentY, hueCursorX - 1, currentY + sliderHeight, 0xFFFFFFFF);
        context.fill(hueCursorX + 1, currentY, hueCursorX + 2, currentY + sliderHeight, 0xFFFFFFFF);

        currentY += sliderHeight + padding;

        // Draw Alpha Slider
        context.fill(currentX - 1, currentY - 1, currentX + sliderFullWidth + 1, currentY + sliderHeight + 1, 0xFFFFFFFF);
        drawAlphaSlider(context, currentX, currentY, sliderFullWidth, sliderHeight);

        // Draw Alpha Cursor
        int alphaCursorX = currentX + (int)(alpha * sliderFullWidth);
        context.fill(alphaCursorX - 2, currentY - 1, alphaCursorX + 2, currentY, 0xFFFFFFFF);
        context.fill(alphaCursorX - 2, currentY + sliderHeight, alphaCursorX + 2, currentY + sliderHeight + 1, 0xFFFFFFFF);
        context.fill(alphaCursorX - 2, currentY, alphaCursorX - 1, currentY + sliderHeight, 0xFFFFFFFF);
        context.fill(alphaCursorX + 1, currentY, alphaCursorX + 2, currentY + sliderHeight, 0xFFFFFFFF);

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

    public boolean charTyped(net.minecraft.client.input.CharInput input) {
        char chr = (char) input.codepoint();
        int modifiers = input.modifiers();
        return this.hexField.charTyped(input);
    }
    
    public boolean keyPressed(net.minecraft.client.input.KeyInput input) {
        int keyCode = input.key();
        int scanCode = input.scancode();
        int modifiers = input.modifiers();
        return this.hexField.keyPressed(input);
    }

    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (!this.active || !this.visible) return false;
        if (this.hexField.mouseClicked(click, bl)) return true;
        
        if (button == 0) {
            int currentY = this.getY() + borderPadding + 20;
            int currentX = this.getX() + borderPadding;
            
            int svX = currentX + previewSize + padding;
            int svY = currentY;
            
            if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= svY && mouseY <= svY + svHeight) {
                draggingSV = true;
                updateColorFromMouse(mouseX, mouseY);
                return true;
            }
            
            int sliderFullWidth = popupWidth - borderPadding * 2;
            int hueY = svY + svHeight + padding;
            if (mouseX >= currentX && mouseX <= currentX + sliderFullWidth && mouseY >= hueY && mouseY <= hueY + sliderHeight) {
                draggingHue = true;
                updateColorFromMouse(mouseX, mouseY);
                return true;
            }

            int alphaY = hueY + sliderHeight + padding;
            if (mouseX >= currentX && mouseX <= currentX + sliderFullWidth && mouseY >= alphaY && mouseY <= alphaY + sliderHeight) {
                draggingAlpha = true;
                updateColorFromMouse(mouseX, mouseY);
                return true;
            }
        }
        return false;
    }

    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (draggingSV || draggingHue || draggingAlpha) {
            updateColorFromMouse(mouseX, mouseY);
            return true;
        }
        return false;
    }

    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
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
        int currentY = this.getY() + borderPadding + 20;
        int currentX = this.getX() + borderPadding;
        int sliderFullWidth = popupWidth - borderPadding * 2;
        
        if (draggingSV) {
            int svX = currentX + previewSize + padding;
            int svY = currentY;
            float s = (float)(mouseX - svX) / svWidth;
            float b = 1.0f - (float)(mouseY - svY) / svHeight;
            this.saturation = Math.max(0.0f, Math.min(1.0f, s));
            this.brightness = Math.max(0.0f, Math.min(1.0f, b));
        } else if (draggingHue) {
            float h = (float)(mouseX - currentX) / sliderFullWidth;
            this.hue = Math.max(0.0f, Math.min(1.0f, h));
        } else if (draggingAlpha) {
            float a = (float)(mouseX - currentX) / sliderFullWidth;
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
