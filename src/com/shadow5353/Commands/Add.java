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

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.add"))) {
            MessageManager.noPermission(p);
        } else {
            if (!(args.length >= 2)) {
                MessageManager.error(p, MessageManager.getMessageConfig().getString("commands.add.usage"));
            } else {
                String note = "";

                for (int i = 1; i < args.length; i++) {
                    String arg = args[i] + " ";
                    note = note + arg;
                }

                NoteManager.addNote(args[0], p, note);
            }
        }
    }

    public Add() {
        super(MessageManager.getMessageConfig().getString("commands.add.description"), "<Player> <Note>", "add");
    }
}
