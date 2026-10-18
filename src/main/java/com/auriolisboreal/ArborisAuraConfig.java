package com.auriolisboreal;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class ArborisAuraConfig {
    private static final int[] DEFAULT_CAPACITY = {0, 100, 120, 145, 165, 190, 210, 235, 255, 280, 300};
    private static final double[] DEFAULT_CONSUMPTION = {0, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55};
    private static final int[] DEFAULT_COCOON_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int[] DEFAULT_DRYAD_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private static final int[] CAPACITY = DEFAULT_CAPACITY.clone();
    private static final double[] CONSUMPTION_PER_DAY = DEFAULT_CONSUMPTION.clone();
    private static final int[] COCOON_SLOTS = DEFAULT_COCOON_SLOTS.clone();
    private static final int[] DRYAD_SLOTS = DEFAULT_DRYAD_SLOTS.clone();

    private ArborisAuraConfig() {
    }

    public static void load(File file) {
        Configuration config = new Configuration(file);
        config.load();

        for (int phase = 0; phase <= 10; phase++) {
            String category = "phases.phase_" + phase;
            CAPACITY[phase] = config.getInt(
                    "capacity",
                    category,
                    DEFAULT_CAPACITY[phase],
                    0,
                    1000000,
                    "Maximum nutrition stored at this phase."
            );
            CONSUMPTION_PER_DAY[phase] = config.get(
                    category,
                    "consumption_per_day",
                    DEFAULT_CONSUMPTION[phase],
                    "Nutrition consumed per Minecraft day.",
                    0.0,
                    1000000.0
            ).getDouble();
            COCOON_SLOTS[phase] = config.getInt(
                    "cocoon_slots",
                    category,
                    DEFAULT_COCOON_SLOTS[phase],
                    0,
                    1000,
                    "Reserved cocoon capacity; not used yet."
            );
            DRYAD_SLOTS[phase] = config.getInt(
                    "dryad_slots",
                    category,
                    DEFAULT_DRYAD_SLOTS[phase],
                    0,
                    1000,
                    "Reserved dryad capacity; not used yet."
            );
        }

        config.save();
    }

    public static int getCapacity(int phase) {
        return CAPACITY[phase];
    }

    public static double getConsumptionPerDay(int phase) {
        return CONSUMPTION_PER_DAY[phase];
    }

    public static int getCocoonSlots(int phase) {
        return COCOON_SLOTS[phase];
    }

    public static int getDryadSlots(int phase) {
        return DRYAD_SLOTS[phase];
    }
}
