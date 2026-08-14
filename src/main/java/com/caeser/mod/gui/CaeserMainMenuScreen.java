package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.hud.HudManager;
import com.caeser.mod.gui.hud.IHudModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CaeserMainMenuScreen extends Screen {
    private final Screen parent;
    private IHudModule draggingModule = null;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    
    private IHudModule scalingModule = null;
    private int scaleStartX = 0;
    private float initialScale = 1.0f;

    public CaeserMainMenuScreen(Screen parent) {
        super(Text.literal("Caeser Main Menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        // Centered "Mod Menu" button
        int buttonWidth = 200;
        int buttonHeight = 20;
        int x = this.width / 2 - buttonWidth / 2;
        int y = this.height / 2 - buttonHeight / 2;
        
        this.addDrawableChild(new net.minecraft.client.gui.widget.ClickableWidget(x, y, buttonWidth, buttonHeight, Text.literal("Caeser Mod Menu")) {
            @Override
            public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                boolean hovered = this.isHovered();
                int bgColor = hovered ? 0xFF2A2E3D : 0xFF1E212A;
                
                context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);
                
                // Border
                context.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, 0xFF363B4F); // Top
                context.fill(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, 0xFF363B4F); // Bottom
                context.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, 0xFF363B4F); // Left
                context.fill(this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF363B4F); // Right
                
                // Text
                int textWidth = client.textRenderer.getWidth(this.getMessage());
                context.drawTextWithShadow(client.textRenderer, this.getMessage(), this.getX() + (this.width - textWidth) / 2, this.getY() + (this.height - 8) / 2, 0xFFFFFFFF);
            }
            
            @Override
            protected void appendClickableNarrations(net.minecraft.client.gui.screen.narration.NarrationMessageBuilder builder) {}
            
            public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (this.isHovered()) {
                    client.setScreen(new CaeserSettingsScreen(CaeserMainMenuScreen.this));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Blur background and render darkening
        super.renderBackground(context, mouseX, mouseY, delta);
        for (IHudModule module : HudManager.INSTANCE.getModules()) {
            if (module.isEnabled()) {
                module.render(context, delta);
                
                // Draw bounding box
                int mx = module.getX();
                int my = module.getY();
                int mw = (int)(module.getWidth() * module.getScale());
                int mh = (int)(module.getHeight() * module.getScale());
                
                boolean hovered = mouseX >= mx && mouseX <= mx + mw && mouseY >= my && mouseY <= my + mh;
                boolean isDragging = draggingModule == module;
                
                if (hovered || isDragging) {
                    int color = 0x803B82F6;
                    context.fill(mx, my, mx + mw, my + mh, 0x40000000); // Dark translucent bg
                    context.fill(mx, my, mx + mw, my + 1, color); // Top
                    context.fill(mx, my + mh - 1, mx + mw, my + mh, color); // Bottom
                    context.fill(mx, my, mx + 1, my + mh, color); // Left
                    context.fill(mx + mw - 1, my, mx + mw, my + mh, color); // Right
                }
                
                if (hovered) {
                    // Draw close button (red '-')
                    int closeX = mx;
                    int closeY = my;
                    boolean closeHovered = mouseX >= closeX && mouseX <= closeX + 10 && mouseY >= closeY && mouseY <= closeY + 10;
                    context.fill(closeX, closeY, closeX + 10, closeY + 10, closeHovered ? 0xFFFF0000 : 0x80FF0000);
                    context.drawTextWithShadow(this.textRenderer, "-", closeX + 3, closeY + 1, 0xFFFFFFFF);
                    
                    // Draw resize handle
                    int resizeX = mx + mw - 6;
                    int resizeY = my + mh - 6;
                    boolean resizeHovered = mouseX >= resizeX && mouseX <= mx + mw && mouseY >= resizeY && mouseY <= my + mh;
                    context.fill(resizeX, resizeY, mx + mw, my + mh, resizeHovered ? 0xFF3B82F6 : 0x803B82F6);
                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (button == 0) {
            // Check if clicking a HUD module
            for (IHudModule module : HudManager.INSTANCE.getModules()) {
                if (module.isEnabled()) {
                    int mx = module.getX();
                    int my = module.getY();
                    int mw = (int)(module.getWidth() * module.getScale());
                    int mh = (int)(module.getHeight() * module.getScale());

                    if (mouseX >= mx && mouseX <= mx + mw && mouseY >= my && mouseY <= my + mh) {
                        
                        // Check close button (top left)
                        if (mouseX >= mx && mouseX <= mx + 10 && mouseY >= my && mouseY <= my + 10) {
                            module.setEnabled(false);
                            CaeserConfig.save();
                            return true;
                        }
                        
                        // Check resize handle
                        if (mouseX >= mx + mw - 6 && mouseY >= my + mh - 6) {
                            scalingModule = module;
                            scaleStartX = (int)mouseX;
                            initialScale = module.getScale();
                            return true;
                        }
                        
                        // Otherwise drag
                        draggingModule = module;
                        dragOffsetX = (int)mouseX - mx;
                        dragOffsetY = (int)mouseY - my;
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(click, bl);
    }

    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (button == 0) {
            draggingModule = null;
            scalingModule = null;
        }
        return super.mouseReleased(click);
    }

    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (scalingModule != null) {
            float deltaScale = (float)(mouseX - scaleStartX) / 100.0f;
            float newScale = Math.max(0.5f, Math.min(3.0f, initialScale + deltaScale));
            scalingModule.setScale(newScale);
            return true;
        }
        if (draggingModule != null) {
            draggingModule.setX((int)mouseX - dragOffsetX);
            draggingModule.setY((int)mouseY - dragOffsetY);
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public void close() {
        CaeserConfig.save();
        this.client.setScreen(parent);
    }
}
