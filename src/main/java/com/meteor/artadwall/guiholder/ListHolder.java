package com.meteor.artadwall.guiholder;

import com.meteor.artadwall.data.AdData;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

public class ListHolder implements InventoryHolder {
    private List<AdData> ads = new ArrayList<>();
    public List<AdData> getAds() {
        return ads;
    }
    public void setAds(List<AdData> ads) {
        this.ads = ads;
    }
    @Override
    public Inventory getInventory() {
        return null;
    }
}
