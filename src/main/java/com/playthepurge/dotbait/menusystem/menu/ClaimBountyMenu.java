package com.playthepurge.dotbait.menusystem.menu;

import com.playthepurge.dotbait.EcoBounty;
import com.playthepurge.dotbait.menusystem.Menu;
import com.playthepurge.dotbait.menusystem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ClaimBountyMenu extends Menu {
    public ClaimBountyMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Claim Bounties";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        switch (e.getCurrentItem().getType()){
            case GOLD_INGOT:
                //check the players inventory for any player heads
                Player p = (Player) e.getWhoClicked();
                ItemStack[] contents = p.getInventory().getContents();
                for(ItemStack item : contents){
                    if(item != null) {
                        if (item.getType().equals(Material.PLAYER_HEAD)) {
                            //Check if player head is active bounty
                            SkullMeta skull = (SkullMeta) item.getItemMeta();
                            assert skull != null;
                            OfflinePlayer owner = skull.getOwningPlayer();
                            double price = EcoBounty.getInstance().getPlayerBountyPrice(owner.getPlayer());
                            EcoBounty.getInstance().getServer().dispatchCommand(EcoBounty.getInstance().getServer().getConsoleSender(), "eco add " + ((Player) e.getWhoClicked()).getDisplayName() + " " + price);
                            p.getInventory().remove(item);
                            EcoBounty.getInstance().claimActiveBounty(owner.getPlayer());
                            EcoBounty.getInstance().sendPlayerMessage(p,"You successfully claimed your bounties!", ChatColor.GREEN);
                            e.getWhoClicked().closeInventory();
                            return;
                        }
                    }
                }
                EcoBounty.getInstance().sendPlayerMessage((Player) e.getWhoClicked(),"You do not have a bounty head in your inventory.", ChatColor.RED);
                break;
            case BARRIER:
                //Go back to main menu
                new BountyMainMenu(playerMenuUtility).open();
                break;
        }
    }

    @Override
    public void setMenuItems() {
        //Add the claim button and the cancel button
        ItemStack claimBountyButton = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta claimBountyButtonMeta = claimBountyButton.getItemMeta();
        claimBountyButtonMeta.setDisplayName(ChatColor.GOLD + "Claim Bounties");
        claimBountyButton.setItemMeta(claimBountyButtonMeta);

        ItemStack closeButton = new ItemStack(Material.BARRIER, 1);
        ItemMeta closeButtonItemMeta = closeButton.getItemMeta();
        closeButtonItemMeta.setDisplayName(ChatColor.RED + "Close Menu");
        closeButton.setItemMeta(closeButtonItemMeta);

        inventory.setItem(3, claimBountyButton);
        inventory.setItem(5, closeButton);
    }
}
