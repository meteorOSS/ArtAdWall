package com.meteor.artadwall.data;

import com.meteor.artadwall.ArtAdWall;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AdData {
    private AdType type;
    private String name;
    private int like;
    private String owner;
    private String wolrd;
    private boolean enable;
    private long date;
    private List<String> likeplayer;
    private Location location;
    private List<String> lore = new ArrayList<>();
    public AdData(){

    }
    public AdData(AdType type, String name, int like, String owner, String wolrd, boolean enable, long date,List<String> likeplayer,Location location) {
        this.type = type;
        this.name = name;
        this.like = like;
        this.owner = owner;
        this.wolrd = wolrd;
        this.enable = enable;
        this.date = date;
        this.likeplayer = likeplayer;
        this.location = location;
    }
    public AdData(AdType type, String name, int like, String owner, String wolrd, boolean enable, long date,List<String> likeplayer,Location location,List<String> lore) {
        this.type = type;
        this.name = name;
        this.like = like;
        this.owner = owner;
        this.wolrd = wolrd;
        this.enable = enable;
        this.date = date;
        this.likeplayer = likeplayer;
        this.location = location;
        this.lore = lore;
    }

    public List<String> getLore() {
        return lore;
    }
    public void addLore(String lore){
        this.lore.add(lore);
    }
    public void setLore(List<String> lore) {
        this.lore = lore;
    }
    public void setLoreSql(List<String> lore){
        this.lore = lore;
        ArtAdWall.plugin.getSqllite().updateLore(name,lore);
    }

    public AdType getType() {
        return type;
    }

    public void setType(AdType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setTypeSql(AdType type){
        this.type = type;
        ArtAdWall.plugin.getSqllite().updateType(name,type.toString());
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setNameSQL(String oldname,String name,PlayerData playerData) {
        playerData.getAd().remove(oldname);
        playerData.getAd().add(name);
        playerData.setAdSQL(playerData.getAd());
        ArtAdWall.plugin.getSqllite().updateName(name,this.name);
        this.name = name;
    }
    public int getLike() {
        return like;
    }
    public void setLike(int like) {
        this.like = like;
    }
    public void setLikeSQL(int like) {
        this.like = like;
        ArtAdWall.plugin.getSqllite().updateLike(like,name);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getWolrd() {
        return wolrd;
    }

    public void setWolrd(String wolrd) {
        this.wolrd = wolrd;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public void setEnableSql(boolean enable) {
        this.enable = enable;
        ArtAdWall.plugin.getSqllite().updateEnable(name,enable);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<String> getLikeplayer() {
        return likeplayer;
    }
    public void setLikeplayer(List<String> likeplayer) {
        this.likeplayer = likeplayer;
    }
    public void setLikeplayerSQL(List<String> likeplayer) {
        this.likeplayer = likeplayer;
        ArtAdWall.plugin.getSqllite().updateLikePlayer(likeplayer,name);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void setLocationSql(Location location) {
        this.location = location;
        ArtAdWall.plugin.getSqllite().updateLoca(location,name);
    }
}
