package me.luckyzz.arbuzikclans.clan.chat;

import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface ClanChat {

    void send(String message, ClanMember member);

    void setChatEnabled(boolean can, ClanMember member);

    void setMuted(ClanMember muted, boolean isMuted, ClanMember member);

}
