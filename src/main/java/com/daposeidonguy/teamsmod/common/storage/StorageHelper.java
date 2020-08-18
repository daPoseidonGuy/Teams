package com.daposeidonguy.teamsmod.common.storage;

import net.minecraft.inventory.InventoryEnderChest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StorageHelper {
    public static String getTeam(UUID playerId) {
        return StorageHandler.uuidToTeamMap.get(playerId);
    }

    public static UUID getTeamOwner(String teamName) {
        return StorageHandler.teamToUuidsMap.get(teamName).get(0);
    }

    public static List<UUID> getTeamPlayers(String teamName) {
        return StorageHandler.teamToUuidsMap.get(teamName);
    }

    public static Set<String> getTeamSet() {
        return StorageHandler.teamSettingsMap.keySet();
    }

    public static boolean getTeamSetting(String teamName, String setting) {
        return StorageHandler.teamSettingsMap.get(teamName).get(setting);
    }

    public static void setTeamSetting(String teamName, String setting, boolean value) {
        StorageHandler.teamSettingsMap.get(teamName).put(setting, value);
    }

    public static boolean isPlayerInTeam(UUID playerId) {
        return StorageHandler.uuidToTeamMap.containsKey(playerId);
    }

    public static boolean doesTeamExist(String teamName) {
        return teamName != null && StorageHandler.teamToUuidsMap.containsKey(teamName);
    }

    public static InventoryEnderChest getTeamEnderChest(String team) {
        if (doesTeamExist(team)) {
            if (StorageHandler.teamChestMap.get(team) == null) {
                StorageHandler.teamChestMap.put(team, new InventoryEnderChest());
            }
            return StorageHandler.teamChestMap.get(team);
        } else {
            return null;
        }
    }
}
