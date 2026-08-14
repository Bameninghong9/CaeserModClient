package com.caeser.mod.gui;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.widget.CaeserModuleWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import com.caeser.mod.gui.hud.HudBackgroundType;

public class CaeserSettingsScreen extends Screen {
    private final Screen parent;
    private int currentCategory = 0; // 0 = All, 1 = Combat, 2 = Chat, 3 = Gameplay
    private final String[] categories = {"All", "Combat", "Chat", "Gameplay"};
    
    private TextFieldWidget searchField;
    private String searchQuery = "";
    
    private double scrollAmount = 0;
    private int maxScroll = 0;

    public CaeserSettingsScreen(Screen parent) {
        super(Text.literal("Caeser Client Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        
        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;
        
        this.clearChildren();

        this.searchField = new TextFieldWidget(this.textRenderer, startX + panelWidth - 155, startY + 6, 150, 12, Text.literal("Search"));
        this.searchField.setText(this.searchQuery);
        this.searchField.setDrawsBackground(false);
        this.searchField.setChangedListener(text -> {
            this.searchQuery = text.toLowerCase();
            this.scrollAmount = 0; // Reset scroll on search
            this.rebuildWidgets();
        });
        this.addDrawableChild(this.searchField);

        this.rebuildWidgets();
    }

    private void rebuildWidgets() {
        // Clear all except searchField
        this.clearChildren();
        this.addDrawableChild(this.searchField);

        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;
        
        int currentX = startX + 10;
        int currentY = startY + 34; // Start lower for padding
        int columnWidth = (panelWidth - 30) / 2;

        boolean showAll = currentCategory == 0;
        boolean showCombat = currentCategory == 1;
        boolean showChat = currentCategory == 2;
        boolean showGameplay = currentCategory == 3;
        
        // Helper to add widget and advance grid
        java.util.function.Consumer<CaeserModuleWidget> addWidget = (widget) -> {
            this.addDrawableChild(widget);
        };

        // Track items added
        int itemsAdded = 0;

        // COMBAT
        if (showAll || showCombat) {
            if ("hitboxes".contains(searchQuery)) {
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Hitboxes", "Show player and mob hitboxes", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/hitboxes.png"),
                    () -> CaeserConfig.INSTANCE.hitboxes, 
                    val -> { CaeserConfig.INSTANCE.hitboxes = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new HitboxCategoryScreen(this))));
                itemsAdded++;
            }

            if ("hitcolor".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "HitColor", "Change the color of damage tint", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/hitcolor.png"),
                    () -> CaeserConfig.INSTANCE.hitColors, 
                    val -> { CaeserConfig.INSTANCE.hitColors = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new HitColorCategoryScreen(this))));
                itemsAdded++;
            }
            
            if ("combo counter".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Combo Counter", "Display your current hit combo", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/combo.png"),
                    () -> CaeserConfig.INSTANCE.comboCounter, 
                    val -> { CaeserConfig.INSTANCE.comboCounter = val; CaeserConfig.save(); }, 
                    createBgMenu("Combo Counter", com.caeser.mod.gui.hud.ComboModule.class,
                        () -> CaeserConfig.INSTANCE.comboBgType, val -> CaeserConfig.INSTANCE.comboBgType = val,
                        () -> CaeserConfig.INSTANCE.comboBgColor, val -> CaeserConfig.INSTANCE.comboBgColor = val,
                        () -> CaeserConfig.INSTANCE.comboOutlineColor, val -> CaeserConfig.INSTANCE.comboOutlineColor = val)));
                itemsAdded++;
            }

            if ("reach display".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Reach Display", "Show distance to your target", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/reach.png"),
                    () -> CaeserConfig.INSTANCE.reachDisplay, 
                    val -> { CaeserConfig.INSTANCE.reachDisplay = val; CaeserConfig.save(); }, 
                    createBgMenu("Reach Display", com.caeser.mod.gui.hud.ReachModule.class,
                        () -> CaeserConfig.INSTANCE.reachBgType, val -> CaeserConfig.INSTANCE.reachBgType = val,
                        () -> CaeserConfig.INSTANCE.reachBgColor, val -> CaeserConfig.INSTANCE.reachBgColor = val,
                        () -> CaeserConfig.INSTANCE.reachOutlineColor, val -> CaeserConfig.INSTANCE.reachOutlineColor = val)));
                itemsAdded++;
            }

            if ("cps display".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "CPS Display", "Shows clicks per second", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/cps.png"),
                    () -> CaeserConfig.INSTANCE.cpsDisplay, 
                    val -> { CaeserConfig.INSTANCE.cpsDisplay = val; CaeserConfig.save(); }, 
                    createBgMenu("CPS Display", com.caeser.mod.gui.hud.CpsModule.class,
                        () -> CaeserConfig.INSTANCE.cpsBgType, val -> CaeserConfig.INSTANCE.cpsBgType = val,
                        () -> CaeserConfig.INSTANCE.cpsBgColor, val -> CaeserConfig.INSTANCE.cpsBgColor = val,
                        () -> CaeserConfig.INSTANCE.cpsOutlineColor, val -> CaeserConfig.INSTANCE.cpsOutlineColor = val)));
                itemsAdded++;
            }

            if ("target hud".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Target HUD", "Shows info about your target", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/targethud.png"),
                    () -> CaeserConfig.INSTANCE.targetHud, 
                    val -> { CaeserConfig.INSTANCE.targetHud = val; CaeserConfig.save(); }, 
                    createBgMenu("Target HUD", com.caeser.mod.gui.hud.TargetHudModule.class,
                        () -> CaeserConfig.INSTANCE.targetHudBgType, val -> CaeserConfig.INSTANCE.targetHudBgType = val,
                        () -> CaeserConfig.INSTANCE.targetHudBgColor, val -> CaeserConfig.INSTANCE.targetHudBgColor = val,
                        () -> CaeserConfig.INSTANCE.targetHudOutlineColor, val -> CaeserConfig.INSTANCE.targetHudOutlineColor = val)));
                itemsAdded++;
            }

            if ("low fire".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Low Fire", "Lowers fire on your screen", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/lowfire.png"),
                    () -> CaeserConfig.INSTANCE.lowFire, 
                    val -> { CaeserConfig.INSTANCE.lowFire = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new LowFireCategoryScreen(this))));
                itemsAdded++;
            }
        }

        // GAMEPLAY
        if (showAll || showGameplay) {
            if ("bossbar".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Bossbar", "Customize the boss health bar", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/bossbar.png"),
                    () -> CaeserConfig.INSTANCE.customBossbar, 
                    val -> { CaeserConfig.INSTANCE.customBossbar = val; CaeserConfig.save(); }, null));
                itemsAdded++;
            }

            if ("scoreboard".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Scoreboard", "Move or hide the scoreboard", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/scoreboard.png"),
                    () -> CaeserConfig.INSTANCE.customScoreboard, 
                    val -> { CaeserConfig.INSTANCE.customScoreboard = val; CaeserConfig.save(); }, 
                    createBgMenu("Scoreboard", com.caeser.mod.gui.hud.ScoreboardModule.class,
                        () -> CaeserConfig.INSTANCE.scoreboardBgType, val -> CaeserConfig.INSTANCE.scoreboardBgType = val,
                        () -> CaeserConfig.INSTANCE.scoreboardBgColor, val -> CaeserConfig.INSTANCE.scoreboardBgColor = val,
                        () -> CaeserConfig.INSTANCE.scoreboardOutlineColor, val -> CaeserConfig.INSTANCE.scoreboardOutlineColor = val)));
                itemsAdded++;
            }
            
            if ("fps display".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "FPS Display", "Shows your current frames per second", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/fps.png"),
                    () -> CaeserConfig.INSTANCE.fps, 
                    val -> { CaeserConfig.INSTANCE.fps = val; CaeserConfig.save(); }, 
                    createBgMenu("FPS Display", com.caeser.mod.gui.hud.FpsModule.class,
                        () -> CaeserConfig.INSTANCE.fpsBgType, val -> CaeserConfig.INSTANCE.fpsBgType = val,
                        () -> CaeserConfig.INSTANCE.fpsBgColor, val -> CaeserConfig.INSTANCE.fpsBgColor = val,
                        () -> CaeserConfig.INSTANCE.fpsOutlineColor, val -> CaeserConfig.INSTANCE.fpsOutlineColor = val)));
                itemsAdded++;
            }

            if ("no fog".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "No Fog", "Removes fog from the game", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/nofog.png"),
                    () -> CaeserConfig.INSTANCE.noFog, 
                    val -> { CaeserConfig.INSTANCE.noFog = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new NoFogCategoryScreen(this))));
                itemsAdded++;
            }


            if ("uptime".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Uptime", "Shows how long you've played", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/uptime.png"),
                    () -> CaeserConfig.INSTANCE.uptime, 
                    val -> { CaeserConfig.INSTANCE.uptime = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new UptimeCategoryScreen(this, com.caeser.mod.gui.hud.HudManager.INSTANCE.getModule(com.caeser.mod.gui.hud.UptimeModule.class)))));
                itemsAdded++;
            }

            if ("coordinates".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Coordinates", "Shows your position in the world", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/coordinates.png"),
                    () -> CaeserConfig.INSTANCE.coordinates, 
                    val -> { CaeserConfig.INSTANCE.coordinates = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new CoordinatesCategoryScreen(this, com.caeser.mod.gui.hud.HudManager.INSTANCE.getModule(com.caeser.mod.gui.hud.CoordinatesModule.class)))));
                itemsAdded++;
            }

            if ("fullbright".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Fullbright", "Makes the game permanently bright", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/fullbright.png"),
                    () -> CaeserConfig.INSTANCE.fullbright, 
                    val -> { CaeserConfig.INSTANCE.fullbright = val; CaeserConfig.save(); }, null));
                itemsAdded++;
            }
        }

        // CHAT
        if (showAll || showChat) {
            if ("chat heads".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Chat Heads", "Show player heads in chat", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/chatheads.png"),
                    () -> CaeserConfig.INSTANCE.chatHeads, 
                    val -> { CaeserConfig.INSTANCE.chatHeads = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new ChatHeadsCategoryScreen(this))));
                itemsAdded++;
            }

            if ("stack messages".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "Stack Messages", "Stacks repeated chat messages", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/stackmessages.png"),
                    () -> CaeserConfig.INSTANCE.stackMessages, 
                    val -> { CaeserConfig.INSTANCE.stackMessages = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new StackingCategoryScreen(this))));
                itemsAdded++;
            }

            if ("autotext".contains(searchQuery)) {
                if (itemsAdded % 2 == 1) { currentX += columnWidth + 10; } else if (itemsAdded > 0) { currentX = startX + 10; currentY += 52; }
                addWidget.accept(createToggle(currentX, currentY, columnWidth, "AutoText", "Send commands automatically", net.minecraft.util.Identifier.of("caeserclient", "textures/gui/icons/autotext.png"),
                    () -> CaeserConfig.INSTANCE.autoTextEnabled, 
                    val -> { CaeserConfig.INSTANCE.autoTextEnabled = val; CaeserConfig.save(); }, 
                    () -> MinecraftClient.getInstance().setScreen(new AutoTextScreen(this))));
                itemsAdded++;
            }
        }
        
        // Calculate max scroll
        if (itemsAdded > 0) {
            maxScroll = Math.max(0, currentY + 52 - (startY + panelHeight) + 10);
            scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
        } else {
            maxScroll = 0;
            scrollAmount = 0;
        }
    }

    private CaeserModuleWidget createToggle(int x, int y, int width, String title, String description, net.minecraft.util.Identifier iconId, java.util.function.Supplier<Boolean> getter, java.util.function.Consumer<Boolean> setter, Runnable rightClick) {
        return new CaeserModuleWidget(x, y - (int)scrollAmount, width, 48, Text.literal(title), description, iconId, getter, setter, rightClick);
    }

    private Runnable createBgMenu(String name, Class<? extends com.caeser.mod.gui.hud.IHudModule> moduleClass, java.util.function.Supplier<HudBackgroundType> bgTypeGetter, java.util.function.Consumer<HudBackgroundType> bgTypeSetter,
                                  java.util.function.Supplier<Integer> bgColorGetter, java.util.function.Consumer<Integer> bgColorSetter,
                                  java.util.function.Supplier<Integer> outlineColorGetter, java.util.function.Consumer<Integer> outlineColorSetter) {
        return () -> MinecraftClient.getInstance().setScreen(new HudBackgroundCategoryScreen(this, Text.literal(name), com.caeser.mod.gui.hud.HudManager.INSTANCE.getModule(moduleClass), bgTypeGetter, bgTypeSetter, bgColorGetter, bgColorSetter, outlineColorGetter, outlineColorSetter));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (maxScroll > 0) {
            scrollAmount -= verticalAmount * 24; // Scroll by one row height per tick
            scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
            rebuildWidgets(); // Update positions
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x80030712);

        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        context.fill(startX, startY + 24, startX + panelWidth, startY + panelHeight, 0xB20A0F1D);
        context.fill(startX, startY, startX + panelWidth, startY + 24, 0xFF0F172A);

        int tabWidth = 72;
        for (int i = 0; i < categories.length; i++) {
            int tabX = startX + (i * tabWidth);
            int textColor = 0xFFAAAAAA;
            
            if (mouseX >= tabX && mouseX <= tabX + tabWidth && mouseY >= startY && mouseY <= startY + 24) {
                context.fill(tabX, startY, tabX + tabWidth, startY + 24, 0xFF1E293B);
                textColor = 0xFFFFFFFF;
            }
            
            if (currentCategory == i) {
                context.fill(tabX, startY, tabX + tabWidth, startY + 24, 0xFF3B82F6);
                textColor = 0xFFFFFFFF;
            }

            int textWidth = this.textRenderer.getWidth(categories[i]);
            context.drawTextWithShadow(this.textRenderer, Text.literal(categories[i]), tabX + (tabWidth - textWidth) / 2, startY + 8, textColor);
        }

        // Custom search bar background
        int searchX = startX + panelWidth - 160;
        int searchY = startY + 2;
        int searchW = 155;
        int searchH = 20;
        context.fill(searchX, searchY, searchX + searchW, searchY + searchH, 0xFF1E293B);
        int borderColor = this.searchField.isFocused() ? 0xFF3B82F6 : 0xFF475569;
        context.fill(searchX, searchY, searchX + searchW, searchY + 1, borderColor); // Top
        context.fill(searchX, searchY + searchH - 1, searchX + searchW, searchY + searchH, borderColor); // Bottom
        context.fill(searchX, searchY, searchX + 1, searchY + searchH, borderColor); // Left
        context.fill(searchX + searchW - 1, searchY, searchX + searchW, searchY + searchH, borderColor); // Right

        if (this.searchQuery.isEmpty() && !this.searchField.isFocused()) {
            context.drawTextWithShadow(this.textRenderer, "Search...", startX + panelWidth - 155, startY + 8, 0xFF94A3B8);
        }

        // Apply Scissors for scrolling content
        context.enableScissor(startX, startY + 24, startX + panelWidth, startY + panelHeight);
        super.render(context, mouseX, mouseY, delta);
        
        // Draw Scrollbar if needed
        if (maxScroll > 0) {
            int scrollbarX = startX + panelWidth - 4;
            int scrollbarY = startY + 26;
            int scrollbarHeight = panelHeight - 28;
            float scrollPercentage = (float)scrollAmount / maxScroll;
            int handleHeight = Math.max(20, (int)((panelHeight / (float)(panelHeight + maxScroll)) * scrollbarHeight));
            int handleY = scrollbarY + (int)(scrollPercentage * (scrollbarHeight - handleHeight));
            
            context.fill(scrollbarX, scrollbarY, scrollbarX + 2, scrollbarY + scrollbarHeight, 0x80000000);
            context.fill(scrollbarX, handleY, scrollbarX + 2, handleY + handleHeight, 0xFF3B82F6);
        }
        
        context.disableScissor();
        
        // Re-render search field without scissor so text shows up!
        this.searchField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {
        double mouseX = click.x();
        double mouseY = click.y();
        int panelWidth = 450;
        int panelHeight = 300;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        int tabWidth = 72;

        if (mouseY >= startY && mouseY <= startY + 24) {
            for (int i = 0; i < categories.length; i++) {
                int tabX = startX + (i * tabWidth);
                if (mouseX >= tabX && mouseX <= tabX + tabWidth) {
                    this.currentCategory = i;
                    this.scrollAmount = 0; // reset scroll
                    this.init();
                    return true;
                }
            }
        }
        
        return super.mouseClicked(click, bl);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }
}
