package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;

public interface ClanRank extends NotUsedClanRank {

    @Override
    default ClanRank toUsing() {
        throw new UnsupportedOperationException();
    }

    Clan getClan();

    void changePrefix(String prefix, ClanMember member);

}
