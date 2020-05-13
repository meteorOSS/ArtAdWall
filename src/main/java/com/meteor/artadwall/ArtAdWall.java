package com.meteor.artadwall;

import com.meteor.artadwall.command.PlayerCommands;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.AdType;
import com.meteor.artadwall.inv.InvType;
import com.meteor.artadwall.sqlite.Sqllite;
import com.meteor.artadwall.tools.MessageManager;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ArtAdWall extends JavaPlugin {
    private Sqllite sqllite;
    private MessageManager lang;
    private List<AdData> ads = new ArrayList<>();
    public static HashMap<InvType,MessageManager> gui = new HashMap<>();
    public static ArtAdWall plugin = null;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadFile();
        getServer().getLogger().info("[ArtAdWall] 广告墙插件已载入。");
        {
                plugin = this;
                sqllite = new Sqllite(this,new File(this.getDataFolder(),"ad.db"));
                lang = new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/lang.yml")));
                getCommand("artad").setExecutor(new PlayerCommands());
                loadAds();
        }
    }
    @Override
    public void onDisable() {
        try {
            getSqllite().close();
            getServer().getLogger().info("[ArtAdWall] 广告墙插件已卸载.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reloadFile(){
        reloadConfig();
        saveResource("lang.yml",false);
        saveResource("menu-gui.yml",false);
        saveResource("edit-gui.yml",false);
        gui.put(InvType.MENU,new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/menu-gui.yml"))));
        gui.put(InvType.EDIT,new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/edit-gui.yml"))));
    }

    public Sqllite getSqllite() {
        return sqllite;
    }
    public MessageManager getLang() {
        return lang;
    }
    private YamlConfiguration getYml(String data){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(new StringReader(data));
        return yml;
    }

    public List<AdData> getAds() {
        return ads;
    }

    private ItemStack getItem(String name, YamlConfiguration yml){
        ItemStack itemStack = new ItemStack(Material.STONE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name.replace("&","§"));
        List<String> lore = new ArrayList<>();
        for(String str :yml.getStringList("lore")){
            str = str.replace("&","§");
            lore.add(str);
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private void loadAds(){
        Statement smt = null;
        try {
            smt = getSqllite().getConnection().createStatement();
            ResultSet set = smt.executeQuery("select * from adwall;");
            while (set.next()){
                YamlConfiguration yml = getYml(set.getString("info"));
                AdData adData = new AdData();
                adData.setItem(getItem(set.getString("name"),yml));
                adData.setEnable(yml.getBoolean("enable"));
                adData.setLike(set.getInt("like"));
                adData.setOwner(set.getString("owner"));
                adData.setType(AdType.valueOf(set.getString("type")));
                adData.setName(set.getString("name"));
                adData.setWolrd(yml.getString("worldname").replace("&","§"));
                adData.setDate(yml.getLong("date"));
                ads.add(adData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                smt.close();
                getServer().getLogger().info("[ArtAdWall] 本次读取了"+ads.size()+"个广告");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
