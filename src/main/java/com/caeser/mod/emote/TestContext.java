package com.caeser.mod.emote;

public class TestContext {
    public static void flush(net.minecraft.client.gui.DrawContext context) {
        try {
            java.lang.reflect.Method m = context.getClass().getMethod("draw");
            m.invoke(context);
        } catch (Exception e) {}
    }
}
