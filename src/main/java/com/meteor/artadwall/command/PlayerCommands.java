package com.meteor.artadwall.command;

import com.meteor.artadwall.ArtAdWall;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerCommands implements CommandExecutor {
    private static HashMap<String,CommandI> cmd = new HashMap<>();
    static {
        cmd.put("help",new HelpCommand());
        cmd.put("create",new CreateCommands());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length>=1){
            String arg = args[0].toLowerCase();
            if(!sender.hasPermission("artad.use")){
                ArtAdWall.plugin.getLang().sendMessage("message.no-perm",(Player)sender);
                return true;
            }
            if(cmd.containsKey(arg)){
                if (!((CommandI)cmd.get(arg)).excute(sender, command, arg, args)) {
                    ((CommandI) cmd.get("help")).excute(sender, command, arg, args);
                }
                return true;
            }
        }
        sender.sendMessage(ArtAdWall.plugin.getLang().getMessage("message.no-arg"));
        return true;
    }
}
