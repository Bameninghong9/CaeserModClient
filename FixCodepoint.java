import java.nio.file.*;
import java.io.IOException;

public class FixCodepoint {
    public static void main(String[] args) throws IOException {
        Files.walk(Paths.get("src/main/java/com/caeser/mod/gui"))
            .filter(Files::isRegularFile)
            .filter(p -> p.toString().endsWith(".java"))
            .forEach(p -> {
                try {
                    String content = new String(Files.readAllBytes(p));
                    String original = content;
                    
                    content = content.replace("char chr = input.chr();", "char chr = (char) input.codepoint();");

                    if (!original.equals(content)) {
                        Files.write(p, content.getBytes());
                        System.out.println("Fixed codepoint in " + p);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
    }
}
