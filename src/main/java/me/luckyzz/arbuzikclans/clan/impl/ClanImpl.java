package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.message.Message;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.chat.ClanChat;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.ClanMembers;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRanks;
import me.luckyzz.arbuzikclans.clan.region.ClanRegion;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrades;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.time.LocalDate;

class ClanImpl implements Clan {

    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final EconomyProvider economyProvider;
    private final QueryExecutors executors;

    private final int id;
    private final LocalDate dateCreated;
    private final ClanMembers members;
    private final ClanUpgrades upgrades;
    private final ClanRanks ranks;
    private final ClanRegion region;
    private final ClanChat chat;
    private String name;
    private int money, coins;

    ClanImpl(ClanConfig config, MessageConfig<Messages> messageConfig, EconomyProvider economyProvider, QueryExecutors executors, int id, LocalDate dateCreated, String name, ClanMembers members, ClanUpgrades upgrades, ClanRanks ranks, int money, int coins, ClanRegion region, ClanChat chat) {
        this.config = config;
        this.messageConfig = messageConfig;
        this.economyProvider = economyProvider;
        this.executors = executors;

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
        if (!config.getBoolean(Settings.CLAN_NAME_COLORS) && name.contains(ColorUtils.ALTERNATIVE_CODE_STRING)) {
            return;
        }
        String old = this.name;
        this.name = ColorUtils.color(name);

        member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_RENAME_SUCCESS_EXECUTOR)
                .placeholder("%old_name%", old)
                .placeholder("%new_name%", this.name));
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
    public ClanRegion getRegion() {
        return region;
    }

    @Override
    public ClanChat getChat() {
        return chat;
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
                .append(money, clan.money)
                .append(coins, clan.coins)
                .append(config, clan.config)
                .append(messageConfig, clan.messageConfig)
                .append(economyProvider, clan.economyProvider)
                .append(executors, clan.executors)
                .append(dateCreated, clan.dateCreated)
                .append(name, clan.name)
                .append(members, clan.members)
                .append(upgrades, clan.upgrades)
                .append(ranks, clan.ranks)
                .append(region, clan.region)
                .append(chat, clan.chat)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(config)
                .append(messageConfig)
                .append(economyProvider)
                .append(executors)
                .append(id)
                .append(dateCreated)
                .append(name)
                .append(members)
                .append(upgrades)
                .append(ranks)
                .append(money)
                .append(coins)
                .append(region)
                .append(chat)
                .toHashCode();
    }
}
