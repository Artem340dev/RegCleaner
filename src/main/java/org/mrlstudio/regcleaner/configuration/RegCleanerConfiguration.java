package org.mrlstudio.regcleaner.configuration;

import org.mrlstudio.regcleaner.RegCleanerPlugin;

import java.io.File;

public class RegCleanerConfiguration {
    public static void init() {
        File config = new File(RegCleanerPlugin.getInstance().getDataFolder() + File.separator + "config.yml");

        if (!config.exists()) {
            RegCleanerPlugin.getInstance().getConfig().options().copyDefaults(true);
            RegCleanerPlugin.getInstance().saveDefaultConfig();
        }
    }

    public static String getString(String path) {
        return RegCleanerPlugin.getInstance().getConfig().getString(path);
    }

    public static int getInt(String path) {
        return RegCleanerPlugin.getInstance().getConfig().getInt(path);
    }
}