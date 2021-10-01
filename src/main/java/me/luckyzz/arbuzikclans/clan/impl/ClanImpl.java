package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.provider.economy.EconomicUser;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.chat.ClanChat;
import me.luckyzz.arbuzikclans.clan.chat.mute.ClanChatMutes;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.ClanMembers;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRanks;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.clan.name.FormatNameCheck;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrades;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;

import java.time.LocalDate;

class ClanImpl implements Clan {

    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final EconomyProvider economyProvider;
    private final QueryExecutors executors;
    private final FormatNameCheck formatNameCheck;

    private final int id;
    private final LocalDate dateCreated;
    private final ClanMembers members;
    private final ClanUpgrades upgrades;
    private final ClanRanks ranks;
    private final ClanRegion region;
    private final ClanChat chat;
    private final ClanChatMutes mutes;
    private String name;
    private int money, coins;

    ClanImpl(ClanConfig config, MessageConfig<Messages> messageConfig, EconomyProvider economyProvider, QueryExecutors executors, FormatNameCheck formatNameCheck, int id, LocalDate dateCreated, String name, ClanMembers members, ClanUpgrades upgrades, ClanRanks ranks, int money, int coins, ClanRegion region, ClanChat chat, ClanChatMutes mutes) {
        this.config = config;
        this.messageConfig = messageConfig;
        this.economyProvider = economyProvider;
        this.executors = executors;
        this.formatNameCheck = formatNameCheck;

        this.id = id;
        this.dateCreated = dateCreated;
        this.name = name;
        this.members = members;
        this.upgrades = upgrades;
        this.ranks = ranks;
        this.money = money;
        this.coins = coins;
        this.region = region;
        this.chat = chat;
        this.mutes = mutes;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void renameClan(String name, ClanMember member) {
        if (!member.hasPossibility(RankPossibility.RENAME)) {
            member.apply(player -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player));
            return;
        }

        if(!member.isOnline()) {
            return;
        }
        Player player = member.getPlayer();
        int needMoney = config.getInt(Settings.RENAME_MONEY);
        if (needMoney > 0) {
            EconomicUser economicUser = economyProvider.getUser(player);
            if (!economicUser.hasBalance(needMoney)) {
                messageConfig.getAdaptiveMessage(Messages.NOT_ENOUGH_MONEY)
                        .placeholder("%balance%", (int) economicUser.getBalance())
                        .placeholder("%need_balance%", needMoney)
                        .placeholder("%need%", needMoney - (int) economicUser.getBalance())
                        .send(player);
                return;
            }
            economicUser.changeBalance(-needMoney);
        }
        if (!formatNameCheck.checkName(player, name)) {
            return;
        }
        String old = this.name;
        this.name = ColorUtils.color(name);

        messageConfig.getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_EXECUTOR)
                .placeholder("%old_name%", old)
                .placeholder("%new_name%", this.name);
        send(messageConfig.getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_LOCAL)
                .placeholder("%old_name%", old)
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", member.getName())
                .placeholder("%new_name%", this.name));
        messageConfig.getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_GLOBAL)
                .placeholder("%old_name%", old)
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", member.getName())
                .placeholder("%new_name%", this.name)
                .broadcast();

        executors.async().update("UPDATE clans SET name = ? WHERE id = ?", this.name, id);
    }

    @Override
    public ClanMembers getMembers() {
        return members;
    }

    @Override
    public ClanUpgrades getUpgrades() {
        return upgrades;
    }

    @Override
    public ClanRanks getRanks() {
        return ranks;
    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public void changeCoinsSilently(int amount) {
        coins += amount;
        executors.async().update("UPDATE clans SET coins = ? WHERE id = ?", coins, id);
    }

    @Override
    public void changeCoinsQuest(int amount, ClanMember member) {
        changeCoinsSilently(amount);

        send(messageConfig.getAdaptiveMessage(Messages.QUEST_COINS_GAVE)
                .placeholder("%rank%", member.getRank().getPrefix())
                .placeholder("%name%", member.getName())
                .placeholder("%amount%", amount)
                .placeholder("%sum_amount%", coins));
    }

    @Override
    public ClanRegion getRegion() {
        return region;
    }

    @Override
    public ClanChat getChat() {
        return chat;
    }

    @Override
    public ClanChatMutes getChatMutes() {
        return mutes;
    }

    @Override
    public void send(Message message) {
        members.getAllMembers().forEach(member -> member.apply(message::send));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanImpl clan = (ClanImpl) o;
        return new EqualsBuilder()
                .append(id, clan.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
