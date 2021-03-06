package me.luckyzz.arbuzikclans.clan.impl;

import me.luckkyyz.luckapi.database.QueryExecutors;
import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.upgrade.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Set;

class ClanUpgradeImpl implements ClanUpgrade {

    private final QueryExecutors executors;

    private final int index;
    private final ItemStack itemStack;
    private final UpgradeType upgradeType;
    private final UpgradeData upgradeData;
    private final Set<UpgradeRequirement> requirements;
    private final Set<UpgradeShowRequirement> showRequirements;

    ClanUpgradeImpl(QueryExecutors executors, int index, ItemStack itemStack, UpgradeType upgradeType, UpgradeData upgradeData, Set<UpgradeRequirement> requirements, Set<UpgradeShowRequirement> showRequirements) {
        this.executors = executors;
        this.index = index;
        this.itemStack = itemStack;
        this.upgradeType = upgradeType;
        this.upgradeData = upgradeData;
        this.requirements = requirements;
        this.showRequirements = showRequirements;

        if (upgradeType == UpgradeType.MEMBER_SLOTS) {
            this.requirements.add(new MemberUpgradeUpgradeRequirement((MemberUpgradeData) upgradeData));
        }
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public ItemStack getItem() {
        return itemStack;
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
    public Collection<UpgradeShowRequirement> getShowRequirements() {
        return showRequirements;
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
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(index)
                .toHashCode();
    }
}
