package com.meteor.artadwall.events;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.EnterType;
import com.meteor.artadwall.data.PlayerData;
import com.meteor.artadwall.guiholder.EditHolder;
import com.meteor.artadwall.guiholder.ListHolder;
import com.meteor.artadwall.inv.AdInv;
import com.meteor.artadwall.tools.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class EditClick implements Listener {
    private ArtAdWall plugin;
    public EditClick(ArtAdWall plugin){
        this.plugin = plugin;
    }
    private AdData getKey(String name, List<AdData> ads){
        name = name.replace("§","&");
        for(AdData adData :ads){
            if(adData.getName().equalsIgnoreCase(name)){
                return adData;
            }
        }
        return null;
    }
    @EventHandler
    void click(InventoryClickEvent event){
        if(event.getInventory().getHolder()==null){return;}
            if(event.getInventory().getHolder() instanceof EditHolder){
                event.setCancelled(true);
                Player player = (Player)event.getWhoClicked();
                PlayerData playerData = ArtAdWall.plugin.getPlayerData().get(player.getName());
                MessageManager mes = ArtAdWall.plugin.getLang();
                switch (event.getRawSlot()){
                    case 2:
                        player.closeInventory();
                        playerData.setEnterType(EnterType.RNAME);
                        mes.sendMessage("message.enter-rname",player);
                        return;
                    case 3:
                        player.closeInventory();
                        playerData.setEnterType(EnterType.RLORE);
                        mes.sendMessageList("message.cg-lore.pre-mes",player);
                        player.sendMessage("§b当前介绍:");
                        for(String str: playerData.getRad().getLore()){
                            str = str.replace("&","§");
                            player.sendMessage(str);
                        }
                        return;
                    case 4:
                        player.closeInventory();
                        player.sendMessage(mes.getMessage("message.new-location"));
                        playerData.getRad().setLocation(player.getLocation());
                        return;
                    case 5:
                        player.closeInventory();
                        AdInv.openTypeSelect(player);
                        return;
                    case 6:
                        player.closeInventory();
                        AdInv.openAdList(player);
                        return;
                    case 8:
                        mes.sendMessage("message.remove-ad",player);
                        player.closeInventory();
                        playerData.getAd().remove(playerData.getRad().getName());
                        ArtAdWall.plugin.getSqllite().delAd(playerData.getRad());
                        ArtAdWall.plugin.getAds().remove(playerData.getRad());
                        ArtAdWall.plugin.getSqllite().updateAds(player.getName(),playerData.getAd());
                        return;
                }
            }
            if(event.getInventory().getHolder() instanceof ListHolder){
                event.setCancelled(true);
                if(event.getCurrentItem()!=null&&event.getCurrentItem().getType()!= Material.AIR){
                    Player player = (Player)event.getWhoClicked();
                    PlayerData playerData = plugin.getPlayerData().get(player.getName());
                    ListHolder listHolder = (ListHolder)event.getInventory().getHolder();
                    if(event.getRawSlot()==8){
                        playerData.setEnterType(EnterType.NEWAD);
                        player.closeInventory();
                        ArtAdWall.plugin.getLang().sendMessage("message.enter-name",player);
                        return;
                    }
                    AdData adData = getKey(event.getCurrentItem().getItemMeta().getDisplayName(),listHolder.getAds());
                    if(adData!=null){
                        playerData.setRad(adData);
                        AdInv.openEdit((Player)event.getWhoClicked());
                    }
                }
            }

    }
}
