package dk.jacobhinze.StaffNotes.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 24-12-2016.
 */
public class CommandList {

    public static void showList(Player player) {
        player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
        commandConstructor(player, "staffnotes", "Show a list of commands!");
        commandConstructor(player, "staffnotes info", "Show information about the plugin!");
        commandConstructor(player, "staffnotes add [Player] [Note]", "Add a note onto a player!");
        commandConstructor(player, "staffnotes show [Player]", "Show all notes on a player!");
        commandConstructor(player, "staffnotes remove [Player] [NoteID]", "Remove a note on a player!");
        commandConstructor(player, "staffnotes removeall [Player]", "Remove all notes on a player!");
        player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
    }

    private static void commandConstructor(Player player, String name, String description) {
        player.sendMessage(ChatColor.GOLD + "/" + name + ChatColor.BLACK + " : " + ChatColor.YELLOW + description);
    }
}
