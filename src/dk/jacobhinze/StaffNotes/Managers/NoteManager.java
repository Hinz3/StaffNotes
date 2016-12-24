package dk.jacobhinze.StaffNotes.Managers;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.net.StandardSocketOptions;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created by Jacob on 24-12-2016.
 */
public class NoteManager {
    private Statement statement;
    private MessageManager msg;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public NoteManager(Statement statement, MessageManager messageManager) {
        this.statement = statement;
        this.msg = messageManager;
    }

    public void addNote(OfflinePlayer target, Player admin, String note) {
        if (target.hasPlayedBefore()) {
            UUID targetUUID = target.getUniqueId();
            UUID adminUUID = admin.getUniqueId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            try {
                statement.executeUpdate("INSERT INTO players (fldUUID, fldNote, fldAdmin, fldTimeStamp) " +
                        "VALUES ('" + targetUUID + "', '" + note + "', '" + adminUUID + "', '" + timestamp + "')");
                msg.good(admin, target.getName() + " note have been added to the database!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            msg.error(admin, target.getName() + " has never played before on this server!");
        }
    }

    public void showNotes(OfflinePlayer target, Player admin) {
        if (target.hasPlayedBefore()) {
            UUID targetUUID = target.getUniqueId();

            try {
                ResultSet result = statement.executeQuery("SELECT * FROM players WHERE fldUUID = '" + targetUUID + "'");

                msg.info(admin, "Here is the list of notes on " + target.getName() + "!");

                while (result.next()) {
                    int id = result.getInt("fldID");
                    String note = result.getString("fldNote");
                    String adminUUID = result.getString("fldAdmin");
                    Timestamp timestamp = result.getTimestamp("fldTimeStamp");

                    OfflinePlayer madeNote = Bukkit.getOfflinePlayer(adminUUID);

                    admin.sendMessage(ChatColor.GOLD + "---------------------------");
                    admin.sendMessage(ChatColor.GOLD + "Note ID: " + ChatColor.YELLOW + id);
                    admin.sendMessage(ChatColor.GOLD + "Admin: " + ChatColor.YELLOW + madeNote.getName());
                    admin.sendMessage(ChatColor.GOLD + "Date: " + ChatColor.YELLOW + sdf.format(timestamp));
                    admin.sendMessage(ChatColor.GOLD + "Note: " + ChatColor.YELLOW + note);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            msg.error(admin, target.getName() + " has never played before on this server!");
        }
    }
}
