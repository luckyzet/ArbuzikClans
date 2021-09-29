package me.luckyzz.arbuzikclans;

import me.luckkyyz.luckapi.LuckApi;
import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.HikariDatabase;
import me.luckkyyz.luckapi.database.HikariQueryExecutors;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckkyyz.luckapi.database.serialize.DatabaseSerializers;
import me.luckkyyz.luckapi.menu.LuckMenuService;
import me.luckkyyz.luckapi.menu.MenuService;
import me.luckkyyz.luckapi.provider.economy.EconomyProvider;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.impl.*;
import me.luckyzz.arbuzikclans.clan.member.invite.ClanInviteService;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuestsService;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuestService;
import me.luckyzz.arbuzikclans.clan.member.rank.ClanRankService;
import me.luckyzz.arbuzikclans.clan.region.ClanRegionService;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgradeService;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.dependency.SoftDependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

@Plugin(name = "ArbuzikClans", version = "0.1.0-SNAPSHOT")
@Author("luckyzz")
@ApiVersion(ApiVersion.Target.v1_16)
@Command(name = "clan", aliases = {"clans", "c"})
@Command(name = "clanchat", aliases = {"cc", "cchat", "clanc"})
@SoftDependency("WorldEdit")
@SoftDependency("WorldGuard")
@SoftDependency("Vault")
@SoftDependency("ProtocolLib")
public final class ArbuzikClansPlugin extends JavaPlugin {

    private LuckApi luckApi;

    private HikariDatabase clanDatabase;

    private ClanService clanService;
    private ClanUpgradeService clanUpgradeService;
    private ClanRankService clanRankService;
    private ClanInviteService clanInviteService;
    private ClanRegionService clanRegionService;
    private MemberDayQuestsService memberDayQuestsService;
    private MemberQuestService memberQuestService;

    @Override
    public void onEnable() {
        luckApi = LuckApi.bootstrapWith(this);
        luckApi.registerService(MenuService.class, LuckMenuService::new);

        ClanConfig config = new ClanConfig(this);
        MessageConfig<Messages> messageConfig = new MessageConfig<>(this, Messages.values());
        EconomyProvider economyProvider = EconomyProvider.openProvider();

        clanDatabase = DatabaseSerializers.yaml().deserialize(config.getSection("database"));
        QueryExecutors clanExecutors = new HikariQueryExecutors(this, clanDatabase);

        memberDayQuestsService = new ConfigMemberDayQuestsService(config);

        clanUpgradeService = new ConfigClanUpgradeService(config, clanExecutors);
        clanRankService = new ConfigClanRankService(config, messageConfig, clanExecutors);
        clanService = new ClanServiceImpl(this, config, messageConfig, economyProvider, clanRankService, memberDayQuestsService, clanUpgradeService, clanExecutors);

        memberQuestService = new MemberQuestServiceImpl(this, messageConfig, clanService, memberDayQuestsService);
        clanInviteService = new ClanInviteServiceImpl(this, clanService, config, messageConfig);
        clanRegionService = new ClanRegionServiceImpl(this, messageConfig, clanService);
    }

    @Override
    public void onDisable() {
        if(luckApi != null) {
            luckApi.cancel();
        }
        if(clanService != null) {
            clanService.cancel();
        }
        if(clanUpgradeService != null) {
            clanUpgradeService.cancel();
        }
        if(clanRankService != null) {
            clanRankService.cancel();
        }
        if(clanInviteService != null) {
            clanInviteService.cancel();
        }
        if(clanRegionService != null) {
            clanRegionService.cancel();
        }
        if(memberDayQuestsService != null) {
            memberDayQuestsService.cancel();
        }
        if(memberQuestService != null) {
            memberQuestService.cancel();
        }
        if(clanDatabase != null) {
            clanDatabase.close();
        }
    }
}
