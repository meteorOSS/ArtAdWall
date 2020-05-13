package com.meteor.artadwall.sqlite;

import com.meteor.artadwall.ArtAdWall;
import com.meteor.artadwall.data.AdData;
import com.meteor.artadwall.data.AdType;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
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
            st.executeUpdate("create table if not exists player (name text not null,ad text not null)");
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
    public void addNewAd(String name,String playername,String worldname) {
        YamlConfiguration yml = new YamlConfiguration();
        try {
            PreparedStatement ps = getConnection().prepareStatement("insert into adwall (name,like,type,owner,info) values (?,?,?,?,?)");
            ps.setString(1, name);
            ps.setInt(2, 0);
            ps.setString(3, AdType.OTHER.toString());
            ps.setString(4, playername);
            yml.set("lore", Arrays.asList("&c未设置描述", "&c未设置描述"));
            yml.set("enable", true);
            yml.set("worldname", worldname);
            yml.set("date", System.currentTimeMillis());
            ps.setString(5, yml.saveToString());
            doCommand(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
