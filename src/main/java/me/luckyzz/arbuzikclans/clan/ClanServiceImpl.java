package me.luckyzz.arbuzikclans.clan;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.config.SettingConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.provider.economy.EconomicUser;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckkyyz.luckapi.util.color.ColorUtils;
import me.luckkyyz.luckapi.util.date.DateFormat;
import me.luckkyyz.luckapi.util.date.DateUtil;
import me.luckkyyz.luckapi.util.date.DateZone;
import me.luckkyyz.luckapi.util.date.FormatDate;
import me.luckyzz.arbuzikclans.config.Messages;
import me.luckyzz.arbuzikclans.config.Settings;
import me.luckyzz.arbuzikclans.name.BelowNameService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClanServiceImpl implements ClanService {

    private final SettingConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final EconomyProvider economyProvider;

    private final QueryExecutors executors;
    private final Map<Integer, Clan> clanMap = new HashMap<>();

    public ClanServiceImpl(SettingConfig config, MessageConfig<Messages> messageConfig, EconomyProvider economyProvider, QueryExecutors executors) {
        this.config = config;
        this.messageConfig = messageConfig;
        this.economyProvider = economyProvider;
        this.executors = executors;

        executors.sync().update("CREATE TABLE IF NOT EXISTS `clans` (" +
                "`id` INT NOT NULL PRIMARY KEY, " +
                "`dateCreated` VARCHAR(64) NOT NULL, " +
                "`name` VARCHAR(64) NOT NULL" +
                ")");
        executors.sync().update("CREATE TABLE IF NOT EXISTS `clanMembers` (" +
                "`name` VARCHAR(64) NOT NULL PRIMARY KEY, " +
                "`clan` INT NOT NULL" +
                ")");

        reload();
    }

    @Override
    public void reload() {
        clanMap.clear();

        executors.async().result("SELECT * FROM clans", result -> {
            while (result.next()) {
                int id = result.getInt("id");
                LocalDateTime dateCreated = LocalDateTime.now();
                //LocalDateTime dateCreated = DateFormat.dateTimeFormatter().parse(result.getString("dateCreated"), FormatDate.DATE_TIME);
                String name = result.getString("name");

                Map<String, ClanMemberImpl> memberMap = new HashMap<>();
                executors.sync().result("SELECT * FROM clanMembers WHERE clan = ?", memberResult -> {
                    while (memberResult.next()) {
                        String memberName = memberResult.getString("name");
                        ClanMemberImpl member = new ClanMemberImpl(memberName);

                        memberMap.put(member.getName(), member);
                    }
                }, id);

                ClanMembersImpl members = new ClanMembersImpl(executors, memberMap);
                ClanImpl clan = new ClanImpl(messageConfig, executors, id, dateCreated, name, members);
                clanMap.put(clan.getId(), clan);
                members.setClan(clan);
                memberMap.values().forEach(member -> member.setClan(clan));
            }
        });
    }

    @Override
    public Clan getClanByMember(String name) {
        return clanMap.values().stream()
                .filter(clan -> clan.getMembers().getMember(name) != null)
                .findFirst().orElse(null);
    }

    private CompletableFuture<Integer> getLastId() {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        executors.async().result("SELECT id FROM clans ORDER BY id DESC LIMIT 1", result -> {
            if (!result.next()) {
                future.complete(0);
                return;
            }
            future.complete(result.getInt("id"));
        });
        return future;
    }

    @Override
    public void createClan(String name, Player owner) {
        CompletableFuture.runAsync(() -> {
            if (!owner.isOnline()) {
                return;
            }

            if(hasClanByMember(owner)) {
                messageConfig.getMessage(Messages.CLAN_CREATE_ALREADY_CLAN).send(owner);
                return;
            }

            if (!config.getBoolean(Settings.CLAN_NAME_COLORS) && name.contains(ColorUtils.ALTERNATIVE_CODE_STRING)) {
                messageConfig.getMessage(Messages.CLAN_CREATE_NOT_COLORS).send(owner);
                return;
            }

            int id;

            try {
                id = getLastId().get(5, TimeUnit.SECONDS) + 1;
            } catch (InterruptedException | TimeoutException | ExecutionException exception) {
                exception.printStackTrace();
                messageConfig.getMessage(Messages.SOMETHING_WENT_WRONG).send(owner);
                return;
            }

            EconomicUser economicUser = economyProvider.getUser(owner);
            int clanCreateMoney = config.getInt(Settings.CLAN_CREATE_MONEY);
            if (clanCreateMoney > 0) {
                if (!economicUser.hasBalance(clanCreateMoney)) {
                    messageConfig.getAdaptiveMessage(Messages.NOT_ENOUGH_MONEY)
                            .placeholder("%balance%", (int) economicUser.getBalance())
                            .placeholder("%need_balance%", clanCreateMoney)
                            .placeholder("%need%", clanCreateMoney - (int) economicUser.getBalance())
                            .send(owner);
                    return;
                }
                economicUser.changeBalance(-clanCreateMoney);
            }

            Clan clan = new ClanImpl(messageConfig, executors, id, DateUtil.getDate(DateZone.MOSCOW), name, owner);
            clanMap.put(clan.getId(), clan);

            executors.sync().update("INSERT INTO clans VALUES (?, ?, ?)", clan.getId(), clan.getDateCreated(FormatDate.DATE_TIME), clan.getName());
            messageConfig.getAdaptiveMessage(Messages.CLAN_CREATE_SUCCESS)
                    .placeholder("%name%", clan.getName())
                    .send(owner);
        });
    }

    @Override
    public void cancel() {
        clanMap.clear();
    }
}
