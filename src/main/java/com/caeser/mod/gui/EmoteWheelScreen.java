package com.caeser.mod.gui;

import com.caeser.mod.emote.EmoteManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.joml.Vector3f;
import org.joml.Quaternionf;

public class EmoteWheelScreen extends Screen {
    private int hoveredSlot = -1;
    public static String[] assignedEmotes = new String[8];
    private boolean wasMouseDown = false;
    
    static {
        // Start empty so user sees plus signs
    }

    public EmoteWheelScreen() {
        super(Text.literal("Emotes"));
    }

    @Override
    public void tick() {
        super.tick();
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        
        // Check B key release
        if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_B) != GLFW.GLFW_PRESS) {
            this.client.setScreen(null);
            if (hoveredSlot != -1 && assignedEmotes[hoveredSlot] != null) {
                EmoteManager.INSTANCE.playEmote("emotes/" + assignedEmotes[hoveredSlot] + ".json");
            }
            return;
        }
        
        // Check mouse click
        boolean isMouseDown = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        if (isMouseDown && !wasMouseDown && hoveredSlot != -1) {
            this.client.setScreen(new EmoteSelectionScreen(this, hoveredSlot));
        }
        wasMouseDown = isMouseDown;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x50000000); // 0x80000000 in NoRisk but slightly lighter
        
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int radius = 100;
        int slotSize = 64;
        
        hoveredSlot = -1;
        
        for (int i = 0; i < 8; i++) {
            // angle goes clockwise from top
            double angle = i * (Math.PI / 4) - (Math.PI / 2);
            int slotX = centerX + (int)(Math.cos(angle) * radius) - slotSize / 2;
            int slotY = centerY + (int)(Math.sin(angle) * radius) - slotSize / 2;
            
            boolean isHovered = mouseX >= slotX && mouseX <= slotX + slotSize && mouseY >= slotY && mouseY <= slotY + slotSize;
            if (isHovered) hoveredSlot = i;
            
            int color = isHovered ? 0x90666666 : 0x80222222;
            context.fill(slotX, slotY, slotX + slotSize, slotY + slotSize, color);
            
            // Draw cross or name
            if (assignedEmotes[i] == null) {
                context.fill(slotX + slotSize/2 - 1, slotY + slotSize/2 - 5, slotX + slotSize/2 + 1, slotY + slotSize/2 + 5, 0xFFFFFFFF);
                context.fill(slotX + slotSize/2 - 5, slotY + slotSize/2 - 1, slotX + slotSize/2 + 5, slotY + slotSize/2 + 1, 0xFFFFFFFF);
            } else {
                String name = assignedEmotes[i];
                int textWidth = this.textRenderer.getWidth(name);
                // We could draw the entity here too, but for the wheel just drawing the text is easier
                context.drawTextWithShadow(this.textRenderer, name, slotX + slotSize/2 - textWidth/2, slotY + slotSize/2 - 4, 0xFFFFFFFF);
            }
        }
        
        // Draw player in center!
        if (this.client.player != null) {
            float mouseXOffset = centerX - mouseX;
            float mouseYOffset = centerY - mouseY;
            // drawEntity(context, x1, y1, x2, y2, size, f, mouseX, mouseY, entity)
            InventoryScreen.drawEntity(context, centerX - 25, centerY - 50, centerX + 25, centerY + 50, 30, 0.0f, mouseXOffset, mouseYOffset, this.client.player);
        }
        
        context.drawCenteredTextWithShadow(this.textRenderer, "1 / 5", centerX, centerY + 60, 0xFFFFFF);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean shouldPause() {
        return false;
    }
}
