import java.lang.reflect.Method;
public class Test {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("net.minecraft.client.gui.screen.Screen");
        for (Method m : clazz.getMethods()) {
            if (m.getName().toLowerCase().contains("mouse") || m.getName().toLowerCase().contains("key") || m.getName().toLowerCase().contains("char")) {
                System.out.println(m.toString());
            }
        }
    }
}
