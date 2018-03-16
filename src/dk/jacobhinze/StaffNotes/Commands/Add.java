package dk.jacobhinze.StaffNotes.Commands;

import dk.jacobhinze.StaffNotes.Managers.MessageManager;
import dk.jacobhinze.StaffNotes.Managers.NoteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 16-03-2018.
 */
public class Add extends StaffCommand {
    private MessageManager message = new MessageManager();
    private NoteManager noteManager = new NoteManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.add"))) {
            message.noPermission(p);
        } else {
            if (!(args.length >= 2)) {
                message.error(p, "Usage: " + ChatColor.GOLD + "/sn add <player> <message>");
            } else {
                String note = "";

                for (int i = 1; i < args.length; i++) {
                    String arg = args[i] + " ";
                    note = note + arg;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                message.info(p, "Player: " + p.getDisplayName());
                message.info(p, "Target: " + target.getName());
                message.info(p, "Note: " + note);

                noteManager.addNote(target, p, note);
            }
        }
    }

    public Add() {
        super("Add a note onto a player", "<Player> <Note>", "add");
    }
}
