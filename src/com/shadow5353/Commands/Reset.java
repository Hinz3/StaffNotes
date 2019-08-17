package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.Managers.NoteManager;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 17-03-2018.
 */
public class Reset extends StaffCommand {

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.reset"))) {
            MessageManager.noPermission(p);
        } else {
            NoteManager.reset(p);
        }
    }

    public Reset() {
        super(MessageManager.getMessageConfig().getString("commands.reset.description"), "", "reset");
    }
}
