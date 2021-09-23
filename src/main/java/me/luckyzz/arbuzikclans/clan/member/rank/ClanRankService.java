package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckkyyz.luckapi.api.Service;

public interface ClanRankService extends Service {

    NotUsedClanRank getRank(int index);

    NotUsedClanRank getRank(RankRole role);

}
