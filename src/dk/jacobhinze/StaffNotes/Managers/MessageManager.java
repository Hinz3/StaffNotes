package dk.jacobhinze.StaffNotes.Managers;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by Jacob on 23-12-2016.
 */
public class MessageManager {

    public MessageManager() { }

    private String prefix = ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Staff Notes" + ChatColor.GOLD + "] ";

    public void info(CommandSender s, String msg) {
        msg(s, ChatColor.YELLOW, msg);
    }

    public void command(CommandSender s, String msg) {
        msg(s, ChatColor.GOLD, msg);
    }

    public void error(CommandSender s, String msg) {
        msg(s, ChatColor.RED, msg);
    }

    public void good(CommandSender s, String msg) {
        msg(s, ChatColor.GREEN, msg);
    }

    public void noPermission(CommandSender s) {
        msg(s, ChatColor.DARK_RED, "You do not have permission!");
    }

    private void msg(CommandSender s, ChatColor color, String msg) {
        s.sendMessage(prefix + color + msg);
    }
}