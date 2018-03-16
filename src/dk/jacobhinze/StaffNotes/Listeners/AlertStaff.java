package dk.jacobhinze.StaffNotes.Listeners;

import dk.jacobhinze.StaffNotes.Managers.NoteManager;
import dk.jacobhinze.StaffNotes.StaffNotes;
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
