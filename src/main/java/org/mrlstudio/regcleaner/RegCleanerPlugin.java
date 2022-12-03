package org.mrlstudio.regcleaner;

import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.mrlstudio.regcleaner.configuration.RegCleanerConfiguration;
import org.mrlstudio.regcleaner.utils.AuthMeManager;
import org.mrlstudio.regcleaner.utils.LuckPermsManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class RegCleanerPlugin extends JavaPlugin {
    private static RegCleanerPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        RegCleanerConfiguration.init();

        LuckPermsManager.loadUsers(Arrays.asList(Bukkit.getOfflinePlayers()));

        LuckPermsManager.getUsersWithDonate().stream().filter(user -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(user.getUniqueId());
            LocalDate lastLoginDate = AuthMeManager.getLastLoginDate(player);

            return Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), lastLoginDate)) >= RegCleanerConfiguration.getInt("max-days");
        }).forEach(user -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(user.getUsername());

            LuckPermsManager.updateUserGroup(user, RegCleanerConfiguration.getString("default-group"));
            AuthMeManager.unregisterPlayer(player);

            this.getLogger().info(ChatColor.AQUA + "User " + player.getName() + " has been deleted.");
        });
    }

    @Override
    public void onDisable() {
    }

    public static RegCleanerPlugin getInstance() {
        return instance;
    }
}