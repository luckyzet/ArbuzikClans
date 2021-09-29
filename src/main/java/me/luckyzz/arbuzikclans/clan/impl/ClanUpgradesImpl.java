package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.config.MessageConfig;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.member.ClanMember;
import me.luckyzz.arbuzikclans.clan.member.rank.RankPossibility;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrade;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrades;
import me.luckyzz.arbuzikclans.config.Messages;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Set;

class ClanUpgradesImpl implements ClanUpgrades {

    private final MessageConfig<Messages> messageConfig;
    private final Set<ClanUpgrade> upgrades;
    private Clan clan;

    ClanUpgradesImpl(MessageConfig<Messages> messageConfig, Set<ClanUpgrade> upgrades) {
        this.messageConfig = messageConfig;
        this.upgrades = upgrades;
    }

    @Override
    public Clan getClan() {
        return clan;
    }

    void setClan(Clan clan) {
        this.clan = clan;
    }

    @Override
    public Collection<ClanUpgrade> getUpgrades() {
        return upgrades;
    }

    @Override
    public void addUpgrade(ClanUpgrade upgrade, ClanMember member) {
        if (!member.hasPossibility(RankPossibility.UPGRADE)) {
            member.apply(player -> messageConfig.getMessage(Messages.NOT_ACCESS).send(player));
            return;
        }
        if (!upgrade.isApplicable(clan) || clan.getUpgrades().getUpgrades().stream().anyMatch(upgrade1 -> upgrade1.getIndex() == upgrade.getIndex())) {
            member.apply(player -> messageConfig.getMessage(Messages.CLAN_UPGRADE_CANNOT_APPLY).send(player));
            return;
        }

        upgrade.applyUpgrade(clan);
        upgrades.add(upgrade);

        member.getClan().send(upgrade.getData().getClanMessage(member));
    }

    @Override
    public void removeUpgradeSilently(ClanUpgrade upgrade) {
        if (clan.getUpgrades().getUpgrades().stream().noneMatch(upgrade1 -> upgrade1.getIndex() == upgrade.getIndex())) {
            return;
        }
        upgrade.removeUpgrade(clan);
        upgrades.remove(upgrade);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanUpgradesImpl that = (ClanUpgradesImpl) o;
        return new EqualsBuilder().append(messageConfig, that.messageConfig).append(clan, that.clan).append(upgrades, that.upgrades).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(messageConfig).append(clan).append(upgrades).toHashCode();
    }
}
