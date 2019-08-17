package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.Managers.NoteManager;
import org.bukkit.*;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 17-03-2018.
 */
public class Show extends StaffCommand {
    private NoteManager noteManager = new NoteManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.show"))) {
            MessageManager.noPermission(p);
        } else {
            if (!(args.length == 1)) {
                MessageManager.sendMessage(p, MessageManager.getMessageConfig().getString("commands.show.usage"), true);
            } else {
                NoteManager.showNotes(args[0], p);
            }
        }
    }

    public Show() {
        super(MessageManager.getMessageConfig().getString("commands.show.description"),"<Player>", "show");
    }
}
