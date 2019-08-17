package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.Managers.NoteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 16-03-2018.
 */
public class Add extends StaffCommand {
    private NoteManager noteManager = new NoteManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.add"))) {
            MessageManager.noPermission(p);
        } else {
            if (!(args.length >= 2)) {
                MessageManager.error(p, "Usage: " + ChatColor.GOLD + "/sn add <player> <message>");
            } else {
                String note = "";

                for (int i = 1; i < args.length; i++) {
                    String arg = args[i] + " ";
                    note = note + arg;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                noteManager.addNote(target, p, note);
            }
        }
    }

    public Add() {
        super("Add a note onto a player", "<Player> <Note>", "add");
    }
}
