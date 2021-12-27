package com.playthepurge.dotbait.menusystem.menu;

import com.playthepurge.dotbait.menusystem.Menu;
import com.playthepurge.dotbait.menusystem.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BountyMainMenu extends Menu {
    public BountyMainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Bounty Menu";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        switch (e.getCurrentItem().getType()){
            case SPRUCE_SIGN:
                new CreateBountyMenu(playerMenuUtility).open();
                break;
            case PLAYER_HEAD:
                new ActiveBountyMenu(playerMenuUtility).open();
                break;
            case GOLD_INGOT:
                new ClaimBountyMenu(playerMenuUtility).open();
                break;
            case BARRIER:
                e.getWhoClicked().sendMessage("Closing menu...");
                e.getWhoClicked().closeInventory();
                break;
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack createBountyButton = new ItemStack(Material.SPRUCE_SIGN, 1);
        ItemMeta createBountyButtonItemMeta = createBountyButton.getItemMeta();
        createBountyButtonItemMeta.setDisplayName(ChatColor.GREEN + "Create Bounty");
        createBountyButton.setItemMeta(createBountyButtonItemMeta);

        ItemStack activeBountyButton = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta activeBountyButtonMeta = activeBountyButton.getItemMeta();
        activeBountyButtonMeta.setDisplayName(ChatColor.DARK_GREEN + "Active Bounties");
        activeBountyButton.setItemMeta(activeBountyButtonMeta);

        ItemStack claimBountyButton = new ItemStack(Material.GOLD_INGOT, 1);
        ItemMeta claimBountyButtonMeta = claimBountyButton.getItemMeta();
        claimBountyButtonMeta.setDisplayName(ChatColor.GOLD + "Claim Bounties");
        claimBountyButton.setItemMeta(claimBountyButtonMeta);

        ItemStack closeButton = new ItemStack(Material.BARRIER, 1);
        ItemMeta closeButtonItemMeta = closeButton.getItemMeta();
        closeButtonItemMeta.setDisplayName(ChatColor.RED + "Close Menu");
        closeButton.setItemMeta(closeButtonItemMeta);

        inventory.setItem(1, createBountyButton);
        inventory.setItem(3, activeBountyButton);
        inventory.setItem(5, claimBountyButton);
        inventory.setItem(7, closeButton);
    }
}
