import java.nio.file.*;
import java.io.IOException;

public class RevertSignaturesBack {
    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("src/main/java/com/caeser/mod/gui"))
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p));
                    String original = content;
                    
                    content = content.replace("public boolean mouseClicked(double mouseX, double mouseY, int button)", "public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl)");
                    content = content.replace("super.mouseClicked(mouseX, mouseY, button)", "super.mouseClicked(click, bl)");
                    content = content.replace("this.hexField.mouseClicked(mouseX, mouseY, button)", "this.hexField.mouseClicked(click, bl)");
                    content = content.replace("box.mouseClicked(mouseX, mouseY, button)", "box.mouseClicked(click, bl)");
                    content = content.replace("activePopup.mouseClicked(mouseX, mouseY, button)", "activePopup.mouseClicked(click, bl)");
                    
                    content = content.replace("public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)", "public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY)");
                    content = content.replace("super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)", "super.mouseDragged(click, deltaX, deltaY)");
                    content = content.replace("activePopup.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)", "activePopup.mouseDragged(click, deltaX, deltaY)");
                    
                    content = content.replace("public boolean mouseReleased(double mouseX, double mouseY, int button)", "public boolean mouseReleased(net.minecraft.client.gui.Click click)");
                    content = content.replace("super.mouseReleased(mouseX, mouseY, button)", "super.mouseReleased(click)");
                    content = content.replace("activePopup.mouseReleased(mouseX, mouseY, button)", "activePopup.mouseReleased(click)");
                    
                    content = content.replace("public boolean charTyped(char chr, int modifiers)", "public boolean charTyped(net.minecraft.client.gui.CharInput input)");
                    content = content.replace("super.charTyped(chr, modifiers)", "super.charTyped(input)");
                    content = content.replace("this.hexField.charTyped(chr, modifiers)", "this.hexField.charTyped(input)");
                    content = content.replace("activePopup.charTyped(chr, modifiers)", "activePopup.charTyped(input)");
                    
                    content = content.replace("public boolean keyPressed(int keyCode, int scanCode, int modifiers)", "public boolean keyPressed(net.minecraft.client.gui.KeyInput input)");
                    content = content.replace("super.keyPressed(keyCode, scanCode, modifiers)", "super.keyPressed(input)");
                    content = content.replace("this.hexField.keyPressed(keyCode, scanCode, modifiers)", "this.hexField.keyPressed(input)");
                    content = content.replace("activePopup.keyPressed(keyCode, scanCode, modifiers)", "activePopup.keyPressed(input)");

                    if (!original.equals(content)) {
                        Files.write(p, content.getBytes());
                        System.out.println("Re-applied Click to " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
