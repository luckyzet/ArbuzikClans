package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.util.date.DateFormat;
import me.luckkyyz.luckapi.util.date.FormatDate;
import me.luckyzz.arbuzikclans.clan.chat.ClanChat;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.ClanMembers;
import me.luckyzz.arbuzikclans.clan.member.invite.ClanInvites;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRanks;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrades;

import java.time.LocalDate;

public interface Clan {

    int getId();

    LocalDate getDateCreated();

    default String getDateCreated(FormatDate formatDate) {
        return DateFormat.dateFormatter().format(getDateCreated(), formatDate);
    }

    String getName();

    void renameClan(String name, ClanMember member);

    ClanMembers getMembers();

    ClanUpgrades getUpgrades();

    ClanRanks getRanks();

    int getMoney();

    void addMoney(int amount, ClanMember member);

    void takeMoney(int amount, ClanMember member);

    int getCoins();

    void addCoins(int amount, ClanMember member);

    ClanRegion getRegion();

    ClanChat getChat();

    ClanInvites getInvites();

}
