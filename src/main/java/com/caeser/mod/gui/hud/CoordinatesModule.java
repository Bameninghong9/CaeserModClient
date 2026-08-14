package com.caeser.mod.gui.hud;

import com.caeser.mod.config.CaeserConfig;
import com.caeser.mod.gui.CaeserSettingsScreen;
import com.caeser.mod.gui.CoordinatesCategoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class CoordinatesModule implements IHudModule {

    @Override
    public String getName() {
        return "Coordinates";
    }

    @Override
    public boolean isEnabled() {
        return CaeserConfig.INSTANCE.coordinates;
    }

    @Override
    public void setEnabled(boolean enabled) {
        CaeserConfig.INSTANCE.coordinates = enabled;
        CaeserConfig.save();
    }

    @Override
    public int getX() {
        return CaeserConfig.INSTANCE.coordsX;
    }

    @Override
    public int getY() {
        return CaeserConfig.INSTANCE.coordsY;
    }

    @Override
    public void setX(int x) {
        CaeserConfig.INSTANCE.coordsX = x;
        CaeserConfig.save();
    }

    @Override
    public void setY(int y) {
        CaeserConfig.INSTANCE.coordsY = y;
        CaeserConfig.save();
    }

    @Override
    public float getScale() {
        return CaeserConfig.INSTANCE.coordsScale;
    }

    @Override
    public void setScale(float scale) {
        CaeserConfig.INSTANCE.coordsScale = scale;
        CaeserConfig.save();
    }

    @Override
    public int getWidth() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int maxWidth = 0;
        String[] lines = getCoordsLines();
        for (String line : lines) {
            int width = textRenderer.getWidth(line);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }

    @Override
    public int getHeight() {
        return getCoordsLines().length * MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        
        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float)getX(), (float)getY());
        context.getMatrices().scale(getScale(), getScale());
        
        IHudModule.drawBackground(context, this, getWidth(), getHeight(),
            CaeserConfig.INSTANCE.coordsBgType, CaeserConfig.INSTANCE.coordsBgColor, CaeserConfig.INSTANCE.coordsOutlineColor);
            
        String[] lines = getCoordsLines();
        for (int i = 0; i < lines.length; i++) {
            context.drawTextWithShadow(textRenderer, lines[i], 0, (i * textRenderer.fontHeight), 0xFFFFFFFF);
        }
        
        context.getMatrices().popMatrix();
    }

    public void openSettingsMenu(MinecraftClient client) {
        client.setScreen(new CoordinatesCategoryScreen(client.currentScreen instanceof CaeserSettingsScreen ? (CaeserSettingsScreen)client.currentScreen : new CaeserSettingsScreen(null), this));
    }

    private String[] getCoordsLines() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (player == null) {
            if (CaeserConfig.INSTANCE.coordsShowX) lines.add("X: 0");
            if (CaeserConfig.INSTANCE.coordsShowY) lines.add("Y: 0");
            if (CaeserConfig.INSTANCE.coordsShowZ) lines.add("Z: 0");
            if (CaeserConfig.INSTANCE.coordsShowBiome) lines.add("Biome: Plains");
            if (CaeserConfig.INSTANCE.coordsShowDirection) lines.add("Facing: North");
            return lines.toArray(new String[0]);
        }

        BlockPos pos = player.getBlockPos();
        if (CaeserConfig.INSTANCE.coordsShowX) lines.add("X: " + pos.getX());
        if (CaeserConfig.INSTANCE.coordsShowY) lines.add("Y: " + pos.getY());
        if (CaeserConfig.INSTANCE.coordsShowZ) lines.add("Z: " + pos.getZ());
        
        if (CaeserConfig.INSTANCE.coordsShowBiome && client.world != null) {
            String biomeName = client.world.getBiome(pos).getKey().map(k -> k.getValue().getPath()).orElse("unknown");
            if (biomeName.contains(":")) {
                biomeName = biomeName.substring(biomeName.indexOf(":") + 1);
            }
            biomeName = java.util.Arrays.stream(biomeName.split("_"))
                .map(word -> word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(java.util.stream.Collectors.joining(" "));
            lines.add("Biome: " + biomeName);
        }
        
        if (CaeserConfig.INSTANCE.coordsShowDirection) {
            String direction = player.getHorizontalFacing().asString();
            direction = Character.toUpperCase(direction.charAt(0)) + direction.substring(1);
            lines.add("Facing: " + direction);
        }
        
        return lines.toArray(new String[0]);
    }
}
