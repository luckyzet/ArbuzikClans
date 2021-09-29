package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.event.QuickEventListener;
import me.luckkyyz.luckapi.util.date.DateZone;
import me.luckkyyz.luckapi.util.scheduler.Scheduler;
import me.luckkyyz.luckapi.util.scheduler.SchedulerTicks;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.ClanService;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuestsService;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuestService;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class MemberQuestServiceImpl implements MemberQuestService {

    private final Set<Scheduler> schedulers = new HashSet<>();

    public MemberQuestServiceImpl(Plugin plugin, ClanService clanService, MemberDayQuestsService questsService) {
        schedulers.add(new QuestUpdateTask(plugin, questsService, clanService));

        QuickEventListener.newListener().event(EntityDeathEvent.class, event -> {
            LivingEntity entity = event.getEntity();
            Player player = entity.getKiller();
            if (player == null) {
                return;
            }
            Clan clan = clanService.getClanPlayer(player);
            if (clan == null) {
                return;
            }
            ClanMember member = clan.getMembers().getMember(player);
            if (member == null) {
                return;
            }
            member.getQuests().stream()
                    .filter(quest -> !quest.isFinished())
                    .filter(quest -> quest.isTarget(entity.getType()))
                    .forEach(quest -> quest.changeCount(1));
        }).event(BlockBreakEvent.class, event -> {
            Player player = event.getPlayer();
            Clan clan = clanService.getClanPlayer(player);
            if (clan == null) {
                return;
            }
            ClanMember member = clan.getMembers().getMember(player);
            if (member == null) {
                return;
            }
            member.getQuests().stream()
                    .filter(quest -> !quest.isFinished())
                    .filter(quest -> quest.isTarget(event.getBlock().getType()))
                    .forEach(quest -> quest.changeCount(1));
        }).register(plugin);
    }

    @Override
    public void cancel() {
        schedulers.forEach(Scheduler::cancel);
    }

    private static class QuestUpdateTask extends Scheduler {

        private final MemberDayQuestsService questsService;
        private final ClanService clanService;

        private QuestUpdateTask(Plugin plugin, MemberDayQuestsService questsService, ClanService clanService) {
            super(plugin);
            this.questsService = questsService;
            this.clanService = clanService;
            runTaskTimerAsynchronously(SchedulerTicks.SECOND);
        }

        @Override
        public void run() {
            LocalTime time = LocalTime.now(DateZone.MOSCOW.getIdentifier());
            if (time.getHour() != 0 || time.getMinute() != 0 || time.getSecond() == 0) {
                return;
            }

            clanService.getAllClans().forEach(clan -> clan.getMembers().getAllMembers().forEach(member -> member.changeQuests(questsService.getQuests(member).generateRandom(member))));
        }
    }
}
