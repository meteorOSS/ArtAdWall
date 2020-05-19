package com.meteor.artadwall.events;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.EnterType;
import com.meteor.artadwall.data.PlayerData;
import com.meteor.artadwall.inv.AdInv;
import com.meteor.artadwall.tools.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerChat implements Listener {
    private ArtAdWall plugin;

    public PlayerChat(ArtAdWall plugin) {
        this.plugin = plugin;
    }

    private boolean isBlack(String name) {
        if (plugin.getConfig().getStringList("black").contains(name)) {
            return false;
        }
        return true;
    }

    private void sendAdLore(Player player, List<String> mes) {
        mes.forEach((string) -> {
            string = string.replace("&", "§");
            player.sendMessage(string);
        });
    }
    private boolean isNameExit(String name){
        for(AdData adData : ArtAdWall.plugin.getAds()){
            if(adData.getName().equalsIgnoreCase(name)){
                return false;
            }
        }
        return true;
    }
    @EventHandler
    public void chat(AsyncPlayerChatEvent chatEvent) {
        PlayerData playerData = plugin.getPlayerData().get(chatEvent.getPlayer().getName());
        if(playerData.getEnterType()!=EnterType.NO){
            Player player = chatEvent.getPlayer();
            MessageManager mes = ArtAdWall.plugin.getLang();
            if (playerData.getEnterType() != EnterType.NO) {
                if (chatEvent.getMessage().equalsIgnoreCase("cancel")) {
                    mes.sendMessage("message.cancel",player);
                    playerData.setEnterType(EnterType.NO);
                    chatEvent.setCancelled(true);
                    return;
                }
                if (!isBlack(chatEvent.getMessage())) {
                    return;
                }
                chatEvent.setCancelled(true);
            }
            switch (playerData.getEnterType()) {
                case NEWAD:
                    Bukkit.getScheduler().runTask(plugin,()->{
                        player.performCommand("artad create "+chatEvent.getMessage());
                        playerData.setEnterType(EnterType.NO);
                    });
                    return;
                case RNAME:
                    String oldname = playerData.getRad().getName();
                    String newname = chatEvent.getMessage();
                    if(!isNameExit(newname)){
                        mes.sendMessage("message.name-exit",player);
                        return;
                    }
                    playerData.getRad().setNameSQL(oldname,newname,playerData);
                    player.sendMessage(mes.getMessage("message.rname-sur").replace("@ad_name@", newname));
                    playerData.setEnterType(EnterType.NO);
                    Bukkit.getScheduler().runTask(plugin,()->{
                        player.closeInventory();
                        AdInv.openEdit(player);
                    });
                    return;
                case RLORE:
                    String message = chatEvent.getMessage();
                    List<String> lore = new ArrayList<>();
                    lore.addAll(playerData.getRad().getLore());
                    if (message.equalsIgnoreCase("save")) {
                        Bukkit.getScheduler().runTask(plugin,()->{
                            playerData.getRad().setLoreSql(playerData.getRad().getLore());
                            AdInv.openEdit(player);
                            playerData.setEnterType(EnterType.NO);
                            mes.sendMessage("message.save-lore", player);
                        });
                        return;
                    } else if (message.startsWith("add ")) {
                        player.sendMessage(mes.getMessage("message.add-lore"));
                        player.sendMessage("§b当前介绍:");
                        lore.add(message.substring(4));
                        sendAdLore(player,lore);
                        playerData.getRad().setLoreSql(lore);
                    } else if (message.startsWith("del ")) {
                        int line = Integer.valueOf(message.substring(4));
                        if(line+1>lore.size()){
                            mes.sendMessage("message.no-int", player);
                            return;
                        }
                        lore.remove(line-1);
                        playerData.getRad().setLoreSql(lore);
                        mes.sendMessage("message.remove-line", player);
                        player.sendMessage("§b当前介绍:");
                        sendAdLore(player,lore);
                    }
            }
            return;
        }
    }
}
