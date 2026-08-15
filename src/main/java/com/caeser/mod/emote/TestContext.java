package com.caeser.mod.emote;
public class TestContext {
    public static void flush(net.minecraft.client.gui.DrawContext context) {
        net.minecraft.client.MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().draw();
    }
}
