package com.playthepurge.dotbait.menusystem;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;

    private Player bountyPlayer;

    public Player getBountyPlayer() {
        return bountyPlayer;
    }

    public void setBountyPlayer(Player bountyPlayer) {
        this.bountyPlayer = bountyPlayer;
    }

    public PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
