package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.message.StringMessage;
import me.luckkyyz.luckapi.util.date.DateFormat;
import me.luckkyyz.luckapi.util.date.FormatDate;
import me.luckyzz.arbuzikclans.clan.chat.ClanChat;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.ClanMembers;
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

    int getCoins();

    void changeCoinsSilently(int amount);

    void changeCoinsQuest(int amount, ClanMember member);

    ClanRegion getRegion();

    ClanChat getChat();

    void send(Message message);

    default void send(String message) {
        send(new StringMessage(message));
    }

}
