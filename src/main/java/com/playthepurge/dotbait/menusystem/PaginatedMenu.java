package com.playthepurge.dotbait.menusystem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class PaginatedMenu extends Menu{

    protected int page = 0;

    //28 empty slots per page
    protected int maxItemsPerPage = 28;

    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    //Border for our menu
    public void addMenuBorder(){
        ItemStack left = new ItemStack(Material.DARK_OAK_BUTTON, 1);
        ItemMeta leftMeta = left.getItemMeta();
        leftMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
        left.setItemMeta(leftMeta);

        inventory.setItem(48, left);

        ItemStack close = new ItemStack(Material.BARRIER, 1);
        ItemMeta closeMeta = left.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Main Menu");
        close.setItemMeta(closeMeta);

        inventory.setItem(49, close);

        ItemStack right = new ItemStack(Material.DARK_OAK_BUTTON, 1);
        ItemMeta rightMeta = left.getItemMeta();
        rightMeta.setDisplayName(ChatColor.GREEN + "Next Page");
        right.setItemMeta(rightMeta);

        inventory.setItem(50, right);

        for(int i = 0; i < 10; i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);
        inventory.setItem(37, super.FILLER_GLASS);

        for(int i = 43; i < 54; i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

}
