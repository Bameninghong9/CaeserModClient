import java.nio.file.*;
import java.io.IOException;

public class FixInputPaths {
    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("src/main/java/com/caeser/mod/gui"))
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p));
                    String original = content;
                    
                    content = content.replace("net.minecraft.client.gui.CharInput", "net.minecraft.client.input.CharInput");
                    content = content.replace("net.minecraft.client.gui.KeyInput", "net.minecraft.client.input.KeyInput");

                    if (!original.equals(content)) {
                        Files.write(p, content.getBytes());
                        System.out.println("Fixed paths in " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
