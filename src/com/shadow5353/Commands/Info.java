package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Info extends StaffCommand{
    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("staffnotes.info")) {

            String name = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.info.terms.name"));
            String version = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.info.terms.version"));
            String author = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.info.terms.author"));
            String description = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.info.terms.description"));
            String website = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.info.terms.website"));

            MessageManager.sendMessage(player, ChatColor.GOLD + "---------------------------------------------", false);
            if (name != null) MessageManager.sendMessage(player, name, false);
            if (version != null) MessageManager.sendMessage(player, version, false);
            if (author != null) MessageManager.sendMessage(player, author, false);
            if (description != null) MessageManager.sendMessage(player, description, false);
            if (website != null) MessageManager.sendMessage(player, website, false);
            MessageManager.sendMessage(player, ChatColor.GOLD + "---------------------------------------------", false);
        } else {
            MessageManager.noPermission(player);
        }
    }

    public Info() {
        super(MessageManager.getMessageConfig().getString("commands.info.description"), "","info");
    }
}
