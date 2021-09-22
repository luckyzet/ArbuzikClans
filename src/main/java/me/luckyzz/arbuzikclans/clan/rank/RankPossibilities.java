package me.luckyzz.arbuzikclans.clan.rank;

import me.luckkyyz.luckapi.api.Creatable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RankPossibilities {

    private final Set<Integer> indexes;
    private final Set<RankPossibility> possibilities;

    private RankPossibilities(Set<Integer> indexes, Set<RankPossibility> possibilities) {
        this.indexes = indexes;
        this.possibilities = possibilities;
    }

    public int getSingleIndex() {
        return indexes.stream().findFirst().orElse(-1);
    }

    public boolean isIndex(int index) {
        return indexes.contains(index);
    }

    public boolean hasPossibility(RankPossibility possibility) {
        return possibilities.contains(possibility);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder implements Creatable<RankPossibilities> {

        private final Set<Integer> indexes = new HashSet<>();
        private final Set<RankPossibility> possibilities = new HashSet<>();

        private Builder() {
        }

        public Builder addIndex(int index) {
            indexes.add(index);
            return this;
        }

        public Builder addIndexes(Integer... indexes) {
            this.indexes.addAll(Arrays.asList(indexes));
            return this;
        }

        public Builder addPossibility(RankPossibility possibility) {
            possibilities.add(possibility);
            return this;
        }

        public Builder addPossibilities(RankPossibility... possibilities) {
            this.possibilities.addAll(Arrays.asList(possibilities));
            return this;
        }

        @Override
        public RankPossibilities create() {
            return new RankPossibilities(indexes, possibilities);
        }
    }
}
