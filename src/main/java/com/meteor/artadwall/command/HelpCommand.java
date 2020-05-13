package com.meteor.artadwall.command;

import com.meteor.artadwall.ArtAdWall;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandI {
    @Override
    public boolean excute(CommandSender sender, Command cmd, String arg, String[] args) {
        if(args[0].equalsIgnoreCase("help")){
            for(String str : ArtAdWall.plugin.getLang().getMessageList("message.player-help")){
                sender.sendMessage(str);
            }
            return true;
        }
        return true;
    }
}
