package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.StaffNotes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Info extends StaffCommand{
    private MessageManager message = MessageManager.getMessageManager();

    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("staffnotes.info")) {
            PluginDescriptionFile pdf = StaffNotes.getPlugin().getDescription();

            player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
            player.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + pdf.getName());
            player.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdf.getVersion());
            player.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + pdf.getAuthors());
            player.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.YELLOW + pdf.getDescription());
//            player.sendMessage(ChatColor.GOLD + "Website: " + ChatColor.YELLOW + "https://hinz3.dk/plugins/staffnotes/");
            player.sendMessage(ChatColor.GOLD + "Jenkins: " + ChatColor.YELLOW + "https://jenkins.hinz3.dk/job/StaffNotes/");
            player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
        } else {
            message.noPermission(player);
        }
    }

    public Info() {
        super("Show information about the plugin!", "","info");
    }
}
