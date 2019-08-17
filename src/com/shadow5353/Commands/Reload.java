package com.shadow5353.Commands;

import com.shadow5353.FlatSaving;
import com.shadow5353.Managers.MessageManager;
import com.shadow5353.StaffNotes;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 17-03-2018.
 */
public class Reload extends StaffCommand {

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.reload"))) {
            MessageManager.noPermission(p);
        } else {
            MessageManager.reloadMessageFile();
            StaffNotes.getPlugin().reloadConfig();

            if (StaffNotes.getPlugin().getConfig().get("savingType").equals("file"))
                FlatSaving.getInstance().setupNotes();

            MessageManager.good(p, MessageManager.getMessageConfig().getString("commands.reload.success"));
        }
    }

    public Reload() {
        super(MessageManager.getMessageConfig().getString("commands.reload.description"),"","reload");
    }
}
