package me._proflix_.antibuild;

import me._proflix_.antibuild.listeners.BlockBreak;
import me._proflix_.antibuild.listeners.BlockPlace;
import me._proflix_.antibuild.utils.ColorUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin {

    // Plugin startup logic
    @Override
    public void onEnable() {

        // bStats
        int pluginId = 9803;
        Metrics metrics = new Metrics(this, pluginId);

        // Save default config
        saveDefaultConfig();

        // Load files
        getLogger().info(ColorUtil.color("&7Loading files..."));
        loadFiles();
        getLogger().info(ColorUtil.color("&aFiles loaded successfully."));

        // Register events
        getLogger().info(ColorUtil.color("&7Registering all listeners..."));
        registerEvents();
        getLogger().info(ColorUtil.color("&aListeners loaded successfully."));

        getLogger().info(ColorUtil.color("&aPlugin enabled successfully."));
    }

    public void onDisable() {
        getLogger().info(ColorUtil.color("&cPlugin disabled!"));
    }

    private void loadFiles() {
        saveIfNotExists();
    }

    private void saveIfNotExists() {
        if (!(new File(getDataFolder(), "config.yml").exists())) {
            getLogger().info(ColorUtil.color("&cFile config.yml didn't exist, generating it..."));
            saveResource("config.yml", false);
        }
    }

    private void registerEvents() {
        final PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new BlockBreak(this), this);
        pluginManager.registerEvents(new BlockPlace(this), this);
    }

    // Is the module enabled logic
    public boolean isModuleEnabled(String configPath) {
        configPath = configPath + ".enabled";

        if (getConfig().contains(configPath)) {
            return getConfig().getBoolean(configPath);
        } else {
            return false;
        }
    }

    // Per world stuff
    public boolean isEnabledInList(String string, String configPath) {
        if (getConfig().getBoolean(configPath + ".all")) {
            return true;
        } else {
            List<String> list = getConfig().getStringList(configPath + ".list");
            String mode = Objects.requireNonNull(getConfig().getString(configPath + ".mode")).toUpperCase();
            switch (mode) {
                case "WHITELIST":
                    return list.contains(string);
                case "BLACKLIST":
                    return !list.contains(string);
                default:
                    getLogger().info(ColorUtil.color("&cYou must use WHITELIST or BLACKLIST mode. Anything other is disabled."));
                    return false;
            }
        }
    }
}
