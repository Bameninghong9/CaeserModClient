package com.caeser.mod.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import org.joml.Matrix4f;

public class RenderUtils {

    public static void drawRoundedRect(DrawContext context, float x, float y, float width, float height, float radius, int color) {
        if (radius <= 0) {
            context.fill((int)x, (int)y, (int)(x + width), (int)(y + height), color);
            return;
        }

        radius = Math.min(radius, width / 2);
        radius = Math.min(radius, height / 2);

        int iradius = (int) radius;
        int ix = (int) x;
        int iy = (int) y;
        int iw = (int) width;
        int ih = (int) height;

        // Draw central cross
        context.fill(ix + iradius, iy, ix + iw - iradius, iy + ih, color);
        context.fill(ix, iy + iradius, ix + iradius, iy + ih - iradius, color);
        context.fill(ix + iw - iradius, iy + iradius, ix + iw, iy + ih - iradius, color);

        // Draw 4 corners
        for (int i = 0; i < iradius; i++) {
            int stripWidth = (int) Math.round(Math.sqrt(iradius * iradius - (iradius - i) * (iradius - i)));
            int offset = iradius - stripWidth;
            
            // Top Left
            context.fill(ix + offset, iy + i, ix + iradius, iy + i + 1, color);
            // Top Right
            context.fill(ix + iw - iradius, iy + i, ix + iw - offset, iy + i + 1, color);
            // Bottom Left
            context.fill(ix + offset, iy + ih - 1 - i, ix + iradius, iy + ih - i, color);
            // Bottom Right
            context.fill(ix + iw - iradius, iy + ih - 1 - i, ix + iw - offset, iy + ih - i, color);
        }
    }
}
