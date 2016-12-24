package dk.jacobhinze.StaffNotes;

import dk.jacobhinze.StaffNotes.Commands.CommandList;
import dk.jacobhinze.StaffNotes.Managers.MessageManager;
import dk.jacobhinze.StaffNotes.Managers.NoteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;

/**
 * Created by Jacob on 23-12-2016.
 */
public class StaffNotes extends JavaPlugin {

    private Connection connection;
    private String host, username, password, database;
    private int port;
    private Statement statement;
    private MessageManager msg = new MessageManager();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        host = getConfig().getString("host");
        username = getConfig().getString("password");
        password = getConfig().getString("password");
        database = getConfig().getString("database");
        port = getConfig().getInt("port");

        try {
            openConnection();
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        NoteManager noteManager = new NoteManager(statement, msg);

        if(!(sender instanceof Player)) {
            msg.error(sender, "Only players can use this Staff Notes!");
            return true;
        }

        final Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("staffnotes")) {

            if(args.length == 0) {
                if(havePermission(player, "list")) {
                    CommandList.showList(player);
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("add")) {
                if(havePermission(player, "add")) {
                    if (!(args.length >= 3)) {
                        msg.error(player, "Usage: " + ChatColor.GOLD + "/staffnotes add [Player] [Note]");
                        return true;
                    } else {
                        String note = "";

                        for (int i = 3; i < args.length; i++) {
                            String arg = args[i] + "";
                            note = note + arg;
                        }

                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

                        noteManager.addNote(offlinePlayer, player, note);

                        return true;
                    }
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("show")) {
                if(!(args.length == 2)) {
                    msg.error(player, "Usage: " + ChatColor.GOLD + "/staffnotes show [Player]");
                    return true;
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

                    noteManager.showNotes(offlinePlayer, player);

                    return true;
                }
            }
        }

//        if(cmd.getName().equalsIgnoreCase("ping")) {
//            try {
//                statement.executeUpdate("INSERT INTO players (fldUUID, fldNotes) VALUES (123, 'Test')");
//
//                msg.info(player, "Added to database!");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if(cmd.getName().equalsIgnoreCase("pong")) {
//            try {
//                ResultSet result = statement.executeQuery("SELECT * FROM players WHERE fldUUID = 123");
//
//                while (result.next()) {
//                    String note = result.getString("fldNotes");
//                    msg.info(player, "Note: " + note);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }

        return true;
    }

    private boolean havePermission(Player player, String permission) {
        if(player.hasPermission("staffnotes." + permission)) {
            return true;
        } else {
            msg.noPermission(player);
            return false;
        }
    }

    @Override
    public void onDisable() {

    }

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }
}
