package me.abacate.clans.managers;

import java.util.HashMap;
import java.util.Map;

public class AllyInviteManager {
    private final Map<String, String> allyInvites; // Mapa de convites: (Clã Destino -> Clã Origem)

    public AllyInviteManager() {
        this.allyInvites = new HashMap<>();
    }

    public void sendAllyInvite(String fromClan, String toClan) {
        allyInvites.put(toClan, fromClan);
    }

    public void removeAllyInvite(String toClan) {
        allyInvites.remove(toClan);
    }

    public boolean isAllyInvited(String toClan) {
        return allyInvites.containsKey(toClan);
    }

    public String getAllyInviter(String toClan) {
        return allyInvites.get(toClan);
    }
}
