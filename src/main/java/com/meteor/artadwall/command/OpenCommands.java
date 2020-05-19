package com.meteor.artadwall.command;

import com.meteor.artadwall.guiholder.Sort;
import com.meteor.artadwall.inv.AdInv;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenCommands implements CommandI {
    @Override
    public boolean excute(CommandSender sender, Command cmd, String arg, String[] args) {
        if(args.length==0||args[0].equalsIgnoreCase("open")){
            if(!(sender instanceof Player)){return true;}
            AdInv.openMenu((Player)sender, Sort.LIKE,1);
            return true;
        }
        return false;
    }
}
