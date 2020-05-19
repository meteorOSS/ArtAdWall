package com.meteor.artadwall.inv;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.AdType;
import com.meteor.artadwall.data.PlayerData;
import com.meteor.artadwall.guiholder.*;
import com.meteor.artadwall.tools.AdPage;
import com.meteor.artadwall.tools.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdInv {
    enum FlagType {
        PRE,NEXT;
    }
    private static ItemStack getPageItem(FlagType flagType){
        MessageManager mes = ArtAdWall.gui.get(InvType.MENU);
        String type = flagType==FlagType.PRE?"pre-page.":"next-page.";
        ItemStack itemStack = new ItemStack(getMaterial(mes.getYml().getString("items."+type+"ID")),1,(short)mes.getYml().getInt("items."+type+"data"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(mes.getMessageList("items."+type+"lore"));
        itemMeta.setDisplayName(mes.getMessage("items."+type+"name"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private static ItemStack getSortItem(Sort sort){
        MessageManager mes = ArtAdWall.gui.get(InvType.MENU);
        ItemStack sortitem = new ItemStack(getMaterial(mes.getYml().getString("items.like-sort.ID")),1,(short)mes.getYml().getInt("items.like-sort.data"));
        ItemMeta itemMeta1 = sortitem.getItemMeta();
        itemMeta1.setDisplayName(mes.getMessage("items.like-sort.name"));
        List<String> lore = new ArrayList<>();
        String type = sort==Sort.DATE?"§a时间排序":"§d爱心数排序";
        String newsort = sort==Sort.DATE?"§a爱心数排序":"§d时间排序";
        mes.getMessageList("items.like-sort.lore").forEach((str)->{
            str = str.replace("%sort-type%",type).replace("%sort-new%",newsort);
            lore.add(str);
        });
        itemMeta1.setLore(lore);
        sortitem.setItemMeta(itemMeta1);
        return sortitem;
    }
    static void addFlag(Inventory inventory,int[] ints,Sort sort){
        MessageManager mes = ArtAdWall.gui.get(InvType.MENU);
        ItemStack itemStack = new ItemStack(getMaterial(mes.getYml().getString("items.return.ID")),1,(short)mes.getYml().getInt("items.return.data"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("");
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(49,getSortItem(sort));
        inventory.setItem(48,getPageItem(FlagType.PRE));
        inventory.setItem(50,getPageItem(FlagType.NEXT));
        for(int i : ints){
            inventory.setItem(i,itemStack);
        }
    }
    static String getType(AdType adType){
        YamlConfiguration yml = (YamlConfiguration) ArtAdWall.plugin.getConfig();
        switch (adType){
            case OTHER:
                return yml.getString("type.other");
            case SHOP:
                return yml.getString("type.shop");
            case TOWN:
                return yml.getString("type.town");
            case MOBSPAWNER:
                return yml.getString("type.mobspanwner");
            default:
                return "nmsl";
        }
    }
    public static void openMenu(Player player, Sort sort, int page){
        AdMenuHolder adMenuHolder = new AdMenuHolder(sort,page);
        List<AdData> ads = adMenuHolder.getSort().sort(AdPage.getPageList(ArtAdWall.plugin.getAds(),page,44));
        adMenuHolder.setAds(ads);
        Inventory inventory = Bukkit.createInventory(adMenuHolder,54, ArtAdWall.gui.get(InvType.MENU).getMessage("title"));
        addFlag(inventory,new int[] {45,46,47,51,52,53},adMenuHolder.getSort());
        player.openInventory(inventory);
        String id = ArtAdWall.plugin.getConfig().getString("item.ID");
        short data = (short) ArtAdWall.plugin.getConfig().getInt("item.data");
        Bukkit.getScheduler().runTaskAsynchronously(ArtAdWall.plugin,()->{
            YamlConfiguration yml = (YamlConfiguration) ArtAdWall.plugin.getConfig();
            ads.forEach((item)->{
                ItemStack itemStack = new ItemStack(Material.valueOf(id),1,data);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(item.getName().replace("&","§"));
                List<String> lore = new ArrayList<>();
                for(String str : yml.getStringList("item.lore")){
                    str = str.replace("%type%",getType(item.getType()))
                            .replace("%world%",item.getWolrd())
                            .replace("%owner%",item.getOwner())
                            .replace("%like%",item.getLike()+"")
                            .replace("%time%",new SimpleDateFormat("yyyy/MM/dd").format(new Date(item.getDate())))
                            .replace("&","§");
                    lore.add(str);
                }
                item.getLore().forEach((dec)-> lore.add(dec.replace("&","§")));
                yml.getStringList("item.sub-lore").forEach((sub)-> lore.add(sub.replace("&","§")));
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                inventory.addItem(itemStack);
            });
        });
    }
    static ItemStack getAdInfo(AdData adData){
        List<String> lore = new ArrayList<>();
        String id = ArtAdWall.plugin.getConfig().getString("item.ID");
        short data = (short) ArtAdWall.plugin.getConfig().getInt("item.data");
        ItemStack rnitem = new ItemStack(Material.valueOf(id),1,data);
        ItemMeta itemMeta = rnitem.getItemMeta();
        itemMeta.setDisplayName(adData.getName().replace("&","§"));
        for(String str:ArtAdWall.plugin.getConfig().getStringList("item.lore")){
            str = str.replace("%type%",getType(adData.getType()))
                    .replace("%world%",adData.getWolrd())
                    .replace("%owner%",adData.getOwner())
                    .replace("%like%",adData.getLike()+"")
                    .replace("%time%",new SimpleDateFormat("yyyy/MM/dd").format(new Date(adData.getDate())))
                    .replace("&","§");
            lore.add(str);
        }
        adData.getLore().forEach((s)->{
            s = s.replace("&","§");
            lore.add(s);
        });
        itemMeta.setLore(lore);
        rnitem.setItemMeta(itemMeta);
        return rnitem;
    }
    public static void openEdit(Player player){
        Inventory inv = Bukkit.createInventory(new EditHolder(),9,ArtAdWall.gui.get(InvType.EDIT).getMessage("title"));
        ConfigurationSection yml = ArtAdWall.gui.get(InvType.EDIT).getYml().getConfigurationSection("edit-icon");
        for(String str:yml.getKeys(false)){
            ItemStack itemStack = new ItemStack(getMaterial(yml.getString(str+".ID")),1,(short)yml.getInt(str+".data"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(ArtAdWall.gui.get(InvType.EDIT).getMessageList("edit-icon."+str+".lore"));
            itemMeta.setDisplayName(yml.getString(str+".name").replace("&","§"));
            itemStack.setItemMeta(itemMeta);
            inv.setItem(yml.getInt(str+".slot"),itemStack);
        }
        inv.setItem(0,getAdInfo(ArtAdWall.plugin.getPlayerData().get(player.getName()).getRad()));
        player.openInventory(inv);
    }
    static List<AdData> getEditAds(List<String> ad){
        List<AdData> rnads = new ArrayList<>();
        for(String string : ad){
            for(AdData adData : ArtAdWall.plugin.getAds()){
                if(adData.getName().equalsIgnoreCase(string)){
                    rnads.add(adData);
                }
            }
        }
        return rnads;
    }
    static ItemStack getCreateNewAd(){
        MessageManager mes = ArtAdWall.gui.get(InvType.LIST);
        ItemStack itemStack = new ItemStack(getMaterial(mes.getYml().getString("items.create-new.ID")),1,(short)mes.getYml().getInt("items.create-new.data"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(mes.getMessage("items.create-new.name"));
        itemMeta.setLore(mes.getMessageList("items.create-new.lore"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    static Material getMaterial(String material){
        try{
            Material materialrn = Material.valueOf(material);
            return materialrn;
        }catch (IllegalArgumentException e){
            return Material.BEDROCK;
        }
    }
    public static void openTypeSelect(Player player){
        MessageManager mes = ArtAdWall.gui.get(InvType.TYPESEL);
        Inventory inv = Bukkit.createInventory(new TypeSelHolder(),9,mes.getMessage("title"));
        ConfigurationSection yml = ArtAdWall.gui.get(InvType.TYPESEL).getYml().getConfigurationSection("items");
        for (String str : yml.getKeys(false)){
            ItemStack itemStack = new ItemStack(getMaterial(mes.getYml().getString("items."+str+".ID")),1,(short)mes.getYml().getInt("items."+str+".data"));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(mes.getMessage("items."+str+".name"));
            itemMeta.setLore(mes.getMessageList("items."+str+".lore"));
            itemStack.setItemMeta(itemMeta);
            inv.setItem(mes.getYml().getInt("items."+str+".slot"),itemStack);
        }
        player.openInventory(inv);
    }
    public static void openAdList(Player player){
        ListHolder listHolder = new ListHolder();
        PlayerData playerData = ArtAdWall.plugin.getPlayerData().get(player.getName());
        if(playerData.getAd()!=null){
            listHolder.setAds(getEditAds(playerData.getAd()));
        }
        Inventory inv = Bukkit.createInventory(listHolder,9,ArtAdWall.gui.get(InvType.LIST).getMessage("title"));
        int[] slot = {1,2,3,4,5,6,7};
        if(listHolder.getAds()!=null){
            listHolder.getAds().forEach((ad)->{
                    ItemStack itemStack = new ItemStack(Material.PAPER);
                    ItemMeta itemMetae = itemStack.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    for(String str : ad.getLore()){
                        str = str.replace("&","§");
                        lore.add(str);
                    }
                    itemMetae.setDisplayName(ad.getName().replace("&","§"));
                    ArtAdWall.plugin.getLang().getYml().getStringList("message.edit-lore").forEach((s)->lore.add(s=s.replace("&","§")));
                    itemMetae.setLore(lore);
                    itemStack.setItemMeta(itemMetae);
                    inv.addItem(itemStack);
            });
        }
        inv.setItem(8,getCreateNewAd());
        player.openInventory(inv);
    }
}
