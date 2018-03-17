package com.shadow5353.Managers;

import com.shadow5353.MySQL;
import com.shadow5353.StaffNotes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
    private MessageManager msg = new MessageManager();
    private MySQL mySQL = new MySQL();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public NoteManager() {}

    private boolean hasMySQLSave() {
        return StaffNotes.getPlugin().getConfig().get("savingType").equals("mysql");
    }

    private boolean hasFileSave() {
        return StaffNotes.getPlugin().getConfig().get("savingType").equals("file");
    }

    /**
     * Add a new note to the database
     *
     * @param target the player that will get the note
     * @param admin  the admin that make the note
     * @param note   the note that will be registered in the database
     */
    public void addNote(OfflinePlayer target, Player admin, String note) {
        if (playedBefore(target, admin)) {
            UUID targetUUID = target.getUniqueId();
            UUID adminUUID = admin.getUniqueId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            if (hasMySQLSave()) {
                try {
                    mySQL.getStatement().executeUpdate("INSERT INTO players (fldUUID, fldNote, fldAdmin, fldTimeStamp) " +
                            "VALUES ('" + targetUUID + "', '" + note + "', '" + adminUUID + "', '" + timestamp + "')");
                    msg.good(admin, target.getName() + " note have been added!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {

            }
        }
    }

    /**
     * Show a list of notes for a player
     *
     * @param target the given player
     * @param admin  the admin asking for the list
     */
    public void showNotes(OfflinePlayer target, Player admin) {
        if (playedBefore(target, admin)) {
            UUID targetUUID = target.getUniqueId();

            if (hasMySQLSave()) {

                try {
                    ResultSet result = mySQL.getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        msg.error(admin, target.getName() + " do not have any notes!");
                    } else {

                        msg.info(admin, "Here is the list of notes on " + target.getName() + "!");

                        do {
                            int id = result.getInt("fldID");
                            String note = result.getString("fldNote");
                            UUID adminUUID = UUID.fromString(result.getString("fldAdmin"));
                            Timestamp timestamp = result.getTimestamp("fldTimeStamp");

                            OfflinePlayer madeNote = Bukkit.getOfflinePlayer(adminUUID);

                            admin.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                            admin.sendMessage(ChatColor.GOLD + "Note ID: " + ChatColor.YELLOW + id);
                            admin.sendMessage(ChatColor.GOLD + "Player: " + ChatColor.YELLOW + target.getName());
                            admin.sendMessage(ChatColor.GOLD + "Note Added By: " + ChatColor.YELLOW + madeNote.getName());
                            admin.sendMessage(ChatColor.GOLD + "Date Added: " + ChatColor.YELLOW + sdf.format(timestamp));
                            admin.sendMessage(ChatColor.GOLD + "Note: " + ChatColor.YELLOW + note);
                        } while (result.next());
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {

            }
        }
    }

    /**
     * Remove a note from a player
     *
     * @param target the player that will get a note removed
     * @param admin  the admin that do it
     * @param noteID the id of the note
     */
    public void removeNote(OfflinePlayer target, Player admin, int noteID) {
        if (playedBefore(target, admin)) {
            UUID targetUUID = target.getUniqueId();

            if (hasMySQLSave()) {

                try {
                    ResultSet result = mySQL.getStatement().executeQuery("SELECT * FROM players WHERE fldID = " + noteID + " AND fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        msg.error(admin, "This note do not exists on " + target.getName() + "!");
                    } else {
                        mySQL.getStatement().executeUpdate("DELETE FROM players WHERE fldID = " + noteID + " AND fldUUID = '" + targetUUID + "'");
                        msg.good(admin, "Note " + noteID + " was removed from " + target.getName() + "!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {

            }
        }
    }

    /**
     * Remove all notes from a player
     *
     * @param target the player that will get notes remove
     * @param admin  the admin that want to remove all notes
     */
    public void removeAll(OfflinePlayer target, Player admin) {
        if (playedBefore(target, admin)) {
            UUID targetUUID = target.getUniqueId();

            if (hasMySQLSave()) {

                try {
                    ResultSet result = mySQL.getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        msg.error(admin, target.getName() + " do not have any notes!");
                    } else {
                        mySQL.getStatement().executeUpdate("DELETE FROM players WHERE fldUUID = '" + targetUUID + "'");

                        msg.good(admin, "All notes have been removed from " + target.getName() + "!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {

            }
        }
    }

    public void alert(Player player, Player admin)
    {
        UUID playerUUID = player.getUniqueId();

        if (hasMySQLSave()) {

            try {
                ResultSet result = mySQL.getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + playerUUID + "'");

                if (result.next()) {
                    msg.alert(admin, player.getDisplayName() + " seems to have staff notes. The most recent is displayed.");

                    String note = result.getString("fldNote");
                    UUID adminUUID = UUID.fromString(result.getString("fldAdmin"));
                    Timestamp timestamp = result.getTimestamp("fldTimeStamp");
                    OfflinePlayer madeNote = Bukkit.getOfflinePlayer(adminUUID);

                    admin.sendMessage(ChatColor.GOLD + "Note: " + ChatColor.YELLOW + note);
                    admin.sendMessage(ChatColor.YELLOW + "This note was created " + timestamp + " and was made by: " + madeNote);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (hasFileSave()) {

        }
    }

    public boolean hasNote(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (hasMySQLSave()) {

            try {
                ResultSet result = mySQL.getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + playerUUID + "'");

                if (result.next()) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Check if a offline player have played before
     *
     * @param target the offline player
     * @param player the player that get the error message
     * @return if the player have played before
     */
    private boolean playedBefore(OfflinePlayer target, Player player) {
        if (target.hasPlayedBefore()) {
            return true;
        } else {
            msg.error(player, target.getName() + " has never played before on this server!");
            return false;
        }
    }
}
