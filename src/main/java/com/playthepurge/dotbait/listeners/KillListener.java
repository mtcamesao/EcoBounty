package com.playthepurge.dotbait.listeners;

import com.playthepurge.dotbait.EcoBounty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class KillListener implements Listener {



    @EventHandler
    public void onPlayerKill(PlayerDeathEvent e){
        //Check if the player was killed by another player
        boolean debug = false; // Add this to a config?
        if(e.getEntity().getKiller() instanceof Player || debug){
            //Check if the player is an active bounty
            if(EcoBounty.getInstance().doesPlayerHaveActiveBounty(e.getEntity())){
                //Drop their player head on death
                ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                ItemMeta playerHeadMeta = head.getItemMeta();


                SkullMeta meta = (SkullMeta) playerHeadMeta;
                OfflinePlayer player = Bukkit.getOfflinePlayer(e.getEntity().getUniqueId());
                assert meta != null;
                meta.setOwningPlayer(player);
                head.setItemMeta(meta);

                e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), head);
            }
        }else{
            EcoBounty.getInstance().getServer().getConsoleSender().sendMessage("Player wasn't killed by a player");
        }

    }

}
