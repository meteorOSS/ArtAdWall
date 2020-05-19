package com.meteor.artadwall.events;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.guiholder.AdMenuHolder;
import com.meteor.artadwall.guiholder.Sort;
import com.meteor.artadwall.inv.AdInv;
import com.meteor.artadwall.tools.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class AdInventoryClick implements Listener {
    private ArtAdWall plugin;
    public AdInventoryClick(ArtAdWall plugin){
        this.plugin = plugin;
    }
    private AdData getKey(List<AdData> ads,String name){
        name = name.replace("§","&");
        for(AdData adData : ads){
            if(adData.getName().equalsIgnoreCase(name)){
                return adData;
            }
        }
        return null;
    }
    @EventHandler
    void join(PlayerJoinEvent joinEvent){
        if(!plugin.getPlayerData().containsKey(joinEvent.getPlayer().getName())){
            plugin.getSqllite().joincreateData(joinEvent.getPlayer().getName());
        }
    }
    void quit(PlayerQuitEvent quitEvent){
        if(plugin.getPlayerData().containsKey(quitEvent.getPlayer().getName())){
           plugin.getPlayerData().remove(quitEvent.getPlayer().getName());
        }
    }
    @EventHandler
    void click(InventoryClickEvent event){
        if(event.getInventory().getHolder()==null){ return; }
        if(event.getInventory().getHolder() instanceof AdMenuHolder){
            event.setCancelled(true);
            AdMenuHolder holder = (AdMenuHolder)event.getInventory().getHolder();
            MessageManager mes = ArtAdWall.plugin.getLang();
            Player player = (Player)event.getWhoClicked();
            int maxpage = (int) Math.ceil(ArtAdWall.plugin.getAds().size()/44.00D);
            if(event.getCurrentItem()!=null&&event.getCurrentItem().getType()!= Material.AIR){
                switch (event.getRawSlot()){
                    case 48:
                        if(holder.getPage()==1){
                            mes.sendMessage("message.page-no",player);
                            return;
                        }
                        AdInv.openMenu(player,holder.getSort(),holder.getPage()-1);
                        holder.setPage(holder.getPage()-1);
                        return;
                    case 50:
                        if(holder.getPage()==maxpage){
                            mes.sendMessage("message.page-no",player);
                            return;
                        }
                        AdInv.openMenu(player,holder.getSort(),holder.getPage()+1);
                        holder.setPage(holder.getPage()+1);
                        return;
                    case 49:
                        Sort sort = holder.getSort()==Sort.DATE?Sort.LIKE:Sort.DATE;
                        AdInv.openMenu(player,sort,holder.getPage());
                        holder.setSort(sort);
                        return;
                }
                AdData adData = getKey(holder.getAds(),event.getCurrentItem().getItemMeta().getDisplayName());
                if(adData!=null){
                    switch (event.getClick()){
                        case LEFT:
                            player.teleport(adData.getLocation());
                            player.closeInventory();
                            player.sendMessage(mes.getMessage("message.join").replace("@ad_name@",adData.getName().replace("&","§")).replace("@owner@",adData.getOwner()));
                            break;
                        case RIGHT:
                            if(adData.getLikeplayer().contains(player.getName())){
                                mes.sendMessage("message.like-exit",player);
                                return;
                            }
                            adData.setLikeSQL(adData.getLike()+1);
                            player.sendMessage(mes.getMessage("message.click-like").replace("@ad_name@",adData.getName().replace("&","§")));
                            List<String> newl = new ArrayList<>();
                            adData.getLikeplayer().forEach((string)->{newl.add(string);});
                            newl.add(player.getName());
                            adData.setLikeplayerSQL(newl);
                            AdInv.openMenu(player,holder.getSort(),holder.getPage());
                            return;
                    }

                }
            }
        }

    }

}
