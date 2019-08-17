package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.Managers.NoteManager;
import com.shadow5353.Note;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 17-03-2018.
 */
public class RemoveAll extends StaffCommand {

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.removeall"))) {
            MessageManager.noPermission(p);
        } else {
            if (!(args.length == 1)) {
                MessageManager.error(p, MessageManager.getMessageConfig().getString("commands.removeall.usage"));
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                NoteManager.removeAll(args[0], p);
            }
        }
    }

    public RemoveAll() {
        super(MessageManager.getMessageConfig().getString("commands.removeall.description"), "<Player>", "removeall");
    }
}
