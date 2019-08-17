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
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private static boolean hasMySQLSave() {
        return StaffNotes.getPlugin().getConfig().get("savingType").equals("mysql");
    }

    private static boolean hasFileSave() {
        return StaffNotes.getPlugin().getConfig().get("savingType").equals("file");
    }

    public static void showPlayers(Player player) {
        ArrayList<OfflinePlayer> players = getPlayersWithNotes(player);
        ArrayList<SkullInfo> skulls = new ArrayList<>();

        for (OfflinePlayer p : players) {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);

            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(p);
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + p.getName());
            skull.setItemMeta(meta);

            skulls.add(new SkullInfo(p.getName(), skull));
        }

        IconMenu menu = new IconMenu("Players with notes", 54, new IconMenu.OptionClickEventHandler() {
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
     * Add a new note to the database
     *
     * @param playerName Player name of the target
     * @param admin  the admin that make the note
     * @param note   the note that will be registered in the database
     */
    public static void addNote(String playerName, Player admin, String note) {
        OfflinePlayer target = getOfflinePlayer(playerName);

        if (target != null) {
            UUID targetUUID = target.getUniqueId();
            UUID adminUUID = admin.getUniqueId();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            if (hasMySQLSave()) {
                try {
                    MySQL.getInstance().getStatement().executeUpdate("INSERT INTO players (fldUUID, fldNote, fldAdmin, fldTimeStamp) " +
                            "VALUES ('" + targetUUID + "', '" + note + "', '" + adminUUID + "', '" + timestamp + "')");
                    String msg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.add.success"), "[PLAYER]", target.getName());
                    MessageManager.sendMessage(admin, msg, true);
                } catch (SQLException e) {
                    String sqlError = MessageManager.getMessageConfig().getString("console-errors.sql");
                    MessageManager.sendMessage(admin, sqlError, true);
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {
                String date = timestamp.toString();

                FlatSaving.getInstance().saveNote(note, targetUUID, adminUUID, date);
                String msg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.add.success"), "[PLAYER]", target.getName());
                MessageManager.sendMessage(admin, msg, true);
            }
        }
    }

    /**
     * Show a list of notes for a player
     *
     * @param playerName Player name of the target
     * @param admin  the admin asking for the list
     */
    public static void showNotes(String playerName, Player admin) {
        OfflinePlayer target = getOfflinePlayer(playerName);

        if (target != null) {
            UUID targetUUID = target.getUniqueId();

            if (hasMySQLSave()) {

                try {
                    ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        String errorMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.no-notes"), "[PLAYER]", target.getName());
                        MessageManager.sendMessage(admin, errorMsg, true);
                    } else {

                        String msg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.description"), "[PLAYER]", target.getName());
                        MessageManager.sendMessage(admin, msg, true);

                        do {
                            int id = result.getInt("fldID");
                            String note = result.getString("fldNote");
                            UUID adminUUID = UUID.fromString(result.getString("fldAdmin"));
                            Timestamp timestamp = result.getTimestamp("fldTimeStamp");

                            OfflinePlayer madeNote = Bukkit.getOfflinePlayer(adminUUID);

                            String idMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.id"), "[ID]", id + "");
                            String playerMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.player"), "[PLAYER]", target.getName());
                            String addedMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.added-by"), "[PLAYER]", madeNote.getName());
                            String dateMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.date-added"), "[DATE]", sdf.format(timestamp));
                            String noteMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.note"), "[NOTE]", note);

                            MessageManager.sendMessage(admin, ChatColor.GOLD + "---------------------------------------------", false);
                            if (idMsg != null) MessageManager.sendMessage(admin, idMsg, false);
                            if (playerMsg != null) MessageManager.sendMessage(admin, playerMsg, false);
                            if (addedMsg != null) MessageManager.sendMessage(admin, addedMsg, false);
                            if (dateMsg != null) MessageManager.sendMessage(admin, dateMsg, false);
                            if (noteMsg != null) MessageManager.sendMessage(admin, noteMsg, false);
                        } while (result.next());
                    }

                } catch (SQLException e) {
                    String sqlError = MessageManager.getMessageConfig().getString("console-errors.sql");
                    MessageManager.sendMessage(admin, sqlError, true);
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {
                boolean found = false;

                String msg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.description"), "[PLAYER]", target.getName());
                MessageManager.sendMessage(admin, msg, true);

                for (Note note : FlatSaving.getInstance().getNotes()) {
                    if (targetUUID.equals(note.getPlayerUUID())) {
                        found = true;

                        int id = note.getId();
                        String noteMessage = note.getNote();
                        UUID adminUUID = UUID.fromString(note.getAdminUUID().toString());
                        Timestamp timestamp = Timestamp.valueOf(note.getDate());

                        OfflinePlayer madeNote = Bukkit.getOfflinePlayer(adminUUID);

                        String idMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.id"), "[ID]", id + "");
                        String playerMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.player"), "[PLAYER]", target.getName());
                        String addedMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.added-by"), "[PLAYER]", madeNote.getName());
                        String dateMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.date-added"), "[DATE]", sdf.format(timestamp));
                        String noteMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.found.note"), "[NOTE]", noteMessage);

                        MessageManager.sendMessage(admin, ChatColor.GOLD + "---------------------------------------------", false);
                        if (idMsg != null) MessageManager.sendMessage(admin, idMsg, false);
                        if (playerMsg != null) MessageManager.sendMessage(admin, playerMsg, false);
                        if (addedMsg != null) MessageManager.sendMessage(admin, addedMsg, false);
                        if (dateMsg != null) MessageManager.sendMessage(admin, dateMsg, false);
                        if (noteMsg != null) MessageManager.sendMessage(admin, noteMsg, false);
                    }
                }

                if (!(found)) {
                    String errorMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.show.no-notes"), "[PLAYER]", target.getName());
                    MessageManager.sendMessage(admin, errorMsg, true);
                }
            }
        }
    }

    /**
     * Remove a note from a player
     *
     * @param playerName Player name of the target
     * @param admin  the admin that do it
     * @param noteID the id of the note
     */
    public static void removeNote(String playerName, Player admin, int noteID) {
        OfflinePlayer target = getOfflinePlayer(playerName);

        if (target != null) {
            UUID targetUUID = target.getUniqueId();

            if (hasMySQLSave()) {

                try {
                    ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players WHERE fldID = " + noteID + " AND fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        String errorMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.remove.error"), "[PLAYER]", target.getName());
                        MessageManager.sendMessage(admin, errorMsg, true);
                    } else {
                        MySQL.getInstance().getStatement().executeUpdate("DELETE FROM players WHERE fldID = " + noteID + " AND fldUUID = '" + targetUUID + "'");
                        String successMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.remove.success"), "[ID]", noteID + "");
                        successMsg = MessageManager.replacePlaceholder(successMsg, "[PLAYER]", target.getName());
                        MessageManager.sendMessage(admin, successMsg, true);
                    }
                } catch (SQLException e) {
                    String sqlError = MessageManager.getMessageConfig().getString("console-errors.sql");
                    MessageManager.sendMessage(admin, sqlError, true);
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {
                if (FlatSaving.getInstance().removeNote(targetUUID, noteID)) {
                    String successMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.remove.success"), "[ID]", noteID + "");
                    successMsg = MessageManager.replacePlaceholder(successMsg, "[PLAYER]", target.getName());
                    MessageManager.sendMessage(admin, successMsg, true);
                } else {
                    String errorMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.remove.error"), "[PLAYER]", target.getName());
                    MessageManager.sendMessage(admin, errorMsg, true);
                }
            }
        }
    }

    /**
     * Remove all notes from a player
     *
     * @param playerName Player name of the target
     * @param admin  the admin that want to remove all notes
     */
    public static void removeAll(String playerName, Player admin) {
        OfflinePlayer target = getOfflinePlayer(playerName);

        if (target != null) {
            UUID targetUUID = target.getUniqueId();

            if (hasMySQLSave()) {

                try {
                    ResultSet result = MySQL.getInstance().getStatement().executeQuery("SELECT * FROM players WHERE fldUUID = '" + targetUUID + "'");

                    if (!result.next()) {
                        String errorMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.removeall.error"), "[PLAYER]", target.getName());
                        MessageManager.sendMessage(admin, errorMsg, true);
                    } else {
                        MySQL.getInstance().getStatement().executeUpdate("DELETE FROM players WHERE fldUUID = '" + targetUUID + "'");

                        String successMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.removeall.success"), "[PLAYER]", target.getName() + "");
                        MessageManager.sendMessage(admin, successMsg, true);
                    }
                } catch (SQLException e) {
                    String sqlError = MessageManager.getMessageConfig().getString("console-errors.sql");
                    MessageManager.sendMessage(admin, sqlError, true);
                    e.printStackTrace();
                }
            } else if (hasFileSave()) {
                if (FlatSaving.getInstance().removeAllNotes(targetUUID)) {
                    String successMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.removeall.success"), "[PLAYER]", target.getName() + "");
                    MessageManager.sendMessage(admin, successMsg, true);
                } else {
                    String errorMsg = MessageManager.replacePlaceholder(MessageManager.getMessageConfig().getString("commands.removeall.error"), "[PLAYER]", target.getName());
                    MessageManager.sendMessage(admin, errorMsg, true);
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
    public static boolean hasNote(Player player) {
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
                String sqlError = MessageManager.getMessageConfig().getString("console-errors.sql");
                MessageManager.sendMessage(player, sqlError, true);
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
    public static void reset(Player player) {
        if (hasMySQLSave()) {

            try {
                MySQL.getInstance().getStatement().executeUpdate("DROP TABLE players");

                MySQL.getInstance().startUp();

                String msg = MessageManager.getMessageConfig().getString("commands.reset.success");
                MessageManager.sendMessage(player, msg, true);
            } catch (SQLException e) {
                String sqlError = MessageManager.getMessageConfig().getString("console-errors.sql");
                MessageManager.sendMessage(player, sqlError, true);
                e.printStackTrace();
            }
        } else if (hasFileSave()) {
            FlatSaving.getInstance().reset();

            String msg = MessageManager.getMessageConfig().getString("commands.reset.success");
            MessageManager.sendMessage(player, msg, true);
        }
    }

    /**
     * Get an offline player
     * @param name name of the player
     * @return Offline Player
     */
    private static OfflinePlayer getOfflinePlayer(String name) {
        OfflinePlayer[] players = Bukkit.getOfflinePlayers();

        for (OfflinePlayer player : players) {
            if (player.getName().equals(name)) return player;
        }

        return null;
    }

    private static ArrayList<OfflinePlayer> getPlayersWithNotes(Player admin) {
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
                String sqlError = MessageManager.getMessageConfig().getString("console-errors.sql");
                MessageManager.sendMessage(admin, sqlError, true);
                ex.printStackTrace();
            }
        }

        return players;
    }
}
