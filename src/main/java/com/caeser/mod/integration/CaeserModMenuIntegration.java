package com.caeser.mod.integration;

import com.caeser.mod.gui.CaeserSettingsScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class CaeserModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CaeserSettingsScreen::new;
    }
}
