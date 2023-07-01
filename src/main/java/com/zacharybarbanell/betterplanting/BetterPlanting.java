package com.zacharybarbanell.betterplanting;

import java.nio.file.Paths;

import com.zacharybarbanell.betterplanting.config.Config;
import com.zacharybarbanell.betterplanting.config.ConfigEntry;

import net.minecraftforge.fml.common.Mod;

@Mod("betterplanting")
public class BetterPlanting {
    private static Config config = new Config(Paths.get("config/betterplanting.cfg"), "Better Planting config");

    public static ConfigEntry<Boolean> autoSelectCrops = config.register("autoSelectCrops", true);
    public static ConfigEntry<Double> minHeight = config.register("minHeight", 0.864);

    public BetterPlanting() {
        config.load();
    }
}
