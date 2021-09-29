package me.luckyzz.arbuzikclans.clan.chat;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface ClanChat {

    Clan getClan();

    void send(String message, ClanMember member);

    void setChatEnabled(boolean can, ClanMember member);

    void setMuted(ClanMember muted, boolean isMuted, ClanMember member);

}
