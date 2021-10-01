package me.luckyzz.arbuzikclans.clan.chat.mute;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import org.bukkit.entity.Player;

public interface ClanChatMutes {

    Clan getClan();

    ClanChatMute getMute(ClanMember member);

    default ClanChatMute getMute(String member) {
        return getMute(getClan().getMembers().getMember(member));
    }

    default ClanChatMute getMute(Player player) {
        return getMute(getClan().getMembers().getMember(player));
    }

    void mute(ClanMember muted, ClanMember whoMute, long timeMute, String reason);

    void unmuteSilently(ClanChatMute mute);

    void unmuteSilently(ClanMember muted);

}
