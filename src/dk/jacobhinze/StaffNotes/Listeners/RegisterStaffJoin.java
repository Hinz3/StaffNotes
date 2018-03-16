package dk.jacobhinze.StaffNotes.Listeners;

import dk.jacobhinze.StaffNotes.Managers.MessageManager;
import dk.jacobhinze.StaffNotes.StaffNotes;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Jacob on 16-03-2018.
 */
public class RegisterStaffJoin implements Listener {
    MessageManager message = new MessageManager();

    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        if (player.hasPermission("staffnote.staff")) {
            StaffNotes.addStaff(player);

            if (StaffNotes.getPlugin().getConfig().get("alert") == "true") {
                message.info(player, "You get this message to inform you are staff on this server and will recive staff notes alerts when players join!");
            }
        }
    }
}
