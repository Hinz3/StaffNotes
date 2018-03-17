package com.shadow5353.Commands;

import org.bukkit.entity.Player;

public abstract class StaffCommand {

    public abstract void onCommand(Player p, String[] args);

    private String message, usage;
    private String[] aliases;

    public StaffCommand(String message, String usage, String... aliases) {
        this.message = message;
        this.usage = usage;
        this.aliases = aliases;
    }

    public final String getMessage() {
        return message;
    }

    public final String getUsage() {
        return usage;
    }

    public final String[] getAliases() {
        return aliases;
    }
}
