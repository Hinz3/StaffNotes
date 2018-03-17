package com.shadow5353.Managers;

import com.shadow5353.Commands.Add;
import com.shadow5353.Commands.Info;
import com.shadow5353.Commands.Reload;
import com.shadow5353.Commands.StaffCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class CommandManager implements CommandExecutor {
    private MessageManager message = new MessageManager();

    private ArrayList<StaffCommand> cmds = new ArrayList<StaffCommand>();

    public CommandManager() {
        cmds.add(new Info());
        cmds.add(new Add());
        cmds.add(new Reload());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            message.error(sender, "Only players can use com.shadow5353.StaffNotes!");
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("staffnotes")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                message.command(p, "/sn" + ChatColor.BLACK + " : " + ChatColor.YELLOW + "Show a list of commands");
                for (StaffCommand mc : cmds) message.command(p, "/sn " + aliases(mc) + " " + mc.getUsage() + ChatColor.BLACK + " : " + ChatColor.YELLOW + mc.getMessage());
                p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                return true;
            }

            StaffCommand c = getCommand(args[0]);

            if (c == null) {
                message.error(p,"That command doesn't exist!");
                return true;
            }

            Vector<String> a = new Vector<String>(Arrays.asList(args));
            a.remove(0);
            args = a.toArray(new String[a.size()]);

            c.onCommand(p, args);

            return true;
        }
        return true;
    }

    private String aliases(StaffCommand cmd) {
        String fin = "";

        for (String a : cmd.getAliases()) {
            fin += a + " | ";
        }

        return fin.substring(0, fin.lastIndexOf(" | "));
    }

    private StaffCommand getCommand(String name) {
        for (StaffCommand cmd : cmds) {
            if (cmd.getClass().getSimpleName().equalsIgnoreCase(name)) return cmd;
            for (String alias : cmd.getAliases()) if (name.equalsIgnoreCase(alias)) return cmd;
        }
        return null;
    }
}