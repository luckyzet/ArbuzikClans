package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.provider.economy.EconomicUser;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckkyyz.luckapi.util.date.DateZone;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class ClanServiceImpl implements ClanService {

    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final EconomyProvider economyProvider;

    private final QueryExecutors executors;
    private final Set<Clan> clans = new HashSet<>();

    public ClanServiceImpl(ClanConfig config, MessageConfig<Messages> messageConfig, EconomyProvider economyProvider, QueryExecutors executors) {
        this.config = config;
        this.messageConfig = messageConfig;
        this.economyProvider = economyProvider;
        this.executors = executors;
        reload();

        if (true) {
            executors.sync().update("DROP TABLE clans, clanMembers, clanRanks, clanQuests;");
        }

        executors.sync().update("CREATE TABLE IF NOT EXISTS `clans` (" +
                "`id` INT NOT NULL PRIMARY KEY, " +
                "`dateCreated` VARCHAR(64) NOT NULL, " +
                "`name` VARCHAR(64) NOT NULL, " +
                "`money` INT NOT NULL, " +
                "`coins` INT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanMembers` (" +
                "`name` VARCHAR(64) NOT NULL PRIMARY KEY, " +
                "`clan` INT NOT NULL, " +
                "`rank` INT NOT NULL, " +
                "`questsCompleted` INT NOT NULL, " +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanRegions` (" +
                "`clan` INT NOT NULL PRIMARY KEY, " +
                "`center` TEXT NOT NULL, " +
                "`accessBlocks` VARCHAR(6) NOT NULL, " +
                "`accessChests` VARCHAR(6) NOT NULL, " +
                "`accessBlocksWhitelist` TEXT NOT NULL, " +
                "`accessChestsWhitelist` TEXT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanChats` (" +
                "`clan` INT NOT NULL PRIMARY KEY, " +
                "`enabled` VARCHAR(6) NOT NULL, " +
                "`muted` TEXT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanRanks` (" +
                "`clan` INT NOT NULL, " +
                "`index` INT NOT NULL, " +
                "`prefix` VARCHAR(64) NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanQuests` (" +
                "`name` VARCHAR(64) NOT NULL, " +
                "`target` VARCHAR(16) NOT NULL, " +
                "`count` INT NOT NULL, " +
                "`needCount` INT NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanUpgrades` (" +
                "`clan` INT NOT NULL, " +
                "`id` INT NOT NULL" +
                ")");
    }

    @Override
    public void reload() {
        clans.clear();

        executors.async().result("SELECT * FROM clans", clanResult -> {

        });
    }

    @Override
    public Collection<Clan> getAllClans() {
        return clans;
    }

    @Override
    public Clan getClan(int id) {
        return clans.stream().filter(clan -> clan.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Clan getClan(String name) {
        return clans.stream().filter(clan -> clan.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public Clan getClanPlayer(String name) {
        return clans.stream().filter(clan -> clan.getMembers().hasMember(name)).findFirst().orElse(null);
    }

    private int getClanId() {
        return clans.stream()
                .sorted(Comparator.comparingInt(Clan::getId))
                .map(Clan::getId)
                .findFirst().orElse(0);
    }

    @Override
    public void createClan(Player player, String name) {
        if (!player.isOnline()) {
            messageConfig.getMessage(Messages.SOMETHING_WENT_WRONG).send(player);
            return;
        }

        if (hasClan(player)) {
            messageConfig.getMessage(Messages.ALREADY_CLAN).send(player);
            return;
        }

        if (!config.getBoolean(Settings.CLAN_NAME_COLORS) && name.contains(ColorUtils.ALTERNATIVE_CODE_STRING)) {
            messageConfig.getMessage(Messages.CLAN_CREATE_NOT_COLORS).send(player);
            return;
        }
        int needMoney = config.getInt(Settings.CLAN_CREATE_MONEY);
        if (needMoney > 0) {
            EconomicUser economicUser = economyProvider.getUser(player);
            if (!economicUser.hasBalance(needMoney)) {
                messageConfig.getMessage(Messages.NOT_ENOUGH_MONEY).send(player);
                return;
            }
            economicUser.changeBalance(-needMoney);
        }

        name = ColorUtils.color(name);
        int id = getClanId() + 1;
        LocalDate dateCreated = LocalDate.now(DateZone.MOSCOW.getIdentifier());
        int money = 0, coins = 0;


    }

    @Override
    public void cancel() {
        clans.clear();
    }
}
