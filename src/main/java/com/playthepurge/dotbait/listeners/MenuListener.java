package com.playthepurge.dotbait.listeners;

import com.dotbait.main.events.PurgeEvent;
import com.playthepurge.dotbait.menusystem.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.Objects;

public class MenuListener implements Listener {

    public void onPurgeEvent(PurgeEvent e){
        if(!e.getPurgeEvent()){

        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();

        InventoryHolder holder = Objects.requireNonNull(e.getClickedInventory()).getHolder();

        if(holder instanceof Menu){

            e.setCancelled(true);

            if(e.getCurrentItem() == null){
                return;
            }

            Menu menu = (Menu) holder;
            menu.handleMenu(e);

        }

    }


}
