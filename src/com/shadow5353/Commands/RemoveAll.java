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
public class RemoveAll extends StaffCommand {
    private NoteManager noteManager = new NoteManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.removeall"))) {
            MessageManager.noPermission(p);
        } else {
            if (!(args.length == 1)) {
                MessageManager.error(p, "Usage: " + ChatColor.GOLD + "/sn removeall [Player]");
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                noteManager.removeAll(target, p);
            }
        }
    }

    public RemoveAll() {
        super("Remove all notes from a player", "<Player>", "removeall");
    }
}
