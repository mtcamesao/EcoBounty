package com.playthepurge.dotbait.menusystem.menu;

import com.playthepurge.dotbait.EcoBounty;
import com.playthepurge.dotbait.menusystem.Menu;
import com.playthepurge.dotbait.menusystem.PlayerMenuUtility;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class CreateBountyConfigMenu extends Menu {
    public CreateBountyConfigMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Create Bounty - Confirm";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        switch (e.getCurrentItem().getType()){
            case EMERALD:
                Player bountyTarget = super.playerMenuUtility.getBountyPlayer();
                //Create bounty, if successful let the player know.
                if(EcoBounty.getInstance().AddPendingBounty((Player) e.getWhoClicked(),bountyTarget)) {
                    e.getWhoClicked().sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + ChatColor.GREEN + "Bounty created for: " + ChatColor.RED + bountyTarget.getDisplayName() + ChatColor.GREEN + ". Please" +
                            " use: " + ChatColor.GOLD + "/bounty " + bountyTarget.getDisplayName() + " <PRICE> "
                            + ChatColor.GREEN + "to set your price.");
                }

                e.setCancelled(true);
                //Send information to data file
                e.getWhoClicked().closeInventory();
                break;
            case BARRIER:
                new CreateBountyMenu(playerMenuUtility).open();
                break;
            case PLAYER_HEAD:
                e.setCancelled(true);
                break;
        }
    }



    @Override
    public void setMenuItems() {
        Player bountyTarget = super.playerMenuUtility.getBountyPlayer();
        ItemStack confirm = new ItemStack(Material.EMERALD, 1);
        ItemMeta confirm_meta = confirm.getItemMeta();
        confirm_meta.setDisplayName(ChatColor.GREEN + "Confirm Bounty");
        ArrayList<String> confirm_lore = new ArrayList<>();
        confirm_lore.add(ChatColor.WHITE + "Once created use:");
        confirm_lore.add(ChatColor.GOLD + "/bounty "+ bountyTarget.getDisplayName() +" <price>");
        confirm_lore.add(ChatColor.WHITE + "to set your price.");
        confirm_meta.setLore(confirm_lore);
        confirm.setItemMeta(confirm_meta);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta playerHeadMeta = head.getItemMeta();
        SkullMeta meta = (SkullMeta) playerHeadMeta;
        OfflinePlayer player = Bukkit.getOfflinePlayer(bountyTarget.getUniqueId());
        meta.setOwningPlayer(player);
        meta.getPersistentDataContainer().set(new NamespacedKey(EcoBounty.getInstance(), "uuid"), PersistentDataType.STRING, bountyTarget.getUniqueId().toString());
        head.setItemMeta(meta);

        ItemStack cancel = new ItemStack(Material.BARRIER, 1);
        ItemMeta cancel_meta = cancel.getItemMeta();
        cancel_meta.setDisplayName(ChatColor.RED + "Cancel");
        ArrayList<String> cancel_lore = new ArrayList<>();
        cancel_lore.add(ChatColor.WHITE + "Cancel & go back.");
        cancel_meta.setLore(cancel_lore);
        cancel.setItemMeta(cancel_meta);

        inventory.setItem(2, confirm);
        inventory.setItem(4, head);
        inventory.setItem(6, cancel);
    }
}
