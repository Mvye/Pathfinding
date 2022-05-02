package com.mvye.pathfinding;

import com.mvye.pathfinding.events.Pathfinder;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Pathfinding extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Pathfinder(),this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Pathfinding]: Plugin is enabled!");
    }
    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[Pathfinding]: Plugin is disabled!");
    }
}
