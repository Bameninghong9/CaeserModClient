import java.nio.file.*;
import java.io.IOException;

public class RevertSignatures {
    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("src/main/java/com/caeser/mod/gui"))
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p));
                    String original = content;
                    content = content.replace("mouseClicked(net.minecraft.client.gui.Click click, boolean bl)", "mouseClicked(double mouseX, double mouseY, int button)");
                    content = content.replace("super.mouseClicked(click, bl)", "super.mouseClicked(mouseX, mouseY, button)");
                    content = content.replace("this.hexField.mouseClicked(click, bl)", "this.hexField.mouseClicked(mouseX, mouseY, button)");
                    content = content.replace("box.mouseClicked(click, bl)", "box.mouseClicked(mouseX, mouseY, button)");
                    content = content.replace("activePopup.mouseClicked(click, bl)", "activePopup.mouseClicked(mouseX, mouseY, button)");
                    content = content.replace("click.x()", "mouseX");
                    content = content.replace("click.y()", "mouseY");
                    content = content.replace("click.button()", "button");
                    
                    content = content.replace("mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY)", "mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY)");
                    content = content.replace("super.mouseDragged(click, deltaX, deltaY)", "super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)");
                    content = content.replace("activePopup.mouseDragged(click, deltaX, deltaY)", "activePopup.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)");
                    
                    content = content.replace("mouseReleased(net.minecraft.client.gui.Click click)", "mouseReleased(double mouseX, double mouseY, int button)");
                    content = content.replace("super.mouseReleased(click)", "super.mouseReleased(mouseX, mouseY, button)");
                    content = content.replace("activePopup.mouseReleased(click)", "activePopup.mouseReleased(mouseX, mouseY, button)");
                    
                    content = content.replace("charTyped(net.minecraft.client.gui.CharInput input)", "charTyped(char chr, int modifiers)");
                    content = content.replace("super.charTyped(input)", "super.charTyped(chr, modifiers)");
                    content = content.replace("this.hexField.charTyped(input)", "this.hexField.charTyped(chr, modifiers)");
                    content = content.replace("activePopup.charTyped(input)", "activePopup.charTyped(chr, modifiers)");
                    
                    content = content.replace("keyPressed(net.minecraft.client.gui.KeyInput input)", "keyPressed(int keyCode, int scanCode, int modifiers)");
                    content = content.replace("super.keyPressed(input)", "super.keyPressed(keyCode, scanCode, modifiers)");
                    content = content.replace("this.hexField.keyPressed(input)", "this.hexField.keyPressed(keyCode, scanCode, modifiers)");
                    content = content.replace("activePopup.keyPressed(input)", "activePopup.keyPressed(keyCode, scanCode, modifiers)");

                    if (!original.equals(content)) {
                        Files.write(p, content.getBytes());
                        System.out.println("Reverted " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
