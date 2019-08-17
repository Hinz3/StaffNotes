package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.Managers.NoteManager;
import org.bukkit.entity.Player;

public class Gui extends StaffCommand {


    @Override
    public void onCommand(Player player, String[] args) {
        if (player.hasPermission("staffnotes.gui")) {
            NoteManager.showPlayers(player);
        }
        else MessageManager.noPermission(player);
    }

    public Gui() {
        super(MessageManager.getMessageConfig().getString("commands.gui.description"), "", "gui");
    }
}
