import net.minecraft.client.gui.DrawContext;
public class CheckDrawContext {
    public static void main(String[] args) {
        DrawContext ctx = null;
        ctx.getMatrices().scale(0.5f, 0.5f, 1.0f);
    }
}
