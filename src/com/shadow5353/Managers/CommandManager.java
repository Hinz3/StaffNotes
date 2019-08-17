package com.shadow5353.Managers;

import com.shadow5353.Commands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class CommandManager implements CommandExecutor {
    private ArrayList<StaffCommand> cmds = new ArrayList<>();

    public CommandManager() {
        cmds.add(new Info());
        cmds.add(new Add());
        cmds.add(new Reload());
        cmds.add(new Show());
        cmds.add(new Remove());
        cmds.add(new RemoveAll());
        cmds.add(new Reset());
        cmds.add(new Gui());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.sendMessage(sender, MessageManager.getMessageConfig().getString("console-errors.command"), true);
            return true;
        }

        Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("staffnotes")) {
            if (!p.hasPermission("staffnotes.use")) {
                MessageManager.noPermission(p);
            } else {

                if (args.length == 0) {
                    p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    MessageManager.command(p, "/sn" + ChatColor.BLACK + " : " + MessageManager.getMessageConfig().getString("commands.default.description"));
                    for (StaffCommand mc : cmds)
                        MessageManager.command(p, "/sn " + aliases(mc) + " " + mc.getUsage() + ChatColor.BLACK + " : " + ChatColor.YELLOW + mc.getMessage());
                    p.sendMessage(ChatColor.GOLD + "---------------------------------------------");
                    return true;
                }

                StaffCommand c = getCommand(args[0]);

                if (c == null) {
                    MessageManager.error(p, MessageManager.getMessageConfig().getString("commands.default.dont-exist"));
                    return true;
                }

                Vector<String> a = new Vector<>(Arrays.asList(args));
                a.remove(0);
                args = a.toArray(new String[a.size()]);

                c.onCommand(p, args);

            }
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
