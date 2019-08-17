package com.shadow5353.Managers;

import com.shadow5353.StaffNotes;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Created by Jacob on 23-12-2016.
 */
public class MessageManager {
    private static File messageFile;
    private static FileConfiguration messageConfig;
    private static String prefix;

    public static void Init() {
        messageFile = new File(StaffNotes.getPlugin().getDataFolder(), "messages.yml");

        if (!messageFile.exists())
            StaffNotes.getPlugin().saveResource("messages.yml", false);

        messageConfig = YamlConfiguration.loadConfiguration(messageFile);

        prefix = getMessageConfig().getString("prefix") + " "  + ChatColor.YELLOW;
    }

    public static void reloadMessageFile() {
        messageFile = new File(StaffNotes.getPlugin().getDataFolder(), "messages.yml");
        messageConfig = YamlConfiguration.loadConfiguration(messageFile);

        prefix = getMessageConfig().getString("prefix") + " "  + ChatColor.YELLOW;
    }

    public static FileConfiguration getMessageConfig() {
        return messageConfig;
    }

    public static void info(CommandSender s, String msg) {
        msg(s, msg);
    }

    public static void command(CommandSender s, String msg) {
        msg(s, msg);
    }

    public static void error(CommandSender s, String msg) {
        msg(s, msg);
    }

    public static void good(CommandSender s, String msg) {
        msg(s, msg);
    }

    public static void alert(CommandSender s, String msg) {
        String alertPrefix = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Staff Notes: Alert" + ChatColor.GOLD + "] ";
    }

    public static void noPermission(CommandSender s) {
        msg(s,getMessageConfig().getString("missing-permission"));
    }

    private static void msg(CommandSender s, String msg) {
        s.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + msg));
    }
}