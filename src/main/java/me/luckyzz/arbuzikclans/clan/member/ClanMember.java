package me.luckyzz.arbuzikclans.clan.member;

import me.luckkyyz.luckapi.util.player.PlayerFilters;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRank;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.Consumer;

public interface ClanMember {

    Clan getClan();

    String getName();

    default boolean isOnline() {
        Player player = getPlayer();
        return player != null && player.isOnline();
    }

    default Player getPlayer() {
        return PlayerFilters.byName(getName()).orElse(null);
    }

    default void apply(Consumer<Player> consumer) {
        if(!isOnline()) {
            return;
        }
        Player player = getPlayer().getPlayer();
        consumer.accept(player);
    }

    ClanRank getRank();

    void changeRank(ClanRank rank, ClanMember member);

    Collection<MemberQuest> getQuests();

    void changeQuests(Collection<MemberQuest> quests);

    int getQuestsCompleted();

    void changeQuestsCompleted(int amount);

    default void addQuestCompleted() {
        changeQuestsCompleted(1);
    }

    default boolean hasPossibility(RankPossibility possibility) {
        return getRank().hasPossibility(possibility);
    }

}
