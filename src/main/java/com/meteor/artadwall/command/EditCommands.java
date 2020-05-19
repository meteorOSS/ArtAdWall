package com.meteor.artadwall.command;

import com.meteor.artadwall.inv.AdInv;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommands implements CommandI {
    @Override
    public boolean excute(CommandSender sender, Command cmd, String arg, String[] args) {
        if(args[0].equalsIgnoreCase("edit")){
            if(!(sender instanceof Player)){return true;}
            AdInv.openAdList((Player)sender);
            return true;
        }
        return false;
    }
}
