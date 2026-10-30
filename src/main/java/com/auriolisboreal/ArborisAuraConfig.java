package com.auriolisboreal;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public final class ArborisAuraConfig {
    private static final int[] DEFAULT_CAPACITY = {0, 100, 120, 145, 165, 190, 210, 235, 255, 280, 300};
    private static final double[] DEFAULT_CONSUMPTION = {0, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55};
    private static final int[] DEFAULT_COCOON_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int[] DEFAULT_DRYAD_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int[] DEFAULT_MICROBIOME_RADIUS = {0, 3, 4, 6, 7, 9, 10, 12, 13, 15, 16};

    private static final int[] CAPACITY = DEFAULT_CAPACITY.clone();
    private static final double[] CONSUMPTION_PER_DAY = DEFAULT_CONSUMPTION.clone();
    private static final int[] COCOON_SLOTS = DEFAULT_COCOON_SLOTS.clone();
    private static final int[] DRYAD_SLOTS = DEFAULT_DRYAD_SLOTS.clone();
    private static final int[] MICROBIOME_RADIUS = DEFAULT_MICROBIOME_RADIUS.clone();

    private static float trunkHardness = 1.5F;
    private static float trunkResistance = 10.0F;
    private static int fireEncouragement = 1;
    private static int fireFlammability = 1;

    private static int leavesLight = 3;
    private static int tallGrassLight = 1;
    private static int saplingLight = 5;
    private static int grassBlockLight = 0;

    private static boolean removePlants = true;
    private static int plantChancePercent = 12;
    private static int columnsPerStep = 30;
    private static int rescanDelaySeconds = 10;
    private static int absorbHeight = 24;
    private static double nutritionPlant = 1.0;
    private static double nutritionFlower = 2.0;
    private static double nutritionCrop = 3.0;
    private static double nutritionLog = 4.0;
    private static double nutritionLeaves = 0.5;
    private static int saplingGrowthTicks = 600;

    private ArborisAuraConfig() {
    }

    public static void load(File file) {
        Configuration config = new Configuration(file);
        config.load();

        trunkHardness = config.getFloat("hardness", "trunk", 1.5F, 0.0F, 2000.0F,
                "Block hardness of the trunk and the central block. Stone = 1.5, oak log = 2.0, obsidian = 50.");
        trunkResistance = config.getFloat("resistance", "trunk", 10.0F, 0.0F, 6000000.0F,
                "Explosion resistance. Stone = 10, oak log = 2.");
        fireEncouragement = config.getInt("fire_encouragement", "trunk", 1, 0, 300,
                "How easily fire catches. Oak log = 5. 0 = never.");
        fireFlammability = config.getInt("fire_flammability", "trunk", 1, 0, 300,
                "How fast it burns away. Oak log = 5. 0 = never.");

        leavesLight = config.getInt("leaves_light", "light", 3, 0, 15,
                "Light emitted by the Arboris Aura leaves (0-15). Applies to blocks updated after the change.");
        tallGrassLight = config.getInt("tallgrass_light", "light", 1, 0, 15,
                "Light emitted by the Arboris Aura tall grass (0-15).");
        saplingLight = config.getInt("sapling_light", "light", 5, 0, 15,
                "Light emitted by the Arboris Aura sapling (0-15).");
        grassBlockLight = config.getInt("grass_block_light", "light", 0, 0, 15,
                "Light emitted by the Aura grass block of the micro-biome (0-15).");

        removePlants = config.getBoolean("remove_plants", "microbiome", true,
                "The micro-biome absorbs (removes) vegetation that is not its own: plants, crops, flowers, logs and leaves.");
        plantChancePercent = config.getInt("plant_chance_percent", "microbiome", 12, 0, 100,
                "Chance (%) of an Aura tall grass appearing on each converted column.");
        columnsPerStep = config.getInt("columns_per_step", "microbiome", 30, 1, 1000,
                "Columns processed per second while the micro-biome grows. Lower = less lag.");
        rescanDelaySeconds = config.getInt("rescan_delay_seconds", "microbiome", 10, 1, 3600,
                "Seconds to wait between full passes over the micro-biome (re-converts normal grass, absorbs new vegetation).");
        absorbHeight = config.getInt("absorb_height", "microbiome", 24, 1, 64,
                "How many blocks above the ground are checked for vegetation to absorb.");
        nutritionPlant = config.get("microbiome", "nutrition_plant", 1.0,
                "Nutrition gained by absorbing a small plant (grass, bush, mushroom).", 0.0, 1000.0).getDouble();
        nutritionFlower = config.get("microbiome", "nutrition_flower", 2.0,
                "Nutrition gained by absorbing a flower.", 0.0, 1000.0).getDouble();
        nutritionCrop = config.get("microbiome", "nutrition_crop", 3.0,
                "Nutrition gained by absorbing a crop.", 0.0, 1000.0).getDouble();
        nutritionLog = config.get("microbiome", "nutrition_log", 4.0,
                "Nutrition gained by absorbing a log.", 0.0, 1000.0).getDouble();
        nutritionLeaves = config.get("microbiome", "nutrition_leaves", 0.5,
                "Nutrition gained by absorbing a leaf block.", 0.0, 1000.0).getDouble();
        saplingGrowthTicks = config.getInt("growth_ticks", "sapling", 600, 20, 24000000,
                "Ticks the sapling needs to become a tree. 24000 = one Minecraft day.");

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
            MICROBIOME_RADIUS[phase] = config.getInt(
                    "microbiome_radius",
                    category,
                    DEFAULT_MICROBIOME_RADIUS[phase],
                    0,
                    32,
                    "Radius of the micro-biome at this phase."
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

    public static int getBiomeRadius(int phase) {
        return MICROBIOME_RADIUS[Math.max(0, Math.min(10, phase))];
    }

    public static float getTrunkHardness() {
        return trunkHardness;
    }

    public static float getTrunkResistance() {
        return trunkResistance;
    }

    public static int getFireEncouragement() {
        return fireEncouragement;
    }

    public static int getFireFlammability() {
        return fireFlammability;
    }

    public static int getLeavesLight() {
        return leavesLight;
    }

    public static int getTallGrassLight() {
        return tallGrassLight;
    }

    public static int getSaplingLight() {
        return saplingLight;
    }

    public static int getGrassBlockLight() {
        return grassBlockLight;
    }

    public static boolean isRemovePlants() {
        return removePlants;
    }

    public static int getPlantChancePercent() {
        return plantChancePercent;
    }

    public static int getColumnsPerStep() {
        return columnsPerStep;
    }

    public static int getRescanDelaySeconds() {
        return rescanDelaySeconds;
    }

    public static int getAbsorbHeight() {
        return absorbHeight;
    }

    public static double getNutritionPlant() {
        return nutritionPlant;
    }

    public static double getNutritionFlower() {
        return nutritionFlower;
    }

    public static double getNutritionCrop() {
        return nutritionCrop;
    }

    public static double getNutritionLog() {
        return nutritionLog;
    }

    public static double getNutritionLeaves() {
        return nutritionLeaves;
    }

    public static int getSaplingGrowthTicks() {
        return saplingGrowthTicks;
    }
}
