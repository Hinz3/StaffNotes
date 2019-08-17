package com.shadow5353;

import com.shadow5353.Managers.CommandManager;
import com.shadow5353.Managers.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jacob on 23-12-2016.
 */
public class StaffNotes extends JavaPlugin implements Listener{

    @Override public void onEnable() {
        saveDefaultConfig();

        MessageManager.Init();

        if (getConfig().get("savingType").equals("mysql")) {
            MySQL.getInstance().startUp();
        } else if (getConfig().get("savingType").equals("file")) {
            FlatSaving.getInstance().setupNotes();
        }

        getCommand("staffnotes").setExecutor(new CommandManager());
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("StaffNotes");
    }
}
