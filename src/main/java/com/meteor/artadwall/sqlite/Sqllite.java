package com.meteor.artadwall.sqlite;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.AdType;
import com.meteor.artadwall.data.EnterType;
import com.meteor.artadwall.data.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.StringReader;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class Sqllite {
    private ArtAdWall plugin;
    private Connection connection = null;
    private Statement st = null;
    private File file = null;
    public Sqllite(ArtAdWall plugin, File file){
        this.plugin = plugin;
        this.file = file;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+this.file);
            st = connection.createStatement();
            st.executeUpdate("create table if not exists adwall (name text not null,like int,type text not null,info text not null,owner text not null)");
            st.executeUpdate("create table if not exists player (name text not null,ad text,limt int)");
            st.close();
            plugin.getServer().getLogger().info("[ArtAdWall] 已连接本地数据库...");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            plugin.getServer().getLogger().info("[ArtAdWall] 数据库连接发生异常");
            e.printStackTrace();
        }
    }
    public void doCommand(PreparedStatement ps){
        try {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void close() throws SQLException {connection.close();};
    public Connection getConnection(){
        if(connection==null){
            throw new NullPointerException("[ArtAdWall] 数据库未链接成功....");
        }
        return this.connection;
    }
    ////////////////////////////////
    private ItemStack getItemStack(String name){
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Arrays.asList("&c未设置描述","&c未设置描述"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private YamlConfiguration getYaml(String name){
        ResultSet set = null;
        YamlConfiguration yml = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from adwall where name = ?");
            ps.setString(1,name);
            set = ps.executeQuery();
            yml = YamlConfiguration.loadConfiguration(new StringReader(set.getString("info")));
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return yml;
    }
    public void addNewAd(String name, String playername, String worldname, Location location) {
        YamlConfiguration yml = new YamlConfiguration();
        try {
            List<String> likeplayer = Arrays.asList("player");
            List<String> lore = Arrays.asList("lore","lore");
            AdData adData = new AdData(AdType.OTHER,name,0,playername,worldname,true,System.currentTimeMillis(),likeplayer,location);
            adData.setLore(Arrays.asList("&c未设置描述", "&c未设置描述"));
            plugin.getAds().add(adData);
            PreparedStatement ps = getConnection().prepareStatement("insert into adwall (name,like,type,owner,info) values (?,?,?,?,?)");
            ps.setString(1, adData.getName());
            ps.setInt(2, adData.getLike());
            ps.setString(3, AdType.OTHER.toString());
            ps.setString(4, adData.getOwner());
            yml.set("lore", Arrays.asList("&c未设置描述", "&c未设置描述"));
            yml.set("worldname", worldname);
            yml.set("date", System.currentTimeMillis());
            yml.set("likeplayer",Arrays.asList("player"));
            yml.set("location",location);
            ps.setString(5, yml.saveToString());
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateLike(int i,String name){
        name = name.replace("§","&");
        try {
            PreparedStatement ps = getConnection().prepareStatement("update adwall set like = ? where name = ?");
            ps.setInt(1,i);
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getServer().getLogger().info("update adwall set like = "+i+" where name = "+name);
        }
    }
    public void updateLikePlayer(List<String> player,String name){
        name = name.replace("§","&");
        YamlConfiguration yml = getYaml(name);
        yml.set("likeplayer",player);
        try {
            PreparedStatement ps = getConnection().prepareStatement("update adwall set info = ? where name = ?");
            ps.setString(1,yml.saveToString());
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean checkPlayerData(String name){
        ResultSet set = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from player where name = ?");
            ps.setString(1,name);
            set = ps.executeQuery();
            if(set.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private void loadData(String name){
        ResultSet set = null;
        try {
            PreparedStatement ps = getConnection().prepareStatement("select * from player where name = ?");
            ps.setString(1,name);
            set = ps.executeQuery();
            if(set.next()){
                PlayerData playerData = new PlayerData();
                playerData.setName(name);
                playerData.setLimi(set.getInt("limt"));
                if(set.getString("ad")!=null){
                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(new StringReader(set.getString("ad")));
                    playerData.setAd(yml.getStringList("ads"));
                }
                playerData.setEnterType(EnterType.NO);
                plugin.getPlayerData().put(name,playerData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void joincreateData(String name){
        if(!checkPlayerData(name)){
            try {
                PreparedStatement ps = getConnection().prepareStatement("insert into player (name,ad,limt) values (?,?,?)");
                ps.setString(1,name);
                ps.setString(2,null);
                ps.setInt(3,plugin.getConfig().getInt("setting.normal"));
                doCommand(ps);
                loadData(name);
                return;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        loadData(name);
    }

    public void updateAds(String name,List<String> ad){
        try {
            PreparedStatement ps = getConnection().prepareStatement("update player set ad = ? where name = ?");
            YamlConfiguration yml = new YamlConfiguration();
            yml.set("ads",ad);
            ps.setString(1,yml.saveToString());
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateName(String newname,String name){
        try {
            PreparedStatement ps = getConnection().prepareStatement("update adwall set name = ? where name = ?");
            ps.setString(1,newname);
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateLore(String name,List<String> lore){
        YamlConfiguration yml = getYaml(name);
        yml.set("lore",lore);
        try {
            PreparedStatement ps = getConnection().prepareStatement("update adwall set info = ? where name = ?");
            ps.setString(1,yml.saveToString());
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateType(String name,String type){
        try {
            PreparedStatement ps = getConnection().prepareStatement("update adwall set type = ? where name = ?");
            ps.setString(1,type);
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateEnable(String name,boolean bool){
        YamlConfiguration yml = getYaml(name);
        yml.set("enable",bool);
        try {
            PreparedStatement ps = getConnection().prepareStatement("update adwall set info = ? where name = ?");
            ps.setString(1,yml.saveToString());
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delAd(AdData adData){
        try {
            PreparedStatement ps = getConnection().prepareStatement("delete from adwall where name = ?");
            ps.setString(1,adData.getName());
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateLoca(Location location,String name){
        YamlConfiguration yml = getYaml(name);
        yml.set("location",location);
        try {
            PreparedStatement ps = getConnection().prepareStatement("update adwall set info = ? where name = ?");
            ps.setString(1,yml.saveToString());
            ps.setString(2,name);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateLimt(String player,int amount){
        try {
            PreparedStatement ps = getConnection().prepareStatement("update player set limt = ? where name = ?");
            ps.setInt(1,amount);
            ps.setString(2,player);
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
