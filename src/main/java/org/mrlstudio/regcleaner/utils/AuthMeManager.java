package org.mrlstudio.regcleaner.utils;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.api.v3.AuthMePlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class AuthMeManager {
    private static AuthMeApi api;

    static {
        api = AuthMeApi.getInstance();
    }

    public static LocalDate getLastLoginDate(OfflinePlayer player) {
        if (!api.getPlayerInfo(player.getName()).isPresent()) return null;

        AuthMePlayer authMePlayer = api.getPlayerInfo(player.getName()).get();
        Instant instant = authMePlayer.getRegistrationDate();
        LocalDate date = LocalDate.ofInstant(instant, ZoneId.of("Russia/Moscow"));

        return date;
    }

    public static void unregisterPlayer(OfflinePlayer player) {
        if (!api.getPlayerInfo(player.getName()).isPresent()) return;
        api.forceUnregister(player.getName());
    }
}