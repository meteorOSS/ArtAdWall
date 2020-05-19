package com.meteor.artadwall.command;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OpCommands implements CommandExecutor {
    private ArtAdWall plugin;
    public OpCommands(ArtAdWall plugin){
        this.plugin = plugin;
    }
    private boolean isInt(String i){
        try {
            Integer integer = Integer.valueOf(i);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0||args[0].equalsIgnoreCase("help")){
            if(!sender.isOp()){
                return true;
            }
            sender.sendMessage("§f[ §cArtAdWall 管理命令 §f]");
            sender.sendMessage("§6/artadm reload §f| §3重载配置文件");
            sender.sendMessage("§6/artadm set [p] [amount] §f| §3设置玩家可创建广告上限");
            return true;
        }
        if(args.length==1&&args[0].equalsIgnoreCase("reload")){
            if(!sender.isOp()){
                return true;
            }
            plugin.reloadConfig();
            plugin.reloadFile();
            sender.sendMessage("§f[ §aArtAdWall §f] §c已重载配置文件^^");
            return true;
        }
        if(args.length==3&&args[0].equalsIgnoreCase("set")){
            if(!sender.isOp()){
                return true;
            }
            if(!isInt(args[2])){
                sender.sendMessage("§f[ §aArtAdWall §f] §c输入的不是一个整数！");
                return true;
            }
            ArtAdWall.plugin.getSqllite().updateLimt(args[1],Integer.valueOf(args[2]));
            if(Bukkit.getPlayerExact(args[1])!=null){
                ArtAdWall.plugin.getPlayerData().get(args[1]).setLimi(Integer.valueOf(args[2]));
            }
            sender.sendMessage("§f[ §aArtAdWall §f] §c已设置 " + args[1] + " 的创建上限为 "+ args[2]);
            return true;
        }
        return false;
    }
}
