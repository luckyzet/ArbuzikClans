package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberQuest;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestType;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

class MemberQuestImpl implements MemberQuest {

    private final QueryExecutors executors;
    private final MessageConfig<Messages> messageConfig;
    private final String display;
    private final QuestType type;
    private final Object target;
    private final int needCount, coins;
    private ClanMember member;
    private int count;

    MemberQuestImpl(QueryExecutors executors, MessageConfig<Messages> messageConfig, String display, QuestType type, Object target, int coins, int needCount, int count) {
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.display = display;
        this.type = type;
        this.target = target;
        this.coins = coins;
        this.needCount = needCount;
        this.count = count;
    }

    MemberQuestImpl(ClanMember member, QueryExecutors executors, MessageConfig<Messages> messageConfig, String display, QuestType type, Object target, int coins, int needCount, int count) {
        this.member = member;
        this.executors = executors;
        this.messageConfig = messageConfig;
        this.display = display;
        this.type = type;
        this.target = target;
        this.coins = coins;
        this.needCount = needCount;
        this.count = count;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public String getTargetName() {
        return target instanceof EntityType ? ((EntityType) target).name() :
                target instanceof Material ? ((Material) target).name() : "null";
    }

    @Override
    public String getDisplay() {
        return display;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public QuestType getType() {
        return type;
    }

    @Override
    public ClanMember getMember() {
        return member;
    }

    void setMember(ClanMember member) {
        this.member = member;
    }

    @Override
    public int getNeedCount() {
        return needCount;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void changeCount(int amount) {
        if (isFinished()) {
            return;
        }

        this.count += amount;

        executors.async().update("UPDATE clanQuests SET count = ? WHERE name = ?", this.count, member.getName());

        if (isFinished()) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_QUESTS_FINISHED).send(player));
            member.addQuestCompleted();
            member.getClan().changeCoinsQuest(coins, member);
        } else {
            member.apply(player -> messageConfig.getAdaptiveMessage(Messages.CLAN_QUESTS_COUNT_CHANGE)
                    .placeholder("%count%", this.count)
                    .placeholder("%max%", needCount)
                    .placeholder("%percent%", ((int) (((double) this.count / (double) needCount) * 100)) + "%")
                    .send(player));
        }
    }

}
