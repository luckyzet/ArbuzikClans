package me.luckyzz.arbuzikclans.clan.chat.mute;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface ClanChatMute {

    Clan getClan();

    ClanMember getWhoMute();

    ClanMember getMuted();

    String getReason();

    boolean checkActual();

    void unmuteSilently();

}
