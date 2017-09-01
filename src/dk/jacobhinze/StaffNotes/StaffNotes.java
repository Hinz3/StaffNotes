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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Jacob on 23-12-2016.
 */
public class StaffNotes extends JavaPlugin implements Listener{
    private Connection connection;
    private String host, username, password, database;
    private int port;
    private Statement statement;
    private MessageManager msg = new MessageManager();
    private NoteManager noteManager;
    private ArrayList<Player> staff = new ArrayList();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        host = getConfig().getString("host");
        username = getConfig().getString("username");
        password = getConfig().getString("password");
        database = getConfig().getString("database");
        port = getConfig().getInt("port");

        try {
            openConnection();
            statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `players` (" +
                    "`fldID` int(255) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "`fldUUID` varchar(255) NOT NULL," +
                    "`fldNote` text CHARACTER SET utf16 COLLATE utf16_danish_ci NOT NULL," +
                    "`fldAdmin` varchar(255) NOT NULL," +
                    "`fldTimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1\n;");


            noteManager = new NoteManager(statement, msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            msg.error(sender, "Only players can use this Staff Notes!");
            return true;
        }

        final Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("staffnotes")) {

            if (args.length == 0) {
                if (havePermission(player, "list")) {
                    CommandList.showList(player);
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("add")) {
                if (havePermission(player, "add")) {
                    if (!(args.length >= 3)) {
                        msg.error(player, "Usage: " + ChatColor.GOLD + "/staffnotes add [Player] [Note]");
                    } else {
                        String note = "";

                        for (int i = 2; i < args.length; i++) {
                            String arg = args[i] + " ";
                            note = note + arg;
                        }

                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                        noteManager.addNote(target, player, note);

                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("show")) {
                if (havePermission(player, "show")) {
                    if (!(args.length == 2)) {
                        msg.error(player, "Usage: " + ChatColor.GOLD + "/staffnotes show [Player]");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                        noteManager.showNotes(target, player);

                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (havePermission(player, "remove")) {
                    if (!(args.length == 3)) {
                        msg.error(player, "Usage: " + ChatColor.YELLOW + "/staffnotes remove [Player] [NoteID]");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                        int noteID = Integer.parseInt(args[2]);

                        noteManager.removeNote(target, player, noteID);

                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("removeall")) {
                if (havePermission(player, "removeall")) {
                    if (!(args.length == 2)) {
                        msg.error(player, "Usage: " + ChatColor.YELLOW + "/staffnotes removeall [Player]");
                    } else {
                        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                        noteManager.removeAll(target, player);
                    }
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                if (havePermission(player, "info")) {
                    PluginDescriptionFile pdf = this.getDescription();

                    player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    player.sendMessage(ChatColor.GOLD + "Name: " + ChatColor.YELLOW + "Staff Notes");
                    player.sendMessage(ChatColor.GOLD + "Version: " + ChatColor.YELLOW + pdf.getVersion());
                    player.sendMessage(ChatColor.GOLD + "Author: " + ChatColor.YELLOW + "shadow5353");
                    player.sendMessage(ChatColor.GOLD + "Description: " + ChatColor.YELLOW + pdf.getDescription());
                    player.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                }
                return true;
            }
        }

        return true;
    }

    private boolean havePermission(Player player, String permission) {
        if (player.hasPermission("staffnotes." + permission)) {
            return true;
        } else {
            msg.noPermission(player);
            return false;
        }
    }

    public void onJoinEvent(PlayerJoinEvent e)
    {
        Player player = e.getPlayer();

        if(noteManager.hasNote(player)) {
            getStaff();

            for (Player admin : staff) {
                noteManager.alert(player, admin);
            }
        }
    }

    private void getStaff()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            if (player.hasPermission("staffnotes.alert"))
            {
                staff.add(player);
            }
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
