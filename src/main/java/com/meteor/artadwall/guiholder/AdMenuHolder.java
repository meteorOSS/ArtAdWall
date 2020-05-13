package com.meteor.artadwall.guiholder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class AdMenuHolder implements InventoryHolder {
    Inventory inv;
    int page;
    Sort sort;
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

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
