package com.meteor.artadwall.command;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.PlayerData;
import com.meteor.artadwall.inv.AdInv;
import com.meteor.artadwall.tools.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateCommands implements CommandI {
    private boolean isNameExit(String name){
        for(AdData adData : ArtAdWall.plugin.getAds()){
            if(adData.getName().equalsIgnoreCase(name)){
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean excute(CommandSender sender, Command cmd, String arg, String[] args) {
        if(args[0].equalsIgnoreCase("create")&&args.length==2){
            if(!(sender instanceof Player)){return true;}
            Player player = (Player) sender;
            PlayerData playerData = ArtAdWall.plugin.getPlayerData().get(player.getName());
            MessageManager mes = ArtAdWall.plugin.getLang();
            if(playerData.getAd()!=null){
                if(playerData.getAd().size()+1>playerData.getLimi()){
                    player.sendMessage(mes.getMessage("message.amount-max"));
                    return true;
                }
            }
            if(!isNameExit(args[1])){
                mes.sendMessage("message.name-exit",player);
                return true;
            }
            int money = ArtAdWall.plugin.getConfig().getInt("setting.money");
            if(ArtAdWall.economy.getBalance(player.getName())-money<0){
                player.sendMessage(mes.getMessage("message.money-req"));
                return true;
            }
            ArtAdWall.economy.withdrawPlayer(player.getName(),money);
            ArtAdWall.plugin.getSqllite().addNewAd(args[1],sender.getName(),player.getWorld().getName(),player.getLocation());
            player.sendMessage(mes.getMessage("message.new-ad").replace("@ad_name@",args[1]));
            if(playerData.getAd()==null){
                playerData.setAdSQL(Arrays.asList(args[1]));
                return true;
            }
            List<String> newrl = new ArrayList<>();
            playerData.getAd().forEach((string)->newrl.add(string));
            newrl.add(args[1]);
            playerData.setAdSQL(newrl);
            AdInv.openAdList(player);
            return true;
        }
        return false;
    }
}
