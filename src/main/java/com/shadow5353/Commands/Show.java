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
public class Show extends StaffCommand {
    private MessageManager message = new MessageManager();
    private NoteManager noteManager = new NoteManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.show"))) {
            message.noPermission(p);
        } else {
            if (!(args.length == 1)) {
                message.error(p, "Usage: " + ChatColor.GOLD + "/staffnotes show [Player]");
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

                noteManager.showNotes(target, p);
            }
        }
    }

    public Show() {
        super("Show a list of notes on a player","<Player>", "show");
    }
}
