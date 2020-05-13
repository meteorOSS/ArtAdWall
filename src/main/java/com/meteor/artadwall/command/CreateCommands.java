package com.meteor.artadwall.command;

import com.meteor.artadwall.ArtAdWall;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommands implements CommandI {
    @Override
    public boolean excute(CommandSender sender, Command cmd, String arg, String[] args) {
        if(args[0].equalsIgnoreCase("create")&&args.length==2){
            Player player = (Player) sender;
            ArtAdWall.plugin.getSqllite().addNewAd(args[1],sender.getName(),player.getWorld().getName());
            player.sendMessage("test");
            return true;
        }
        return false;
    }
}
