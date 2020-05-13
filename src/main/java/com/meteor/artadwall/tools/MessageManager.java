package com.meteor.artadwall.tools;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    private YamlConfiguration yml;
    public MessageManager(YamlConfiguration yml){
        this.yml = yml;
    }
    public String getMessage(String path){
        return yml.getString(path).replace("&","§");
    }
    public List<String> getMessageList(String path){
        List<String> rn = new ArrayList<>();
        for(String string : yml.getStringList(path)){
            string = string.replace("&","§");
            rn.add(string);
        }
        return rn;
    }
    public void sendMessage(String path, Player player){
        player.sendMessage(getMessage(path));
    }
    public void sendMessageList(String path,Player player){
        for(String message : getMessageList(path)){
            player.sendMessage(message);
        }
    }
    public YamlConfiguration getYml(){
        return this.yml;
    }
}
