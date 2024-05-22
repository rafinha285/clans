package me.abacate.clans.types;

import java.util.UUID;

public class ClanInvite {
    private final UUID sender;
    private final UUID receiver;
    private final String clanName;
    private final long inviteTime;

    public ClanInvite(UUID sender, UUID receiver, String clanName) {
        this.sender = sender;
        this.receiver = receiver;
        this.clanName = clanName;
        this.inviteTime = System.currentTimeMillis();
    }
    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public String getClanName() {
        return clanName;
    }

    public long getInviteTime() {
        return inviteTime;
    }
}
