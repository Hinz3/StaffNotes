package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.Managers.NoteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 17-03-2018.
 */
public class Remove extends StaffCommand {
    private MessageManager message = new MessageManager();
    private NoteManager noteManager = new NoteManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.remove"))) {
            message.noPermission(p);
        } else {
            if (!(args.length == 2)) {
                message.error(p, "Usage: " + ChatColor.GOLD + "/sn remove <Player> <ID>");
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                int noteID = Integer.parseInt(args[2]);

                noteManager.removeNote(target, p, noteID);
            }
        }
    }

    public Remove() {
        super("Remove a note from a player", "<Player> <ID>", "remove");
    }
}
