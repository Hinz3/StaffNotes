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

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.remove"))) {
            MessageManager.noPermission(p);
        } else {
            if (!(args.length == 2)) {
                MessageManager.error(p, MessageManager.getMessageConfig().getString("commands.remove.usage"));
            } else {

                int noteID = Integer.parseInt(args[1]);

                NoteManager.removeNote(args[0], p, noteID);
            }
        }
    }

    public Remove() {
        super(MessageManager.getMessageConfig().getString("commands.remove.description"), "<Player> <ID>", "remove");
    }
}
