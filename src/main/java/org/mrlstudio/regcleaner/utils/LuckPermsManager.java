package org.mrlstudio.regcleaner.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mrlstudio.regcleaner.configuration.RegCleanerConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class LuckPermsManager {
    private static LuckPerms api;

    static {
        api = LuckPermsProvider.get();
    }

    public static void loadUsers(List<OfflinePlayer> players) {
        players.forEach(player -> api.getUserManager().loadUser(player.getUniqueId()));
    }

    public static List<User> getUsersWithDonate() {
        Set<Group> groups = api.getGroupManager().getLoadedGroups().stream().filter(group -> !group.getName().equals(RegCleanerConfiguration.getString("default-group"))).collect(Collectors.toSet());

        List<User> users = api.getUserManager().getLoadedUsers().stream()
                .filter(user -> groups.contains(api.getGroupManager().getGroup(user.getPrimaryGroup())))
                .collect(Collectors.toList());

        return users;
    }

    public static void updateUserGroup(User user, String groupName) {
        Group oldGroup = api.getGroupManager().getGroup(user.getPrimaryGroup());
        Group newGroup = api.getGroupManager().getGroup(groupName);

        InheritanceNode oldGroupNode = InheritanceNode.builder(oldGroup).value(false).build();
        user.data().add(oldGroupNode);

        InheritanceNode newGroupNode = InheritanceNode.builder(newGroup).value(true).build();
        user.data().add(newGroupNode);

        api.getUserManager().saveUser(user);
    }
}