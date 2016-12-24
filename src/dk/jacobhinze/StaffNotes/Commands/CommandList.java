package dk.jacobhinze.StaffNotes.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 24-12-2016.
 */
public class CommandList {

    public static void showList(Player player) {
        commandConstructor(player, "", "Show a list of commands!");
        commandConstructor(player, "info", "Shows info about the plugin!");
        commandConstructor(player, "add [Player]", "Add a note onto a player!");
        commandConstructor(player, "show [Player]", "Show all notes on a player!");
        commandConstructor(player, "remove [Player] [NoteID]", "Remove a note on a player!");
        commandConstructor(player, "removeall [Player]", "Remove all notes on a player!");
    }

    private static void commandConstructor(Player player, String name, String description) {
        player.sendMessage(ChatColor.GOLD + "/" + "staffnotes" + name + ChatColor.BLACK + " : " + ChatColor.YELLOW + description);
    }
}
