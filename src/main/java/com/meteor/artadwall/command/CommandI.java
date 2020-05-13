package com.meteor.artadwall.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandI {
    boolean excute(CommandSender sender, Command cmd,String arg,String[] args);
}
