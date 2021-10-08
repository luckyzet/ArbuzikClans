package me.luckyzz.arbuzikclans.clan.upgrade;

import me.luckkyyz.luckapi.api.Typable;
import me.luckyzz.arbuzikclans.clan.Clan;

public interface UpgradeRequirement extends Typable<UpgradeRequirement.RequirementType> {

    enum RequirementType {

        COINS,
        MEMBERS

    }

    boolean isApplicable(Clan clan);

    void applyRequirement(Clan clan);

}
