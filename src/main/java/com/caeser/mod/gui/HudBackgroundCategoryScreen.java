package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.hud.HudBackgroundType;
import com.caeser.mod.gui.hud.IHudModule;
import com.caeser.mod.gui.widget.CaeserButtonWidget;
import com.caeser.mod.gui.widget.ColorBoxWidget;
import com.caeser.mod.gui.widget.ColorPickerPopup;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.function.Supplier;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;

public class HudBackgroundCategoryScreen extends CaeserModalScreen {
    protected final IHudModule module;
    
    private final Supplier<HudBackgroundType> bgTypeGetter;
    private final Consumer<HudBackgroundType> bgTypeSetter;
    
    private final Supplier<Integer> bgColorGetter;
    private final Consumer<Integer> bgColorSetter;
    
    private final Supplier<Integer> outlineColorGetter;
    private final Consumer<Integer> outlineColorSetter;

    private final Supplier<Float> cornerRadiusGetter;
    private final Consumer<Float> cornerRadiusSetter;

    private ColorBoxWidget bgColorBox;
    private ColorBoxWidget outlineColorBox;
    private com.caeser.mod.gui.widget.CaeserSliderWidget cornerRadiusSlider;
    
    protected int currentY = 50;
    
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public HudBackgroundCategoryScreen(Screen parent, Text title, IHudModule module,
            Supplier<HudBackgroundType> bgTypeGetter, Consumer<HudBackgroundType> bgTypeSetter,
            Supplier<Integer> bgColorGetter, Consumer<Integer> bgColorSetter,
            Supplier<Integer> outlineColorGetter, Consumer<Integer> outlineColorSetter,
            Supplier<Float> cornerRadiusGetter, Consumer<Float> cornerRadiusSetter) {
        super(parent, title, module::isEnabled, val -> {
            module.setEnabled(val);
            CaeserConfig.save();
        });
        this.module = module;
        this.bgTypeGetter = bgTypeGetter;
        this.bgTypeSetter = bgTypeSetter;
        this.bgColorGetter = bgColorGetter;
        this.bgColorSetter = bgColorSetter;
        this.outlineColorGetter = outlineColorGetter;
        this.outlineColorSetter = outlineColorSetter;
        this.cornerRadiusGetter = cornerRadiusGetter;
        this.cornerRadiusSetter = cornerRadiusSetter;
    }

    @Override
    protected void initModal() {
        this.currentY = this.startY + 40;
        int contentX = this.startX + 20;
        
        addCustomWidgets(contentX);
        
        // Background Type Button
        this.addDrawableChild(new CaeserButtonWidget(contentX, currentY, 360, 20, Text.literal("Background: " + bgTypeGetter.get().name()), () -> {
            HudBackgroundType next = HudBackgroundType.values()[(bgTypeGetter.get().ordinal() + 1) % HudBackgroundType.values().length];
            bgTypeSetter.accept(next);
            CaeserConfig.save();
            this.client.setScreen(new HudBackgroundCategoryScreen(parent, title, module, bgTypeGetter, bgTypeSetter, bgColorGetter, bgColorSetter, outlineColorGetter, outlineColorSetter, cornerRadiusGetter, cornerRadiusSetter));
        }));
        currentY += 30;
        
        // Corner Radius Slider
        cornerRadiusSlider = new com.caeser.mod.gui.widget.CaeserSliderWidget(contentX, currentY, 360, 20, Text.literal("Corner Radius"), cornerRadiusGetter.get(), 0.0f, 20.0f, val -> {
            cornerRadiusSetter.accept(val.floatValue());
        });
        this.addDrawableChild(cornerRadiusSlider);
        currentY += 30;
        
        // Background Color Picker
        bgColorBox = new ColorBoxWidget(contentX, currentY, 20, bgColorGetter.get(), Text.literal("Background Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                bgColorSetter.accept(color);
            });
        });
        this.addDrawableChild(bgColorBox);
        
        // Outline Color Picker
        outlineColorBox = new ColorBoxWidget(contentX + 180, currentY, 20, outlineColorGetter.get(), Text.literal("Outline Color"), box -> {
            this.activePopup = new ColorPickerPopup(this.width, this.height, box.getColor(), color -> {
                box.setColor(color);
                outlineColorSetter.accept(color);
            });
        });
        this.addDrawableChild(outlineColorBox);
        
        updateVisibility();
    }
    
    protected void addCustomWidgets(int x) {
        // Subclasses can override this to add custom widgets before the background widgets
    }

    private void updateVisibility() {
        HudBackgroundType type = bgTypeGetter.get();
        bgColorBox.visible = (type == HudBackgroundType.COLOR);
        bgColorBox.active = (type == HudBackgroundType.COLOR);
        
        outlineColorBox.visible = (type == HudBackgroundType.OUTLINE);
        outlineColorBox.active = (type == HudBackgroundType.OUTLINE);
        
        cornerRadiusSlider.visible = (type != HudBackgroundType.TRANSPARENT);
        cornerRadiusSlider.active = (type != HudBackgroundType.TRANSPARENT);
    }
    
    @Override
    protected void renderModalForeground(DrawContext context, int mouseX, int mouseY, float delta) {
        int contentX = this.startX + 20;
        int textY = this.startY + 106;
        
        if (bgColorBox.visible) {
            context.drawTextWithShadow(this.textRenderer, Text.literal("Background Color"), contentX + 25, textY, 0xFFFFFFFF);
        }
        if (outlineColorBox.visible) {
            context.drawTextWithShadow(this.textRenderer, Text.literal("Outline Color"), contentX + 205, textY, 0xFFFFFFFF);
        }
    }
    
    @Override
    protected void renderModalBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render the module preview at its actual position, but on top of the darkened background
        if (module != null && module.isEnabled()) {
            // Render it!
            module.render(context, delta);
            
            int mx = module.getX();
            int my = module.getY();
            int mw = (int)(module.getWidth() * module.getScale());
            int mh = (int)(module.getHeight() * module.getScale());
            
            // Draw a subtle border around the module to show it's selected/draggable (yellow highlight)
            context.fill(mx - 1, my - 1, mx + mw + 1, my, 0xFFFFFF00); // Top
            context.fill(mx - 1, my + mh, mx + mw + 1, my + mh + 1, 0xFFFFFF00); // Bottom
            context.fill(mx - 1, my, mx, my + mh, 0xFFFFFF00); // Left
            context.fill(mx + mw, my, mx + mw + 1, my + mh, 0xFFFFFF00); // Right
        }
    }

    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (button == 0 && module != null && module.isEnabled()) {
                                    int mx = module.getX();
            int my = module.getY();
            int mw = (int)(module.getWidth() * module.getScale());
            int mh = (int)(module.getHeight() * module.getScale());

            if (mouseX >= mx && mouseX <= mx + mw && mouseY >= my && mouseY <= my + mh) {
                isDragging = true;
                dragOffsetX = (int) mouseX - mx;
                dragOffsetY = (int) mouseY - my;
                return true;
            }
        }
        return super.mouseClicked(click, bl);
    }

    public boolean mouseReleased(net.minecraft.client.gui.Click click) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (button == 0 && isDragging) {
            isDragging = false;
            CaeserConfig.save();
            return true;
        }
        return super.mouseReleased(click);
    }
    
    public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {
        double mouseX = click.x();
        double mouseY = click.y();
        int button = click.button();
        if (isDragging && module != null) {
            int newX = (int) mouseX - dragOffsetX;
            int newY = (int) mouseY - dragOffsetY;
            
            // Constrain to screen bounds to prevent dragging off-screen
            int mw = (int)(module.getWidth() * module.getScale());
            int mh = (int)(module.getHeight() * module.getScale());
            newX = Math.max(0, Math.min(newX, this.width - mw));
            newY = Math.max(0, Math.min(newY, this.height - mh));
            
            module.setX(newX);
            module.setY(newY);
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public void close() {
        CaeserConfig.save();
        this.client.setScreen(parent);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
