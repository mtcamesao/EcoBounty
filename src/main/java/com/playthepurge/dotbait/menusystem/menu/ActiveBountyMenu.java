package com.playthepurge.dotbait.menusystem.menu;

import com.playthepurge.dotbait.EcoBounty;
import com.playthepurge.dotbait.menusystem.PaginatedMenu;
import com.playthepurge.dotbait.menusystem.PlayerMenuUtility;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActiveBountyMenu extends PaginatedMenu {
    public ActiveBountyMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Active Bounties";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        //Clicking on players heads will not show anything. However clicking
        //on the buttons will change pages

        Map<Player, String> bounties = EcoBounty.getInstance().getAllActiveBountyPlayers();
        List<Player> players = new ArrayList<>();


        for(Player p : bounties.keySet()){
            players.add(p);
        }

        if(e.getCurrentItem().getType().equals(Material.BARRIER)){

            //Main Menu
            new BountyMainMenu(playerMenuUtility).open();

        }else if(e.getCurrentItem().getType().equals(Material.DARK_OAK_BUTTON)){
            if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Previous Page")){
                if(page == 0){
                    e.getWhoClicked().sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + ChatColor.RED + "You're already on the first page.");
                }else{
                    page = page - 1;
                    super.open();
                }
            }else if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Next Page")) {
                if(!((index + 1) >= players.size())){
                    page = page + 1;
                    super.open();
                }else{
                    e.getWhoClicked().sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + ChatColor.RED + "You're on the last page.");
                }
            }
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();

        Map<Player, String> bounties = EcoBounty.getInstance().getAllActiveBountyPlayers();
        List<Player> players = new ArrayList<>();


        for(Player p : bounties.keySet()){
            players.add(p);
        }

        if(bounties != null && !bounties.isEmpty()){
            for(int i = 0; i < super.maxItemsPerPage; i++){
                index = super.maxItemsPerPage * page + i;
                if(index >= bounties.size()) break;
                if(players.get(index) != null){
                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                    ItemMeta playerHeadMeta = playerHead.getItemMeta();


                    SkullMeta meta = (SkullMeta) playerHeadMeta;
                    List<String> headLore = new ArrayList<>();
                    headLore.add(ChatColor.GREEN + "Head Price:" + ChatColor.GOLD + " $" + bounties.get(players.get(index)));
                    assert playerHeadMeta != null;
                    playerHeadMeta.setLore(headLore);


                    OfflinePlayer player = Bukkit.getOfflinePlayer(players.get(index).getUniqueId());
                    meta.setOwningPlayer(player);
                    meta.getPersistentDataContainer().set(new NamespacedKey(EcoBounty.getInstance(), "uuid"), PersistentDataType.STRING, players.get(index).getUniqueId().toString());
                    playerHead.setItemMeta(meta);
                    inventory.addItem(playerHead);

                }
            }
        }
    }
}
