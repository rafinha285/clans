package me.abacate.clans.types;

import org.bson.Document;
import org.bukkit.entity.Player;


import java.util.UUID;

public class PlayerMongo {
    private String _id;
    private String name;
    private String clan;
    private int points;

    public PlayerMongo(String id,String name, String clan, Integer points) {
        this._id = id;
        this.name = name;
        this.clan = clan;
        this.points = points;
    }

    public static PlayerMongo fromDocument(Document doc){
        return new PlayerMongo(
            doc.getString("_id"),
            doc.getString("name"),
            doc.getString("clan"),
            doc.getInteger("points")
        );
    }

    // getters and setters

    public String getId() {
        return _id;
    }

    public void setId(String id) {
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
