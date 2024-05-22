package me.abacate.clans.types;

import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerMongo {
    private UUID _id;
    private String name;
    private String clan;
    private int points;

    public PlayerMongo(Player player, String clan, Integer points) {
        this._id = player.getUniqueId();
        this.name = player.getName();
        this.clan = clan;
        this.points = points;
    }

    // getters and setters

    public UUID getId() {
        return _id;
    }

    public void setId(UUID id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClan() {
        return clan;
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
