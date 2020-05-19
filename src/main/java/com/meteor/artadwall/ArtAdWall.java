package com.meteor.artadwall;

import com.meteor.artadwall.command.OpCommands;
import com.meteor.artadwall.command.PlayerCommands;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.AdType;
import com.meteor.artadwall.data.PlayerData;
import com.meteor.artadwall.events.AdInventoryClick;
import com.meteor.artadwall.events.EditClick;
import com.meteor.artadwall.events.PlayerChat;
import com.meteor.artadwall.events.TypeSelClick;
import com.meteor.artadwall.inv.InvType;
import com.meteor.artadwall.sqlite.Sqllite;
import com.meteor.artadwall.tools.MessageManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.StringReader;
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
    public static Economy economy = null;
    private HashMap<String,PlayerData> playerData = new HashMap<>();
    public static HashMap<InvType,MessageManager> gui = new HashMap<>();
    public static ArtAdWall plugin = null;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadFile();
        getServer().getLogger().info("---------------------------------");
        getServer().getLogger().info("[ArtAdWall] 广告墙插件已载入。");
        getServer().getLogger().info("//使用问题,联系qq2260483272");
        getServer().getLogger().info("---------------------------------");
        {
                plugin = this;
                sqllite = new Sqllite(this,new File(this.getDataFolder(),"ad.db"));
                lang = new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/lang.yml")));
                registerCommands();
                registerEvents();
                loadAds();
        }
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
            RegisteredServiceProvider<Economy> ecoapi = getServer().getServicesManager().getRegistration(Economy.class);
            economy = ecoapi.getProvider();
            getServer().getLogger().info("[ArtAdWall] 已关联到Vault插件");
        }
    }
    private void registerCommands(){
        getCommand("artad").setExecutor(new PlayerCommands());
        getCommand("artadm").setExecutor(new OpCommands(this));
    }
    private void registerEvents(){
        getServer().getPluginManager().registerEvents(new AdInventoryClick(this),this);
        getServer().getPluginManager().registerEvents(new EditClick(this),this);
        getServer().getPluginManager().registerEvents(new PlayerChat(this),this);
        getServer().getPluginManager().registerEvents(new TypeSelClick(),this);
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
        String[] file = {"menu-gui.yml","edit-gui.yml","ad-list.yml","type-sel.yml","lang.yml"};
        for(String string : file){
            File f = new File(getDataFolder()+"/"+string);
            if(!f.exists()){
                saveResource(string,false);
            }
        }
        gui.put(InvType.MENU,new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/menu-gui.yml"))));
        gui.put(InvType.EDIT,new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/edit-gui.yml"))));
        gui.put(InvType.LIST,new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/ad-list.yml"))));
        gui.put(InvType.TYPESEL,new MessageManager(YamlConfiguration.loadConfiguration(new File(getDataFolder()+"/type-sel.yml"))));
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

    public HashMap<String, PlayerData> getPlayerData() {
        return playerData;
    }

    private void loadAds(){
        Statement smt = null;
        try {
            smt = getSqllite().getConnection().createStatement();
            ResultSet set = smt.executeQuery("select * from adwall;");
            while (set.next()){
                YamlConfiguration yml = getYml(set.getString("info"));
                AdData adData = new AdData();
                adData.setEnable(yml.getBoolean("enable"));
                adData.setLike(set.getInt("like"));
                adData.setOwner(set.getString("owner"));
                adData.setType(AdType.valueOf(set.getString("type")));
                adData.setName(set.getString("name"));
                adData.setWolrd(yml.getString("worldname").replace("&","§"));
                adData.setDate(yml.getLong("date"));
                adData.setLikeplayer(yml.getStringList("likeplayer"));
                adData.setLocation((Location)yml.get("location"));
                adData.setLore(yml.getStringList("lore"));
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
