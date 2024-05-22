package me.abacate.clans.types;

import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class ClanMongo extends Document {
    private String name;
    private List<UUID> members;
    private int points;
    private List<String> enemies;
    private List<String> allies;
    private UUID owner;
    public ClanMongo(String name, List<UUID> members, int points, List<String> enemies, List<String> allies,UUID owner) {
        this.name = name;
        this.members = members;
        this.points = points;
        this.enemies = enemies;
        this.allies = allies;
        this.owner = owner;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<String> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<String> enemies) {
        this.enemies = enemies;
    }

    public List<String> getAllies() {
        return allies;
    }

    public void setAllies(List<String> allies) {
        this.allies = allies;
    }
    public UUID getOwner(){
        return owner;
    }
    public void setOwner(UUID newOwner){
        this.owner = newOwner;
    }
}
