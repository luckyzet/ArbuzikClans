package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.upgrade.ClanUpgrade;
import me.luckyzz.arbuzikclans.clan.upgrade.UpgradeData;
import me.luckyzz.arbuzikclans.clan.upgrade.UpgradeRequirement;
import me.luckyzz.arbuzikclans.clan.upgrade.UpgradeType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Set;

class ClanUpgradeImpl implements ClanUpgrade {

    private final QueryExecutors executors;

    private final int index;
    private final UpgradeType upgradeType;
    private final UpgradeData upgradeData;
    private final Set<UpgradeRequirement> requirements;

    ClanUpgradeImpl(QueryExecutors executors, int index, UpgradeType upgradeType, UpgradeData upgradeData, Set<UpgradeRequirement> requirements) {
        this.executors = executors;
        this.index = index;
        this.upgradeType = upgradeType;
        this.upgradeData = upgradeData;
        this.requirements = requirements;

        if (upgradeType == UpgradeType.MEMBER_SLOTS) {
            this.requirements.add(new MemberUpgradeUpgradeRequirement((MemberUpgradeData) upgradeData));
        }
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public UpgradeType getType() {
        return upgradeType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends UpgradeData> T getData() {
        return (T) upgradeData;
    }

    @Override
    public Collection<UpgradeRequirement> getRequirements() {
        return requirements;
    }

    @Override
    public void applyUpgrade(Clan clan) {
        if (!isApplicable(clan)) {
            return;
        }
        if (clan.getUpgrades().getUpgrades().stream().anyMatch(upgrade -> upgrade.getIndex() == index)) {
            return;
        }
        requirements.forEach(requirement -> requirement.applyRequirement(clan));

        executors.async().update("INSERT INTO clanUpgrades VALUES (?, ?)", clan.getId(), index);
    }

    @Override
    public void removeUpgrade(Clan clan) {
        if (clan.getUpgrades().getUpgrades().stream().noneMatch(upgrade -> upgrade.getIndex() == index)) {
            return;
        }
        executors.async().update("DELETE FROM clanUpgrades WHERE clan = ? AND id = ?", clan.getId(), index);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClanUpgradeImpl that = (ClanUpgradeImpl) o;
        return new EqualsBuilder()
                .append(index, that.index)
                .append(upgradeType, that.upgradeType)
                .append(upgradeData, that.upgradeData)
                .append(requirements, that.requirements)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .append(upgradeType)
                .append(upgradeData)
                .append(requirements)
                .toHashCode();
    }
}
