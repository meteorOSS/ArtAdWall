package com.meteor.artadwall.inv;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.guiholder.AdMenuHolder;
import com.meteor.artadwall.guiholder.Sort;
import com.meteor.artadwall.tools.AdPage;
import com.meteor.artadwall.tools.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AdInv {
    enum FlagType {
        PRE,NEXT;
    }
    private static ItemStack getPageItem(FlagType flagType){
        MessageManager mes = ArtAdWall.gui.get(InvType.MENU);
        String type = flagType==FlagType.PRE?"pre-page.":"next-page.";
        ItemStack itemStack = new ItemStack(Material.valueOf(mes.getYml().getString("items."+type+"pre-page.ID")),1,(short)mes.getYml().getInt("items."+type+"pre-page.data"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(mes.getMessageList("items."+type+"lore"));
        itemMeta.setDisplayName(mes.getMessage("items."+type+"name"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static void openMenu(Player player, Sort sort, int page){
        AdMenuHolder adMenuHolder = new AdMenuHolder(sort,page);
        Inventory inventory = Bukkit.createInventory(adMenuHolder,54, ArtAdWall.gui.get(InvType.MENU).getMessage("title"));
        List<AdData> ads = adMenuHolder.getSort().sort(AdPage.getPageList(ArtAdWall.plugin.getAds(),page,40));
        int maxpage = (int)Math.ceil(ArtAdWall.plugin.getAds().size()/40);
        inventory.setItem(47,getPageItem(FlagType.PRE));
        inventory.setItem(49,getPageItem(FlagType.NEXT));
        player.openInventory(inventory);
        Bukkit.getScheduler().runTaskAsynchronously(ArtAdWall.plugin,()->{
            ads.forEach((ad)->{ inventory.addItem(ad.getItem()); });
        });
    }
}
