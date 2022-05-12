package com.github.carthax08.servercore.placeholders;

import com.github.carthax08.servercore.Main;
import com.github.carthax08.servercore.util.DataStore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;


public class PluginPlaceholderExpansion extends PlaceholderExpansion {
    @NotNull
    private final Main plugin;

    public PluginPlaceholderExpansion(@NotNull Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "CarThax";
    }

    @Override
    public String getIdentifier() {
        return "prison";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("tokens")){
            return String.valueOf(DataStore.getPlayerData(player.getPlayer()).tokenBalance);
        }
        if(params.equalsIgnoreCase("autosmelt")){
            return DataStore.getPlayerData(player.getPlayer()).autosmelt ? "On" : "Off";
        }
        if(params.equalsIgnoreCase("prestige")){
            return String.valueOf(DataStore.getPlayerData(player.getPlayer()).pindex);
        }
        return null; // Placeholder is unknown by the Expansion
    }
}