package com.shadow5353;

import com.shadow5353.Managers.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Jacob on 23-12-2016.
 */
public class StaffNotes extends JavaPlugin implements Listener{

    @Override public void onEnable() {
        getCommand("staffnotes").setExecutor(new CommandManager());

        saveDefaultConfig();

        if (getConfig().get("savingType").equals("mysql")) {
            MySQL.getInstance().startUp();
        } else if (getConfig().get("savingType").equals("file")) {
            FlatSaving.getInstance().setupNotes();
        }
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("StaffNotes");
    }
}
