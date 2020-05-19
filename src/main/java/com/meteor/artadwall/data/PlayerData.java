package com.meteor.artadwall.data;

import com.meteor.artadwall.ArtAdWall;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class PlayerData {
    private String name;
    private List<String> ad = new ArrayList<>();
    private int limi;
    private EnterType enterType;
    private AdData rad;
    public PlayerData(String name, int limi) {
        this.name = name;
        this.ad = null;
        this.enterType = EnterType.NO;
        this.limi = limi;
    }
    public PlayerData(){
    }
    public PlayerData(String name, List<String> ad, int limi) {
        this.name = name;
        this.ad = ad;
        this.limi = limi;
        this.enterType = EnterType.NO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAd() {
        return ad;
    }

    public void setAd(List<String> ad) {
        this.ad = ad;
    }
    public void setAdSQL(List<String> ad){
        this.ad = ad;
        ArtAdWall.plugin.getSqllite().updateAds(name,ad);
    }
    public int getLimi() {
        return limi;
    }

    public void setLimi(int limi) {
        this.limi = limi;
    }

    public EnterType getEnterType() {
        return enterType;
    }

    public void setEnterType(EnterType enterType) {
        this.enterType = enterType;
    }

    public AdData getRad() {
        return rad;
    }

    public void setRad(AdData rad) {
        this.rad = rad;
    }
}
