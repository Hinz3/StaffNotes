package dk.jacobhinze.StaffNotes;

import dk.jacobhinze.StaffNotes.Listeners.AlertStaff;
import dk.jacobhinze.StaffNotes.Listeners.RegisterStaffJoin;
import dk.jacobhinze.StaffNotes.Managers.CommandManager;
import dk.jacobhinze.StaffNotes.Managers.MessageManager;
import dk.jacobhinze.StaffNotes.Managers.NoteManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
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

        if (getConfig().get("savingType") == "mysql") {
            MySQL mySQL = new MySQL();

            mySQL.startUp();
        } else if (getConfig().get("savingType") == "file") {

        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AlertStaff(), this);
        pm.registerEvents(new RegisterStaffJoin(), this);
    }

    public static Plugin getPlugin() {
        return Bukkit.getServer().getPluginManager().getPlugin("StaffNotes");
    }
}
