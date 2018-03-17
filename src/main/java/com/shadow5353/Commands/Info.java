package com.shadow5353.Commands;

import com.shadow5353.StaffNotes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Info extends StaffCommand{

    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("staffnotes.info")) {
            PluginDescriptionFile pdf = StaffNotes.getPlugin().getDescription();

            player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
            player.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + "Staff Notes");
            player.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdf.getVersion());
            player.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + "shadow5353");
            player.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.YELLOW + pdf.getDescription());
            player.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.YELLOW + "https://shadow5353.com/plugins/staffnotes/");
            player.sendMessage(ChatColor.GOLD + "Jenkins: " + ChatColor.YELLOW + "http://dev.shadow5353.com/staffnotes/");
            player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
        }
    }

    public Info() {
        super("Show information about the plugin!", "","info");
    }
}
