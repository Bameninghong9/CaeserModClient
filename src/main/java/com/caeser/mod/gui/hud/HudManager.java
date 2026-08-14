package com.caeser.mod.gui.hud;

import net.minecraft.client.gui.DrawContext;
import java.util.ArrayList;
import java.util.List;

public class HudManager {
    public static final HudManager INSTANCE = new HudManager();
    private final List<IHudModule> modules = new ArrayList<>();

    private HudManager() {
    }

    public void init() {
        modules.add(new CpsModule());
        modules.add(new ReachModule());
        modules.add(new ComboModule());
        modules.add(new TargetHudModule());
        modules.add(new BossbarModule());
        modules.add(new ScoreboardModule());
        modules.add(new UptimeModule());
        modules.add(new CoordinatesModule());
        modules.add(new FpsModule());
        modules.add(new PingModule());
        modules.add(new KeystrokesModule());
        modules.add(new KeyboardModule());
    }

    public List<IHudModule> getModules() {
        return modules;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends IHudModule> T getModule(Class<T> clazz) {
        for (IHudModule module : modules) {
            if (clazz.isInstance(module)) {
                return (T) module;
            }
        }
        return null;
    }

    public void render(DrawContext context, float tickDelta) {
        for (IHudModule module : modules) {
            if (module.isEnabled()) {
                module.render(context, tickDelta);
            }
        }
    }
}
