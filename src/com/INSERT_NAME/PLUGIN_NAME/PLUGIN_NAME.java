package com.INSERT_NAME.PLUGIN_NAME;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PLUGIN_NAME extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println(ChatColor.GREEN + "[Plugin_NAME] Testing");
    }
    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[PLUGIN_NAME]: Plugin is disabled!");
    }
}
