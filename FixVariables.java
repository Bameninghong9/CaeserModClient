import java.nio.file.*;
import java.io.IOException;
import java.util.regex.*;

public class FixVariables {
    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("src/main/java/com/caeser/mod/gui"))
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p));
                    String original = content;
                    
                    content = content.replaceAll("public boolean mouseClicked\\(net\\.minecraft\\.client\\.gui\\.Click click, boolean bl\\) \\{\\s*(if \\(super\\.mouseClicked\\(click, bl\\)\\) return true;\\s*)?", 
                        "public boolean mouseClicked(net.minecraft.client.gui.Click click, boolean bl) {\n        double mouseX = click.x();\n        double mouseY = click.y();\n        int button = click.button();\n        ");
                        
                    content = content.replaceAll("public boolean mouseDragged\\(net\\.minecraft\\.client\\.gui\\.Click click, double deltaX, double deltaY\\) \\{\\s*(if \\(super\\.mouseDragged\\(click, deltaX, deltaY\\)\\) return true;\\s*)?", 
                        "public boolean mouseDragged(net.minecraft.client.gui.Click click, double deltaX, double deltaY) {\n        double mouseX = click.x();\n        double mouseY = click.y();\n        int button = click.button();\n        ");
                        
                    content = content.replaceAll("public boolean mouseReleased\\(net\\.minecraft\\.client\\.gui\\.Click click\\) \\{\\s*(if \\(super\\.mouseReleased\\(click\\)\\) return true;\\s*)?", 
                        "public boolean mouseReleased(net.minecraft.client.gui.Click click) {\n        double mouseX = click.x();\n        double mouseY = click.y();\n        int button = click.button();\n        ");
                        
                    content = content.replaceAll("public boolean charTyped\\(net\\.minecraft\\.client\\.gui\\.CharInput input\\) \\{\\s*(if \\(super\\.charTyped\\(input\\)\\) return true;\\s*)?", 
                        "public boolean charTyped(net.minecraft.client.gui.CharInput input) {\n        char chr = input.chr();\n        int modifiers = input.modifiers();\n        ");
                        
                    content = content.replaceAll("public boolean keyPressed\\(net\\.minecraft\\.client\\.gui\\.KeyInput input\\) \\{\\s*(if \\(super\\.keyPressed\\(input\\)\\) return true;\\s*)?", 
                        "public boolean keyPressed(net.minecraft.client.gui.KeyInput input) {\n        int keyCode = input.key();\n        int scanCode = input.scancode();\n        int modifiers = input.modifiers();\n        ");

                    if (!original.equals(content)) {
                        Files.write(p, content.getBytes());
                        System.out.println("Added variables to " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
