package me.luckyzz.arbuzikclans.clan.member.rank;

import me.luckkyyz.luckapi.api.Service;

import java.util.Collection;
import java.util.Map;

public interface ClanRankService extends Service {

    NotUsedClanRank getRank(int index);

    NotUsedClanRank getRank(RankRole role);

    Collection<NotUsedClanRank> getRanks();

}
