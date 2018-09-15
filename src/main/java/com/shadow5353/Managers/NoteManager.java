package com.shadow5353.Managers;

import com.shadow5353.FlatSaving;
import com.shadow5353.MySQL;
import com.shadow5353.Note;
import com.shadow5353.StaffNotes;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jacob on 24-12-2016.
 */
class SkullInfo {
    public String name;
    public ItemStack skull;

    public SkullInfo(String name, ItemStack skull) {
        this.name = name;
        this.skull = skull;
    }
}

public class NoteManager {
    private MessageManager msg = new MessageManager();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public NoteManager() {
    }

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
                    MySQL.getInstance().getStatement().executeUpdate("INSERT INTO players (fldUUID, fldNote, fldAdmin, fldTimeStamp) " +
                            "VALUES ('" + targetUUID + "', '" + note + "', '" + adminUUID + "', '" + timestamp + "')");
                    msg.good(admin, "Note on " + target.getName() + " have been added!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {
                String date = timestamp.toString();

                FlatSaving.getInstance().saveNote(note, targetUUID, adminUUID, date);
                msg.good(admin, "Note on " + target.getName() + " have been added!");
            }
        }
    }

    public void showPlayers(Player player) {
        ArrayList<OfflinePlayer> players = getPlayersWithNotes();
        ArrayList<SkullInfo> skulls = new ArrayList<SkullInfo>();

        for (OfflinePlayer p : players) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(p.getName());
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + p.getName());
            skull.setItemMeta(meta);

            skulls.add(new SkullInfo(p.getName(), skull));
        }

        IconMenu menu = new IconMenu("Players with notes", 36, new IconMenu.OptionClickEventHandler() {
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                event.getPlayer().performCommand("sn show " + event.getName());
                event.setWillClose(true);
            }
        }, StaffNotes.getPlugin());

        for (int i = 0; i < skulls.size(); i++) {
            for (SkullInfo skull : skulls) {
                menu.setOption(i, skull.skull, skull.name, "Get all notes about " + skull.name);
            }
        }

        menu.open(player);

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
                    ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + targetUUID + "'");

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
                boolean found = false;

                for (Note note : FlatSaving.getInstance().getNotes()) {
                    if (targetUUID.equals(note.getPlayerUUID())) {
                        found = true;

                        int id = note.getId();
                        String noteMessage = note.getNote();
                        UUID adminUUID = UUID.fromString(note.getAdminUUID().toString());
                        Timestamp timestamp = Timestamp.valueOf(note.getDate());

                        OfflinePlayer madeNote = Bukkit.getOfflinePlayer(adminUUID);

                        admin.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                        admin.sendMessage(ChatColor.GOLD + "Note ID: " + ChatColor.YELLOW + id);
                        admin.sendMessage(ChatColor.GOLD + "Player: " + ChatColor.YELLOW + target.getName());
                        admin.sendMessage(ChatColor.GOLD + "Note Added By: " + ChatColor.YELLOW + madeNote.getName());
                        admin.sendMessage(ChatColor.GOLD + "Date Added: " + ChatColor.YELLOW + sdf.format(timestamp));
                        admin.sendMessage(ChatColor.GOLD + "Note: " + ChatColor.YELLOW + noteMessage);
                    }
                }

                if (!(found)) {
                    msg.error(admin, "There were no notes found on " + target.getName() + "!");
                }
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
                    ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players WHERE fldID = " + noteID + " AND fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        msg.error(admin, "This note do not exists on " + target.getName() + "!");
                    } else {
                        MySQL.getInstance().getStatement().executeUpdate("DELETE FROM players WHERE fldID = " + noteID + " AND fldUUID = '" + targetUUID + "'");
                        msg.good(admin, "Note " + noteID + " was removed from " + target.getName() + "!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {
                if (FlatSaving.getInstance().removeNote(targetUUID, noteID)) {
                    msg.good(admin, "Note " + noteID + " was removed from " + target.getName() + "!");
                } else {
                    msg.error(admin, "This note do not exists on " + target.getName() + "!");
                }
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
                    ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        msg.error(admin, target.getName() + " do not have any notes!");
                    } else {
                        MySQL.getInstance().getStatement().executeUpdate("DELETE FROM players WHERE fldUUID = '" + targetUUID + "'");

                        msg.good(admin, "All notes have been removed from " + target.getName() + "!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {
                if (FlatSaving.getInstance().removeAllNotes(targetUUID)) {
                    msg.good(admin, "All notes have been removed from " + target.getName() + "!");
                } else {
                    msg.error(admin, target.getName() + " do not have any notes!");
                }
            }
        }
    }

    /**
     * Check if the player has a note
     *
     * @param player the player for checking
     * @return if the player has a note
     */
    public boolean hasNote(Player player) {
        UUID playerUUID = player.getUniqueId();

        if (hasMySQLSave()) {

            try {
                ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + playerUUID + "'");

                if (result.next()) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (hasFileSave()) {
            return FlatSaving.getInstance().hasNote(playerUUID);
        }
        return false;
    }

    /**
     * Reset the files
     *
     * @param player
     */
    public void reset(Player player) {
        if (hasMySQLSave()) {

            try {
                MySQL.getInstance().getStatement().executeUpdate("DROP TABLE players");

                MySQL.getInstance().startUp();

                msg.good(player, "Staff Notes have been reset!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (hasFileSave()) {
            FlatSaving.getInstance().reset();

            msg.good(player, "Staff Notes have been reset!");
        }
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

    private ArrayList<OfflinePlayer> getPlayersWithNotes() {
        ArrayList<OfflinePlayer> players = new ArrayList<OfflinePlayer>();

        if (hasFileSave()) {
            for (Note note : FlatSaving.getInstance().getNotes()) {
                UUID playerUUID = note.getPlayerUUID();
                OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);
                players.add(player);
            }
        } else if (hasMySQLSave()) {
            try {
                ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players");

                do {
                    UUID playerUUID = UUID.fromString(result.getString("fldUUID"));

                    OfflinePlayer player = Bukkit.getOfflinePlayer(playerUUID);

                    players.add(player);
                } while (result.next());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return players;
    }
}
