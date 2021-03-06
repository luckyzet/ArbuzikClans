package me.luckyzz.arbuzikclans.clan.member.rank;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RankPossibilities {

    private final Set<RankPossibility> possibilities;

    public RankPossibilities(RankPossibility... possibilities) {
        this.possibilities = ImmutableSet.copyOf(possibilities);
    }

    public RankPossibilities(Set<RankPossibility> possibilities) {
        this.possibilities = ImmutableSet.copyOf(possibilities);
    }

    public RankPossibilities() {
        this(new HashSet<>());
    }

    public static RankPossibilities allExcept(RankPossibility... possibilities) {
        Collection<RankPossibility> except = Arrays.asList(possibilities);

        Set<RankPossibility> rankPossibilities = new HashSet<>();
        Arrays.stream(possibilities).forEach(possibility -> {
            if(except.contains(possibility)) {
                return;
            }
            rankPossibilities.add(possibility);
        });

        return new RankPossibilities(rankPossibilities);
    }

    public Collection<RankPossibility> getPossibilities() {
        return possibilities;
    }

    public boolean hasPossibility(RankPossibility possibility) {
        return possibilities.contains(possibility);
    }
}
