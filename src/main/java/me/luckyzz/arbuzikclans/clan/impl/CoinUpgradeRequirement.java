package me.luckyzz.arbuzikclans.clan.impl;

import me.luckyzz.arbuzikclans.clan.Clan;
import me.luckyzz.arbuzikclans.clan.upgrade.UpgradeRequirement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class CoinUpgradeRequirement implements UpgradeRequirement {

    private final int coins;

    CoinUpgradeRequirement(int coins) {
        this.coins = coins;
    }

    public int getCoins() {
        return coins;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.COINS;
    }

    @Override
    public boolean isApplicable(Clan clan) {
        return clan.getCoins() >= coins;
    }

    @Override
    public void applyRequirement(Clan clan) {
        clan.changeCoinsSilently(-coins);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinUpgradeRequirement that = (CoinUpgradeRequirement) o;
        return new EqualsBuilder().append(coins, that.coins).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(coins).toHashCode();
    }
}
