package com.meteor.artadwall.guiholder;

import com.meteor.artadwall.data.AdData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class AdMenuHolder implements InventoryHolder {
    Inventory inv;
    int page;
    Sort sort;
    List<AdData> ads;
    public AdMenuHolder(Sort sort,int page){
        this.sort = sort;
        this.page = page;
    }
    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }

    public List<AdData> getAds() {
        return ads;
    }

    public void setAds(List<AdData> ads) {
        this.ads = ads;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
