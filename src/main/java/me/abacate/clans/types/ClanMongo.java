package me.abacate.clans.types;

import org.bson.Document;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ClanMongo extends Document {
    private String name;
    private List<String> members;
    private int points;
    private List<String> enemies;
    private List<String> allies;
    private String owner;
    public ClanMongo(String name, List<String> members, int points, List<String> enemies, List<String> allies,String owner) {
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

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
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
    public String getOwner(){
        return owner.toString();
    }
    public void setOwner(UUID newOwner){
        this.owner = newOwner.toString();
    }
    public boolean isOwner(UUID player){
        return Objects.equals(owner.toString(), player.toString());
    }
}
