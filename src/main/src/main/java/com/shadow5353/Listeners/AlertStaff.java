package com.shadow5353.Listeners;

import com.shadow5353.Managers.NoteManager;
import com.shadow5353.StaffNotes;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Jacob on 16-03-2018.
 */
public class AlertStaff implements Listener {
    private NoteManager noteManager = new NoteManager();

    public void onJoinEvent(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        if(noteManager.hasNote(player)) {
            for (Player admin : StaffNotes.getStaff()) {
                noteManager.alert(player, admin);
            }
        }
    }
}
