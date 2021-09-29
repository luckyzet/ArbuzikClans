package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuests;
import me.luckyzz.arbuzikclans.clan.member.quest.MemberDayQuestsService;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestComplexity;
import me.luckyzz.arbuzikclans.clan.member.quest.QuestType;
import me.luckyzz.arbuzikclans.config.ClanConfig;
import me.luckyzz.arbuzikclans.config.Messages;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfigMemberDayQuestsService implements MemberDayQuestsService {

    private final ClanConfig config;
    private final MessageConfig<Messages> messageConfig;
    private final QueryExecutors executors;

    private final Map<QuestComplexity, MemberDayQuests> questsMap = new HashMap<>();

    public ConfigMemberDayQuestsService(ClanConfig config, MessageConfig<Messages> messageConfig, QueryExecutors executors) {
        this.config = config;
        this.messageConfig = messageConfig;
        this.executors = executors;
        reload();
    }

    @Override
    public void reload() {
        questsMap.clear();

        ConfigurationSection mainSection = config.getSection("quests");
        if (mainSection != null) {
            mainSection.getKeys(false).forEach(complexityString -> {
                QuestComplexity complexity = QuestComplexity.fromString(complexityString);
                ConfigurationSection section = mainSection.getConfigurationSection(complexityString);
                if (complexity == null || section == null) {
                    return;
                }

                int minCount = section.getInt("minCount");
                int maxCount = section.getInt("maxCount");
                Map<QuestType, MemberDayQuests.Quests> questsMap = new HashMap<>();

                MemberDayQuests memberDayQuests = new MemberDayQuestsImpl(complexity, minCount, maxCount, questsMap);

                ConfigurationSection questSection = section.getConfigurationSection("quests");
                if(questSection == null) {
                    return;
                }
                questSection.getKeys(false).forEach(sectionName -> {
                    ConfigurationSection current = questSection.getConfigurationSection(sectionName);
                    if (current == null) {
                        return;
                    }

                    QuestType type = QuestType.fromString(current.getString("type"));
                    if (type == null) {
                        return;
                    }
                    MemberDayQuestsQuestsImpl quests = (MemberDayQuestsQuestsImpl) questsMap.get(type);
                    if (quests == null) {
                        quests = new MemberDayQuestsQuestsImpl(memberDayQuests, new ArrayList<>());
                        questsMap.put(type, quests);
                    }

                    String targetString = current.getString("target");
                    if (targetString == null) {
                        return;
                    }
                    int minCountQuest = current.getInt("minCount");
                    int maxCountQuest = current.getInt("maxCount");
                    if (type == QuestType.KILL) {
                        EntityType target = EntityType.valueOf(targetString);
                        quests.addQuest(new MemberDayQuestImpl(messageConfig, executors, type, target, minCountQuest, maxCountQuest));
                        return;
                    }
                    if (type == QuestType.BREAK_BLOCKS) {
                        Material target = Material.getMaterial(targetString);
                        quests.addQuest(new MemberDayQuestImpl(messageConfig, executors, type, target, minCountQuest, maxCountQuest));
                    }
                });

                this.questsMap.put(complexity, memberDayQuests);
            });
        }
    }

    @Override
    public MemberDayQuests getQuests(QuestComplexity complexity) {
        return questsMap.get(complexity);
    }

    @Override
    public MemberDayQuests getQuests(ClanMember member) {
        return getQuests(config.getQuestComplexity(member.getQuestsCompleted()));
    }

    @Override
    public void cancel() {
        questsMap.clear();
    }
}
