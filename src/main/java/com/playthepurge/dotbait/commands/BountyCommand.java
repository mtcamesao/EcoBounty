package com.playthepurge.dotbait.commands;

import com.playthepurge.dotbait.EcoBounty;
import com.playthepurge.dotbait.menusystem.menu.BountyMainMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class BountyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            //Check if the command contains any args..
            if(args.length == 0) {
                //Open the bounty main menu
                Player p = (Player) sender;
                new BountyMainMenu(EcoBounty.getPlayerMenuUntility(p)).open();
                return true;
            }else{
                if(args[0].equals("remove")){ // Remove bounty command
                    if(args.length == 2 && Bukkit.getPlayer(args[1]) != null){
                        Player bounty = Bukkit.getPlayer(args[1]);
                        Player creator = (Player) sender;
                        double price = EcoBounty.getInstance().getBountyPrice(creator, bounty);
                        if(EcoBounty.getInstance().removeActiveBounty(creator,bounty)){
                            sender.sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + ChatColor.GREEN + "Your bounty has been removed and you've been refunded.");

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"eco add "+creator.getDisplayName()+" " + price);
                            return true;
                        }else{
                            sender.sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + ChatColor.RED + "There was an error refunding your bounty.. Did you create a bounty in that name?");
                            return false;
                        }
                    }
                }else if(args.length == 2){ // Set Price Command
                    if(Bukkit.getPlayer(args[0]) != null && EcoBounty.getInstance().isPendingBounty((Player) sender, Objects.requireNonNull(Bukkit.getPlayer(args[0])))) {
                        Player bountyTarget = Bukkit.getPlayer(args[0]);
                        if(EcoBounty.getInstance().setPendingBountyPrice((Player) sender, bountyTarget, args[1].replaceAll("[^0-9]",""))){
                            return true;
                        }
                    }else{
                        sender.sendMessage(ChatColor.DARK_GRAY + "[BOUNTY] " + ChatColor.RED + "You need to create the bounty first before");
                        sender.sendMessage(ChatColor.RED + "using this command");
                    }
                }
            }
        }
        return false;
    }
}
