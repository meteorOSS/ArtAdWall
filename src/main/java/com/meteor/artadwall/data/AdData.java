package com.meteor.artadwall.data;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AdData {
    private ItemStack item;
    private AdType type;
    private String name;
    private int like;
    private String owner;
    private String wolrd;
    private boolean enable;
    private long date;

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
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

    public void setName(String name) {
        this.name = name;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
