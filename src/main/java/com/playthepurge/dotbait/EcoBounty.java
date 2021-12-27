package com.playthepurge.dotbait;

import com.playthepurge.dotbait.commands.BountyCommand;
import com.playthepurge.dotbait.listeners.KillListener;
import com.playthepurge.dotbait.listeners.MenuListener;
import com.playthepurge.dotbait.menusystem.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

import me.clip.placeholderapi.PlaceholderAPI;

public class EcoBounty extends JavaPlugin {

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUnilityMap = new HashMap<>();

    private static EcoBounty instance;


    public static EcoBounty getInstance() {
        return instance;
    }

    /*
    ======================================
    Custom Bounties.yml file

     */

    private File bountiesFile;
    private FileConfiguration bounties;


    //====================================

    @Override
    public void onEnable(){

        instance = this;

        createBounties();

        getCommand("bounty").setExecutor(new BountyCommand());

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new KillListener(), this);
    }


    public static PlayerMenuUtility getPlayerMenuUntility(Player p){
        PlayerMenuUtility playerMenuUtility;

        if(playerMenuUnilityMap.containsKey(p)){
            return playerMenuUnilityMap.get(p);
        }else{
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUnilityMap.put(p,playerMenuUtility);

            return playerMenuUtility;
        }
    }


    //save our config file
    public void SaveBounties(){
        try {
            bounties.save(bountiesFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public FileConfiguration getBounties(){
        return this.bounties;
    }

    private void createBounties(){
        bountiesFile = new File(getDataFolder(), "bounties.yml");
        if(!bountiesFile.exists()){
            bountiesFile.getParentFile().mkdirs();
            saveResource("bounties.yml", false);
        }

        bounties = new YamlConfiguration();
        try{
            bounties.load(bountiesFile);
        }catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public boolean isBountyAlreadyCreated(Player p){
        //Is there and active bounty already?
        if(bounties.isSet("Active."+p.getUniqueId().toString())){
            return true;
        }
        return false;
    }

    public boolean AddPendingBounty(Player bountyCreator, Player bounty){
        //Check if the player already has a bounty set for them
        if(isBountyAlreadyCreated(bounty)){
            sendPlayerMessage(bountyCreator,"That bounty has already been created", ChatColor.RED);
            return false;
        }

        bounties.set("Pending."+bounty.getUniqueId().toString()+"."+"creator", bountyCreator.getUniqueId().toString());
        SaveBounties();
        return true;
    }

    public boolean isPendingBounty(Player bountyCreator, Player bounty){
        //Check if bounty is a valid player
        if(bounty == null) return false;

        //Check if there is a pending bounty for the user
        if(bounties.isSet("Pending."+bounty.getUniqueId().toString())){
            if(bounties.get("Pending."+bounty.getUniqueId().toString()+".creator").equals(bountyCreator.getUniqueId().toString())){
                return true;
            }
        }
        return false;
    }

    public double getBountyPrice(Player bountyCreator, Player bounty){
        if(bounties.isSet("Active."+bounty.getUniqueId().toString())){
            return Double.parseDouble(bounties.get("Active."+bounty.getUniqueId().toString()+".price").toString());
        }
        return 0.0;
    }

    public boolean setPendingBountyPrice(Player bountyCreator, Player bounty, String price){
        if(isPendingBounty(bountyCreator, bounty)){
            //Variables
            double setPrice = Double.parseDouble(price);
            ConsoleCommandSender console = Bukkit.getConsoleSender();


            //Check if the player has enough money first
            String bountyCreatorBankBalance = "%lighteconomy_player_balance%";

            OfflinePlayer oplayer = Bukkit.getOfflinePlayer(bountyCreator.getUniqueId());
            bountyCreatorBankBalance = PlaceholderAPI.setPlaceholders(oplayer, bountyCreatorBankBalance);
            double creatorBalance = Double.parseDouble(bountyCreatorBankBalance.replace(",",""));

            if(creatorBalance < setPrice){
                sendPlayerMessage(bountyCreator, "You do not have enough money in your wallet to set that price. Please try again.", ChatColor.RED);
                return false;
            }



            //Remove the money from the player
            Bukkit.dispatchCommand(console,"eco take "+bountyCreator.getDisplayName()+" " + setPrice);

            //Remove the pending bounty
            bounties.set("Pending."+bounty.getUniqueId().toString(), null);

            //Move to active with the price
            bounties.set("Active."+bounty.getUniqueId().toString()+".price", Integer.parseInt(price));
            bounties.set("Active."+bounty.getUniqueId().toString()+".creator", bountyCreator.getUniqueId().toString());
            bountyCreator.sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + ChatColor.GREEN + "Bounty set for: " + ChatColor.RED + bounty.getDisplayName() + ChatColor.GREEN +
                    " | $" + ChatColor.GOLD + price + ChatColor.GREEN + ".");
            sendPlayerMessage(bountyCreator, "Your wallet is now at: $"+ Double.toString(creatorBalance-setPrice), ChatColor.GREEN);
            SaveBounties();
            return true;
        }
        return false;
    }

    public boolean removeActiveBounty(Player bountyCreator, Player bounty){
        if(bounties.isSet("Active."+bounty.getUniqueId().toString())){
            if(bounties.get("Active."+bounty.getUniqueId().toString()+".creator").equals(bountyCreator.getUniqueId().toString())){
                bounties.set("Active."+bounty.getUniqueId().toString(), null);
                SaveBounties();
                return true;
            }
        }
        return false;
    }

    public void sendPlayerMessage(Player p, String msg, ChatColor msgColor){
        p.sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + msgColor + msg + ".");
    }

    public Map<Player, String> getAllActiveBountyPlayers(){

        Map<Player, String> bounty = new HashMap<>();
        for(String key : bounties.getConfigurationSection("Active").getKeys(false)) {

            if(key == null)
                return null;
            bounty.put(Bukkit.getPlayer(UUID.fromString(key)), bounties.getString("Active."+key+".price"));
        }


        return bounty;
    }

    public boolean doesPlayerHaveActiveBounty(Player p){
        for(String activeUUID : bounties.getConfigurationSection("Active").getKeys(false)){
            if(activeUUID.equals(p.getUniqueId().toString())){
                return true;
            }
        }
        return false;
    }

    public double getPlayerBountyPrice(Player p){
        double price = 0.0;
        for(String activeUUID : bounties.getConfigurationSection("Active").getKeys(false)){
            if(activeUUID.equals(p.getUniqueId().toString())){
                price = bounties.getDouble("Active."+activeUUID+".price");
                return price;
            }
        }
        return price;
    }

    public void claimActiveBounty(Player p){
        bounties.set("Active."+p.getUniqueId().toString(), null);
        SaveBounties();
    }


}
