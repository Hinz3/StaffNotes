package com.shadow5353;

import com.shadow5353.Listeners.AlertStaff;
import com.shadow5353.Listeners.RegisterStaffJoin;
import com.shadow5353.Managers.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * Created by Jacob on 23-12-2016.
 */
public class StaffNotes extends JavaPlugin implements Listener{
    private static ArrayList<Player> staff = new ArrayList();

    public static ArrayList<Player> getStaff() {
        return staff;
    }

    public static void addStaff(Player player) {
        staff.add(player);
    }

    @Override
    public void onEnable() {
        getCommand("staffnotes").setExecutor(new CommandManager());

        saveDefaultConfig();

        if (getConfig().get("savingType").equals("mysql")) {
            MySQL.getInstance().startUp();
        } else if (getConfig().get("savingType") == "file") {
            FlatSaving.getInstance().setupNotes();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AlertStaff(), this);
        pm.registerEvents(new RegisterStaffJoin(), this);
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("StaffNotes");
    }
}
