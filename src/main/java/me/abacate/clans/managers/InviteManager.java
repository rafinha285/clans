package me.abacate.clans.managers;

import me.abacate.clans.types.ClanInvite;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InviteManager {
    private final Map<UUID, ClanInvite> invites = new HashMap<>();

    public void addInvite(UUID receiver, ClanInvite invite) {
        invites.put(receiver, invite);
    }

    public ClanInvite getInvite(UUID receiver) {
        return invites.get(receiver);
    }

    public void removeInvite(UUID receiver) {
        invites.remove(receiver);
    }

    public boolean hasInvite(UUID receiver) {
        return invites.containsKey(receiver);
    }
}
