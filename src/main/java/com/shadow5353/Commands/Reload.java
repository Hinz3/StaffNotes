package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.StaffNotes;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 17-03-2018.
 */
public class Reload extends StaffCommand {
    MessageManager message = new MessageManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.reload"))) {
            message.noPermission(p);
        } else {
            StaffNotes.getPlugin().reloadConfig();
            message.good(p, "Config have been reloaded!");
        }
    }

    public Reload() {
        super("Reload the config.yml","","reload");
    }
}
