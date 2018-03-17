package com.shadow5353.Commands;

import com.shadow5353.Managers.MessageManager;
import com.shadow5353.Managers.NoteManager;
import org.bukkit.entity.Player;

/**
 * Created by Jacob on 17-03-2018.
 */
public class Reset extends StaffCommand {
    private MessageManager message = new MessageManager();
    private NoteManager noteManager = new NoteManager();

    @Override
    public void onCommand(Player p, String[] args) {
        if (!(p.hasPermission("staffnotes.reset"))) {
            message.noPermission(p);
        } else {
            noteManager.reset(p);
        }
    }

    public Reset() {
        super("Remove all notes from the database", "", "reset");
    }
}
