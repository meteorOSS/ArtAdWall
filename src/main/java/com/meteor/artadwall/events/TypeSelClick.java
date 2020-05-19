package com.meteor.artadwall.events;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdType;
import com.meteor.artadwall.data.PlayerData;
import com.meteor.artadwall.guiholder.TypeSelHolder;
import com.meteor.artadwall.inv.AdInv;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TypeSelClick implements Listener {
    private AdType getAdType(int slot){
        switch (slot){
            case 2:
                return AdType.SHOP;
            case 3:
                return AdType.OTHER;
            case 4:
                return AdType.MOBSPAWNER;
            case 5:
                return AdType.TOWN;
        }
        return null;
    }
    @EventHandler
    void click(InventoryClickEvent event){
        if(event.getInventory().getHolder()==null){return;}
        if(event.getInventory().getHolder() instanceof TypeSelHolder){
            if(event.getCurrentItem()!=null&&event.getCurrentItem().getType()!= Material.AIR){
                Player player = (Player)event.getWhoClicked();
                if(event.getRawSlot()==6){
                    AdInv.openEdit(player);
                    return;
                }
                PlayerData playerData = ArtAdWall.plugin.getPlayerData().get(player.getName());
                playerData.getRad().setTypeSql(getAdType(event.getRawSlot()));
                ArtAdWall.plugin.getLang().sendMessage("message.rtype",player);
                AdInv.openEdit(player);
                return;
            }
        }
    }
}
