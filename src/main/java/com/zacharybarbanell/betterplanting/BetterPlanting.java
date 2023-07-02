package com.zacharybarbanell.betterplanting;

import java.nio.file.Paths;

import com.zacharybarbanell.betterplanting.config.Config;
import com.zacharybarbanell.betterplanting.config.ConfigEntry;

import net.minecraftforge.fml.common.Mod;

@Mod("betterplanting")
public class BetterPlanting {
    private static Config config = new Config(Paths.get("config/betterplanting.cfg"), "Better Planting config");

    public static ConfigEntry<Boolean> autoSelectCrops = config.register("autoSelectCrops", true)
            .comment("Whether the mod should try to automatically determine which crops to handle.  " +
                    "If this is set to false, modded items must be tagged with betterplanting:plantable to be planted.");

    public static ConfigEntry<Double> minHeight = config.register("minHeight", 0.864)
            .comment("The minimum height that items must fall to be planted.  " +
                    "Setting this below 0.864 is not recommended, since it will allow items to be planted after being broken.")
            .addRangeRestriction(0d, Double.POSITIVE_INFINITY);

    public BetterPlanting() {
        config.load();
    }
}
