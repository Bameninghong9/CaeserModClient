import java.nio.file.*;
import java.io.IOException;

public class FixErrors {
    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("src/main/java/com/caeser/mod/gui"))
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p));
                    String original = content;
                    
                    // Remove @Override before input methods
                    content = content.replaceAll("(?s)@Override\\s+public\\s+boolean\\s+mouseClicked", "public boolean mouseClicked");
                    content = content.replaceAll("(?s)@Override\\s+public\\s+boolean\\s+mouseDragged", "public boolean mouseDragged");
                    content = content.replaceAll("(?s)@Override\\s+public\\s+boolean\\s+mouseReleased", "public boolean mouseReleased");
                    content = content.replaceAll("(?s)@Override\\s+public\\s+boolean\\s+charTyped", "public boolean charTyped");
                    content = content.replaceAll("(?s)@Override\\s+public\\s+boolean\\s+keyPressed", "public boolean keyPressed");
                    
                    // Remove the double mouseX = mouseX syntax error
                    content = content.replace("double mouseX = mouseX;\n", "");
                    content = content.replace("double mouseY = mouseY;\n", "");
                    content = content.replace("int button = button;\n", "");
                    
                    // Same for tab width issues
                    content = content.replace("double mouseX = mouseX;\r\n", "");
                    content = content.replace("double mouseY = mouseY;\r\n", "");
                    content = content.replace("int button = button;\r\n", "");

                    // Fix other variables that were replaced by my previous script
                    content = content.replace("super.keyPressed(keyCode, scanCode, modifiers)", "super.keyPressed(keyCode, scanCode, modifiers)");

                    if (!original.equals(content)) {
                        Files.write(p, content.getBytes());
                        System.out.println("Fixed " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
