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
    private File messageFile = new File(StaffNotes.getPlugin().getDataFolder(), "messages.yml");
    private FileConfiguration messageConfig = YamlConfiguration.loadConfiguration(messageFile);

    private MessageManager() {
        if (!messageFile.exists())
            StaffNotes.getPlugin().saveResource("messages.yml", false);
    }

    private static MessageManager messageManager = new MessageManager();

    public static MessageManager getMessageManager() {
        return messageManager;
    }

    private String prefix = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Staff Notes" + ChatColor.GOLD + "] ";

    public void info(CommandSender s, String msg) {
        msg(s, ChatColor.YELLOW, msg);
    }

    public void command(CommandSender s, String msg) {
        msg(s, ChatColor.GOLD, msg);
    }

    public void error(CommandSender s, String msg) {
        msg(s, ChatColor.RED, msg);
    }

    public void good(CommandSender s, String msg) {
        msg(s, ChatColor.GREEN, msg);
    }

    public void alert(CommandSender s, String msg) {
        String alertPrefix = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Staff Notes: Alert" + ChatColor.GOLD + "] ";
        msg(s, ChatColor.DARK_RED, msg, alertPrefix);
    }

    public void noPermission(CommandSender s) {
        msg(s, ChatColor.DARK_RED, "You do not have permission!");
    }

    private void msg(CommandSender s, ChatColor color, String msg) {
        s.sendMessage(prefix + color + msg);
    }

    private void msg(CommandSender s, ChatColor color, String msg, String prefix) {
        s.sendMessage(prefix + color + msg);
    }
}