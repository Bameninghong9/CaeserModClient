package com.caeser.mod.gui;

import com.caeser.mod.emote.PreviewHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.text.Text;

public class EmoteSelectionScreen extends Screen {
    private final EmoteWheelScreen parent;
    private final int targetSlot;
    
    public java.util.List<String> getAvailableEmotes() {
        return com.caeser.mod.emote.UnlockedEmotes.unlocked;
    }

    public EmoteSelectionScreen(EmoteWheelScreen parent, int targetSlot) {
        super(Text.literal("Select Emote"));
        this.parent = parent;
        this.targetSlot = targetSlot;
    }

    private boolean wasMouseDown = false;
    
    @Override
    public void tick() {
        super.tick();
        PreviewHelper.previewTime += 0.05f;
        
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        boolean mouseDown = org.lwjgl.glfw.GLFW.glfwGetMouseButton(handle, org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT) == org.lwjgl.glfw.GLFW.GLFW_PRESS;
        
        if (mouseDown && !wasMouseDown) {
            double mouseX = this.client.mouse.getX() * this.client.getWindow().getScaledWidth() / (double) this.client.getWindow().getWidth();
            double mouseY = this.client.mouse.getY() * this.client.getWindow().getScaledHeight() / (double) this.client.getWindow().getHeight();
            
            int startX = 20;
            int startY = 40;
            int slotWidth = 80;
            int slotHeight = 100;
            int cols = 5;
            int padding = 10;
            
            java.util.List<String> emotes = getAvailableEmotes();
            for (int i = 0; i < emotes.size(); i++) {
                int col = i % cols;
                int row = i / cols;
                int x = startX + col * (slotWidth + padding);
                int y = startY + row * (slotHeight + padding);
                
                if (mouseX >= x && mouseX <= x + slotWidth && mouseY >= y && mouseY <= y + slotHeight) {
                    EmoteWheelScreen.assignedEmotes[targetSlot] = emotes.get(i);
                    this.client.setScreen(parent);
                    return;
                }
            }
        }
        wasMouseDown = mouseDown;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xD0000000);
        context.drawTextWithShadow(this.textRenderer, "CLASSIC", 20, 20, 0xFFFFFFFF);
        
        int startX = 20;
        int startY = 40;
        int slotWidth = 80;
        int slotHeight = 100;
        int cols = 5;
        int padding = 10;
        
        java.util.List<String> emotes = getAvailableEmotes();
        for (int i = 0; i < emotes.size(); i++) {
            int col = i % cols;
            int row = i / cols;
            int x = startX + col * (slotWidth + padding);
            int y = startY + row * (slotHeight + padding);
            
            boolean hovered = mouseX >= x && mouseX <= x + slotWidth && mouseY >= y && mouseY <= y + slotHeight;
            context.fill(x, y, x + slotWidth, y + slotHeight, hovered ? 0x80555555 : 0x80222222);
            context.drawTextWithShadow(this.textRenderer, emotes.get(i), x + 5, y - 10, 0xFFFFFFFF);
            
            // Draw player preview
            if (this.client.player != null) {
                // Set the override ONLY for this render call
                PreviewHelper.previewEmoteName = emotes.get(i);
                
                int centerX = x + slotWidth / 2;
                int centerY = y + slotHeight - 10;
                
                // For preview, we want them facing us or slightly angled
                // In InventoryScreen it uses mouse position to make them look at mouse
                float mouseXOffset = centerX - mouseX;
                float mouseYOffset = centerY - mouseY - 40;
                
                InventoryScreen.drawEntity(context, x, y, x + slotWidth, y + slotHeight, 35, 0.0f, mouseXOffset, mouseYOffset, this.client.player);
                net.minecraft.client.MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().draw();
            }
        }
        
        // Reset preview so the real game doesn't animate!
        PreviewHelper.previewEmoteName = null;
        
        super.render(context, mouseX, mouseY, delta);
    }
}
