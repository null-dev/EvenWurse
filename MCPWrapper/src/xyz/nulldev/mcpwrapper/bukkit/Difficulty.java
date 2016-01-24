package xyz.nulldev.mcpwrapper.bukkit;

import com.google.common.collect.Maps;
import net.minecraft.world.EnumDifficulty;

import java.util.Map;

/**
 * Represents the various difficulty levels that are available.
 */
public enum Difficulty {
    /**
     * Players regain health over time, hostile mobs don't spawn, the hunger
     * bar does not deplete.
     */
    PEACEFUL(0),

    /**
     * Hostile mobs spawn, enemies deal less damage than on normal difficulty,
     * the hunger bar does deplete and starving deals up to 5 hearts of
     * damage. (Default value)
     */
    EASY(1),

    /**
     * Hostile mobs spawn, enemies deal normal amounts of damage, the hunger
     * bar does deplete and starving deals up to 9.5 hearts of damage.
     */
    NORMAL(2),

    /**
     * Hostile mobs spawn, enemies deal greater damage than on normal
     * difficulty, the hunger bar does deplete and starving can kill players.
     */
    HARD(3);

    private final static Map<Integer, Difficulty> BY_ID = Maps.newHashMap();

    static {
        for (Difficulty diff : values()) {
            BY_ID.put(diff.value, diff);
        }
    }

    private final int value;
    private Difficulty(final int value) {
        this.value = value;
    }

    public static EnumDifficulty asMCDifficulty(Difficulty difficulty) {
        if (difficulty == null) return null;
        if (difficulty.equals(Difficulty.PEACEFUL)) {
            return EnumDifficulty.PEACEFUL;
        } else if (difficulty.equals(Difficulty.EASY)) {
            return EnumDifficulty.EASY;
        } else if (difficulty.equals(Difficulty.NORMAL)) {
            return EnumDifficulty.NORMAL;
        } else if (difficulty.equals(Difficulty.HARD)) {
            return EnumDifficulty.HARD;
        } else {
            throw new IllegalArgumentException("Unknown difficulty!");
        }
    }

    public static Difficulty fromMCDifficulty(EnumDifficulty difficulty) {
        if (difficulty == null) return null;
        if (difficulty.equals(EnumDifficulty.PEACEFUL)) {
            return Difficulty.PEACEFUL;
        } else if (difficulty.equals(EnumDifficulty.EASY)) {
            return Difficulty.EASY;
        } else if (difficulty.equals(EnumDifficulty.NORMAL)) {
            return Difficulty.NORMAL;
        } else if (difficulty.equals(EnumDifficulty.HARD)) {
            return Difficulty.HARD;
        } else {
            throw new IllegalArgumentException("Unknown difficulty!");
        }
    }

    /**
     * Gets the Difficulty represented by the specified value
     *
     * @param value Value to check
     * @return Associative {@link Difficulty} with the given value, or null if
     * it doesn't exist
     * @deprecated Magic value
     */
    @Deprecated
    public static Difficulty getByValue(final int value) {
        return BY_ID.get(value);
    }

    /**
     * Gets the difficulty value associated with this Difficulty.
     *
     * @return An integer value of this difficulty
     * @deprecated Magic value
     */
    @Deprecated
    public int getValue() {
        return value;
    }
}
